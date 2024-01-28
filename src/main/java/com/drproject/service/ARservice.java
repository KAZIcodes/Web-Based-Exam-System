package com.drproject.service;

import com.drproject.repository.ARRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
        if(arRepository.existsById(UUID.fromString(uuid))){
            res.put("obj",arRepository.getActivityById(UUID.fromString(uuid)));
            res.put("msg","activity object found and returned successfully");
            res.put("status",true);
            return res;
        }
        res.put("obj",null);
        res.put("msg", "failed to find activity object");
        res.put("status", false);
        return res;
    }
}
