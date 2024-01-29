package com.drproject.service;

import com.drproject.entity.*;
import com.drproject.repository.ARRepository;
import com.drproject.repository.ClassroomRepository;
import com.drproject.repository.UserRepository;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class ARservice {
    private final ARRepository arRepository;
    private final UserRepository userRepository;
    private final ClassroomRepository classroomRepository;

    public ARservice(ARRepository arRepository, UserRepository userRepository, ClassroomRepository classroomRepository) {
        this.arRepository = arRepository;
        this.userRepository = userRepository;
        this.classroomRepository = classroomRepository;
    }

    public HashMap<String, Object> getAR(String uuid){
        HashMap<String, Object> res = new HashMap<>();
        if(arRepository.existsById(uuid)){
            res.put("obj",arRepository.getActivityById(uuid));
            res.put("msg","activity object found and returned successfully");
            res.put("status",true);
            return res;
        }
        res.put("obj",null);
        res.put("msg", "failed to find activity object");
        res.put("status", false);
        return res;
    }

    public HashMap<String, Object> getStudentGradeForClass(String username, String classroomCode){
        HashMap<String, Object> res = new HashMap<>();
        User user = userRepository.getUserByUsername(username);
        double score = 0;

        if(classroomRepository.existsByCode(classroomCode)){
            Classroom classroom = classroomRepository.getClassroomByCode(classroomCode);
            List<Section> sections = classroom.getSections();
            for(Section s: sections){
                HashMap<String,Object> hashMap = getStudentGradeForSection(username,classroomCode,s.getId());
                if (hashMap.get("obj").equals(null)){
                    score += 0;
                }
                else {
                    score += Double.parseDouble((String) hashMap.get("obj"));
                }
            }
            String out = "" + (score/classroom.getSections().size() *100);
            res.put("obj", out);
            res.put("msg", "classroom score for user returned successfully");
            res.put("status", true);
            return res;
        }
        res.put("obj", null);
        res.put("msg", "classroom doesn't exist");
        res.put("status", true);
        return res;
    }

    public HashMap<String,Object> getStudentGradeForSection(String username, String classroomCode, String sectionUUID){
        HashMap<String, Object> res = new HashMap<>();
        User user = userRepository.getUserByUsername(username);
        double score = 0;

        if(classroomRepository.existsByCode(classroomCode)){
            Classroom classroom = classroomRepository.getClassroomByCode(classroomCode);
            List<Section> sections = classroom.getSections();
            for(Section s: sections) {
                if (s.getId().equals(sectionUUID)) {
                    List<Activity> sectionActivities = s.getActivities();
                    for (Activity a : sectionActivities) {
                        HashMap<String, Object> hashMappedRes = getStudentGradeForActivity(username, sectionUUID);
                        score += Double.parseDouble((String) hashMappedRes.get("obj"));
                    }
                }
            }

            String out = "" + (score / classroom.getSections().size() * 100);
            res.put("obj", out);
            res.put("msg", "returned user score in section");
            res.put("status", true);
            return res;
        }
        else {
            res.put("msg", "classroom not found");
            res.put("status", false);
            res.put("obj",null);
            return res;
        }
    }

    public HashMap<String,Object> getStudentGradeForActivity(String username, String uuid){
        HashMap<String, Object> res = new HashMap<>();
        User user = userRepository.getUserByUsername(username);
        boolean systemCheck = true;
        if(arRepository.existsById(uuid)){
            Activity activity = arRepository.getActivityById(uuid);

            if(activity instanceof Quiz){
                double score = 0;
                Quiz quiz = (Quiz)activity;
                List<Question> questions = quiz.getQuestions();
                for(Question q : questions){
                    if (!(q instanceof MultipleAnswer)){
                        systemCheck = false;
                    }
                }
                if (systemCheck){
                    for(Question q : questions) {
                        double questionScore = 0;
                        if (q instanceof MultipleAnswer) {
                            List<Choice> choiceList = ((MultipleAnswer) q).getChoiceList();
                            for (Choice c : choiceList) {
                                List<StudentChoice> studentChoiceList = c.getStudentChoices();
                                for (StudentChoice s : studentChoiceList) {
                                    if (s.getUser().equals(user)) {
                                        questionScore = Double.parseDouble(c.getCorrectness());
                                    }
                                }
                            }
                            score += questionScore;
                        }
                    }
                    String out = "" +  score / quiz.getQuestions().size() * 100;
                    res.put("obj", out);
                    res.put("status", true);
                    res.put("msg", "grade for quiz (assigned by system) returned successfully");
                    return res;
                }
                else{
                    List<StudentLongAnswer> studentLongAnswers=quiz.getStudentLongAnswers();
                    for(StudentLongAnswer s : studentLongAnswers){
                        if(s.getUser().equals(user)){
                            res.put("obj",s.getGrade());
                            res.put("status", true);
                            res.put("msg", "student grade assigned by ostad");
                            return res;
                        }
                    }
                }
            }
            else {
                List<StudentLongAnswer> studentLongAnswers=activity.getStudentLongAnswers();
                for(StudentLongAnswer s : studentLongAnswers){
                    if(s.getUser().equals(user)){
                        res.put("obj",s.getGrade());
                        res.put("status", true);
                        res.put("msg", "student grade assigned by ostad");
                        return res;
                    }
                }
                res.put("obj","0");
                res.put("status", true);
                res.put("msg", "student hasn't participated in activity");
                return res;
            }
        }
        res.put("status", false);
        res.put("msg", "activity/classroom does not exist");
        res.put("obj", null);
        return res;
    }



}
