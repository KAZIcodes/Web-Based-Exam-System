package com.drproject.service;

import com.drproject.entity.Classroom;
import com.drproject.entity.ClassroomRole;
import com.drproject.entity.RoleInClassroom;
import com.drproject.entity.User;
import com.drproject.repository.UserRepository;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

@Service
public class UserService {
    public final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public HashMap<String , Object> signUpUser(User user) {
        HashMap<String, Object> res = new HashMap<>();
        if (userRepository.existsByUsername(user.getUsername())) {
            res.put("status", false);
            res.put("msg", "username does not exist");
            res.put("obj",null);

        }
        if (userRepository.existsByEmail(user.getEmail())){
            res.put("status", false);
            res.put("msg", "username does not exist");
            res.put("obj",null);
        }
        try {
            user.setPassword(encryptString(user.getPassword()));
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            res.put("status", false);
            res.put("msg", "username does not exist");
            res.put("obj",null);
        }

        userRepository.save(user);
        return res;
    }
    public HashMap<String , Object> checkCredentials(String email, String password){
        HashMap<String, Object> res = new HashMap<>();
        if(userRepository.existsByEmail(email)){
            User user = userRepository.getUserByEmail(email);
            try {
                if (user.getPassword().equals(encryptString(password))){
                    res.put("found",true);
                    res.put("username",user.getUsername());
                }
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException){
                res.put("found",false);
            }
        }
        res.put("found",false);
        return res;
    }

    public HashMap<String , Object> getUserInfo(String username){
        HashMap<String, Object> res = new HashMap<>();
        if (userRepository.existsByUsername(username)){
            res.put("obj", userRepository.getUserByUsername(username));
            res.put("status", true);
            res.put("msg", "user info returned");
            return res;
        }
        res.put("status", false);
        res.put("obj", null);
        res.put("msg","failed to fetch user info");
        return res;
    }


    public HashMap<String, Object> getUserClassrooms(String username){
        HashMap<String, Object> res = new HashMap<>();
        User user = userRepository.getUserByUsername(username);
        ArrayList<Classroom> classrooms = new ArrayList<>();
        ArrayList<ClassroomRole> classroomRoles = user.getRoleInClassrooms();
        for(ClassroomRole c : classroomRoles){
            classrooms.add(c.getClassroom());
        }
        res.put("status",true);
        res.put("obj", classrooms);
        res.put("msg","successfully fetched classrooms");
        return res;
    }


    private static String encryptString(String input) throws NoSuchAlgorithmException {
        String saltedInput = "DrProject" + input;
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] md5Bytes = md.digest(saltedInput.getBytes());
        return Base64.getEncoder().encodeToString(md5Bytes);
    }



}

