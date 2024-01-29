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
import java.util.List;

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
            return res;
        }
        try {
            user.setPassword(encryptString(user.getPassword()));
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            res.put("status", false);
            res.put("msg", "username does not exist");
            res.put("obj",null);
            return res;
        }

        userRepository.save(user);
        return res;
    }
    public HashMap<String , Object> checkCredentials(String email, String password){
        HashMap<String, Object> res = new HashMap<>();
        if(userRepository.existsByEmail(email)){
            System.out.println("\n\n\n\n\n\n\n\n\n\nkossher1\n\n\n\n\n\n");
            User user = userRepository.getUserByEmail(email);
            System.out.println("\n\n\n\n\n\n\n\n\n\nkossher2\n\n\n\n\n\n");
            try {

                if (user.getPassword().equals(encryptString(password))){
                    res.put("found",true);
                    res.put("username",user.getUsername());
                    return res;
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
        List<ClassroomRole> classroomRoles = user.getRoleInClassrooms();
        for(ClassroomRole c : classroomRoles){
            classrooms.add(c.getClassroom());
            System.out.println("\n\n\n\n\n\n\n\n\n\n" + c.getClassroom().getName()+"!!!!!!!!!!!!!!!");
        }
        res.put("status",true);
        res.put("obj", classrooms);
        res.put("msg","successfully fetched classrooms");
        return res;
    }


    public HashMap<String, Object> updateProfileInfo(HashMap<String, Object> newInfo, String username){
        HashMap<String, Object> res = new HashMap<>();
        User user = userRepository.getUserByUsername(username);
        if(!newInfo.get("bio").equals("")){
            user.setBio((String)newInfo.get("bio"));
            res.put("msg", "profile Updated");
        }
        if(!newInfo.get("firstName").equals("")){
            user.setFirstName((String)newInfo.get("firstName"));
            res.put("msg", "profile Updated");
        }
        if(!newInfo.get("lastName").equals("")){
            user.setLastName((String)newInfo.get("lastName"));
            res.put("msg", "profile Updated");
        }
        if(!newInfo.get("email").equals("")){
            user.setEmail((String)newInfo.get("email"));
            res.put("msg", "profile Updated");
        }
        userRepository.save(user);
        res.put("status", true);
        res.put("obj",null);
        return res;
    }

    public HashMap<String, Object> changePassword(String newPassword, String oldPassword, String username){
        HashMap<String, Object> res = new HashMap<>();
        User user = userRepository.getUserByUsername(username);
        try {
            if (encryptString(oldPassword).equals(user.getPassword())){
                user.setPassword(encryptString(newPassword));
                userRepository.save(user);
                res.put("status", true);
                res.put("obj", null);
                res.put("msg", "password changed successfully");
                return res;
            }
            else {
                res.put("status", false);
                res.put("obj", null);
                res.put("msg", "incorrect password");
                return res;
            }
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException){
            res.put("status", false);
            res.put("obj", null);
            res.put("msg", "no such algorithm exception(md5 hash failed)");
            return res;
        }
    }


    private static String encryptString(String input) throws NoSuchAlgorithmException {
        String saltedInput = "DrProject" + input;
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] md5Bytes = md.digest(saltedInput.getBytes());
        return Base64.getEncoder().encodeToString(md5Bytes);
    }



}

