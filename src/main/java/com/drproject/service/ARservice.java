package com.drproject.service;

import com.drproject.entity.Activity;
import com.drproject.entity.Question;
import com.drproject.entity.Quiz;
import com.drproject.repository.ARRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Handler;

@Service
public class ARservice {
    private final ARRepository arRepository;

    public ARservice(ARRepository arRepository) {
        this.arRepository = arRepository;
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
/*
    public HashMap<String,Object> getStudentGradeForQuiz(String username, String uuid){
        HashMap<String, Object> res = new HashMap<>();
        if(arRepository.existsById(uuid)){
            Activity activity = arRepository.getActivityById(uuid);
            if(activity instanceof Quiz){
                Quiz quiz = (Quiz)activity;
                List<Question> questions = quiz.getQuestions();
                for()
            }
            else {
                res.put("status", false);
                res.put("msg", "Activity is not a quiz");
                res.put("obj", null);
            }
        }
    }


 */
}
