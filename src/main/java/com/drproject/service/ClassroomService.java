package com.drproject.service;

import com.drproject.entity.*;
import com.drproject.repository.ARRepository;
import com.drproject.repository.ClassroomRepository;
import com.drproject.repository.UserRepository;
import org.hibernate.sql.results.graph.entity.internal.AbstractNonJoinedEntityFetch;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ClassroomService {
    private final ClassroomRepository classroomRepository;
    private final UserRepository userRepository;
    private final ARRepository arRepository;
    public ClassroomService(ClassroomRepository classroomRepository, UserRepository userRepository, ARRepository arRepository) {
        this.classroomRepository = classroomRepository;
        this.userRepository = userRepository;
        this.arRepository = arRepository;

    }

    public HashMap<String, Object> joinClassroom(String code, String username){
        HashMap<String, Object> res = new HashMap<>();
        if(classroomRepository.existsByCode(code)){
            Classroom classroom = classroomRepository.getClassroomByCode(code);
            User user = userRepository.getUserByUsername(username);
            ClassroomRole classroomRole = new ClassroomRole();
            classroomRole.setClassroom(classroom);
            classroomRole.setUser(user);
            classroomRole.setRoleInClassroom(RoleInClassroom.STUDENT);
            classroom.getRoleInClassrooms().add(classroomRole);
            classroomRepository.save(classroom);
            user.getRoleInClassrooms().add(classroomRole);
            userRepository.save(user);
            res.put("msg","successfully joined classroom");
            res.put("status", true);
            return res;
        }
        res.put("msg","failed to join classroom");
        res.put("status", false);
        return res;
    }
    public HashMap<String, Object> addClassroom(String username, Classroom classroom){
        HashMap<String, Object> res = new HashMap<>();
        ArrayList<ClassroomRole> classroomRoles = new ArrayList<>();
        ClassroomRole classroomRole = new ClassroomRole();
        classroomRole.setRoleInClassroom(RoleInClassroom.ADMIN);
        classroomRole.setUser(userRepository.getUserByUsername(username));
        classroomRole.setClassroom(classroom);
        classroomRoles.add(classroomRole);
        classroom.setRoleInClassrooms(classroomRoles);
        classroomRepository.save(classroom);
        res.put("status",true);
        res.put("msg", "successfully created new classroom. user = classroom admin.");
        res.put("obj", null);
        return res;
    }

    public HashMap<String, Object> isInClass(String username, String code){
        HashMap<String, Object> res = new HashMap<>();
        if(classroomRepository.existsByCode(code)){
            User user = userRepository.getUserByUsername(username);
            Classroom classroom = classroomRepository.getClassroomByCode(code);
            List<ClassroomRole> classroomRoles = classroom.getRoleInClassrooms();
            for (ClassroomRole c : classroomRoles){
                if (c.getUser().equals(user)){

                    List<Section> sections = classroom.getSections();
                    List<HashMap<String, Object>> out = new ArrayList<>();
                    for (Section s : sections){
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("title", s.getTitle());
                        hashMap.put("id",s.getId());
                        List<HashMap<String, Object>> activityList = new ArrayList<>();
                        for(Activity a : s.getActivities()){
                            HashMap<String ,Object> hashMap1 = new HashMap<>();
                            hashMap1.put("title", a.getTitle());
                            hashMap1.put("id", a.getId());
                            activityList.add(hashMap1);
                        }
                        hashMap.put("activities",activityList);
                        out.add(hashMap);
                    }
                    res.put("msg","user found in classroom");
                    res.put("status", true);
                    res.put("obj", out);
                    return  res;
                }
            }

        }
        res.put("status", false);
        res.put("msg", "user not in classroom or classroom does not exist");
        res.put("obj", null);
        return res;
    }

    public HashMap<String, Object> getUserRole(String username, String code){
        HashMap<String, Object> res = new HashMap<>();
        User user = userRepository.getUserByUsername(username);
        if(classroomRepository.existsByCode(code)) {
            Classroom classroom = classroomRepository.getClassroomByCode(code);
            for (ClassroomRole c : classroom.getRoleInClassrooms()){
                if(c.getUser().equals(user)){
                    if(c.getRoleInClassroom().equals(RoleInClassroom.ADMIN)){
                        res.put("obj", "admin");
                        res.put("status", true);
                        res.put("msg", "role found");
                        return res;
                    }
                    if(c.getRoleInClassroom().equals(RoleInClassroom.TEACHER)){
                        res.put("obj", "teacher");
                        res.put("status", true);
                        res.put("msg", "role found");
                        return res;
                    }
                    if(c.getRoleInClassroom().equals(RoleInClassroom.STUDENT)){
                        res.put("obj", "student");
                        res.put("status", true);
                        res.put("msg", "role found");
                        return res;
                    }
                }
            }
        }
        res.put("obj", null);
        res.put("status", false);
        res.put("msg", "role/classroom not found");
        return res;
    }

    public HashMap<String , Object> getClassroomGlossary(String username, String code){
        HashMap<String, Object> res = new HashMap<>();
        List<HashMap<String, String>> output =new ArrayList<>();
        if(classroomRepository.existsByCode(code)) {
            Classroom classroom = classroomRepository.getClassroomByCode(code);
            List<GlossaryEntry> glossaryEntries = classroom.getGlossaryEntries();
            for(GlossaryEntry g : glossaryEntries){
                HashMap<String , String> entry = new HashMap<>();
                entry.put("value", g.getGlossaryValue());
                entry.put("key", g.getGlossaryKey());
                output.add(entry);
            }
            res.put("status", true);
            res.put("obj", output);
            res.put("msg", "successfully returned glossary");
            return res;
        }
        res.put("status", false);
        res.put("obj", output);
        res.put("msg", "failed to fetch glossary. Classroom may not exist");
        return res;
    }

    public HashMap<String, Object> updateClassroomGlossary(String classroomCode, List<HashMap<String,String>> newGlossaryList){
        HashMap<String, Object> res = new HashMap<>();
        List<HashMap<String, String>> output =new ArrayList<>();
        if(classroomRepository.existsByCode(classroomCode)) {
            Classroom classroom = classroomRepository.getClassroomByCode(classroomCode);

            List<GlossaryEntry> newEntries = new ArrayList<>();
            List<GlossaryEntry> oldEntries = classroom.getGlossaryEntries();
            for (GlossaryEntry entry : oldEntries){
                entry.setClassroom(null);
            }
            for (HashMap<String, String> h : newGlossaryList) {
                GlossaryEntry glossaryEntry = new GlossaryEntry();
                glossaryEntry.setClassroom(classroom);
                glossaryEntry.setGlossaryKey(h.get("key"));
                glossaryEntry.setGlossaryValue(h.get("value"));
                newEntries.add(glossaryEntry);
                System.out.println("\n\n\n\n\n\n\n\n\n" + "new: "+glossaryEntry.getGlossaryKey()+" " + glossaryEntry.getGlossaryValue()+"\n\n\n\n\n\n\n\n");
            }
            classroom.setGlossaryEntries(newEntries);

            for (GlossaryEntry entry : newEntries){
                entry.setClassroom(classroom);
            }
            classroomRepository.save(classroom);
        }
        res.put("status", true);
        res.put("obj", null);
        res.put("msg", "successfully changed glossary for classroom");
        return res;
    }


    // ArrayList<HashMap<String, String>>

    //
/*
    public HashMap<String, Object> getUserGrades(String username, String code){
        HashMap<String, Object> res = new HashMap<>();
        HashMap<String , String> entries = new HashMap<>();
        if(classroomRepository.existsByCode(code)) {

    }


 */
    // student
    //HashMap<string, l2<h2<String topic name, l1<h1<activityname, score>>>>>
    public HashMap<String, Object> getUserGrades(String username, String classroomCode) {
        HashMap<String, Object> res = new HashMap<>();
        List<HashMap<String, String>> output = new ArrayList<>();
        if (classroomRepository.existsByCode(classroomCode)) {
            Classroom classroom = classroomRepository.getClassroomByCode(classroomCode);
            User user = userRepository.getUserByUsername(username);
            List<Section> sections = classroom.getSections();
            List<HashMap<String,Object>> l2 = new ArrayList<>();
            for (Section s : sections) {
                HashMap<String , Object> h2 = new HashMap<>();
                List<Activity> activities = s.getActivities();
                List<HashMap<String,String>> l1 = new ArrayList<>();
                for(Activity a : activities) {
                    HashMap<String ,String> h1 = new HashMap<>();
                    String score = (String) getStudentGradeForActivity(username,a.getId()).get("obj");
                    h1.put(a.getTitle(),score);
                    l1.add(h1);
                }
                h2.put(s.getTitle(),l1);
                l2.add(h2);
            }
            res.put("obj",l2);
            res.put("status", true);
            res.put("msg", "user grades returned successfully");
            return res;
        }
        res.put("obj",null);
        res.put("status", false);
        res.put("msg", "classroom does not exist");
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

    public HashMap<String , Object> updateClassroomSections(String sectionTitle,String classroomCode, String sectionId){
        HashMap<String, Object> res = new HashMap<>();
        if(classroomRepository.existsByCode(classroomCode)) {
            Classroom classroom = classroomRepository.getClassroomByCode(classroomCode);
            if (sectionId.equals("")){
                Section newSection = new Section();
                newSection.setTitle(sectionTitle);
                classroom.getSections().add(newSection);
                List<Section> sections =classroom.getSections();
                for(Section s:sections){
                    s.setClassroom(classroom);
                }
                classroomRepository.save(classroom);
                res.put("obj",null);
                res.put("msg", "successfully added new section to classroom");
                res.put("status", true);
                return res;
            }
            else {
                List<Section> sections = classroom.getSections();
                for(Section s : sections){
                    if (s.getId().equals(sectionId)){
                        s.setTitle(sectionTitle);
                        classroomRepository.save(classroom);
                        res.put("obj",null);
                        res.put("msg", "successfully changed classroomTitle");
                        res.put("status", true);
                        return res;
                    }
                }
            }
        }
        res.put("obj",null);
        res.put("msg", "classroom not found");
        res.put("status", false);
        return res;
    }
}
