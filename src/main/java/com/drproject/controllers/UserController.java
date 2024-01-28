package com.drproject.controllers;

import com.drproject.entity.User;
import com.drproject.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUpUser(@RequestBody User user) {
        Map<String, Object> res = userService.signUpUser(user);   /////signUp function which returns JSON(map) contains status and msg
        return ResponseEntity.ok(res);
    }

    @PostMapping("/login")
    public ResponseEntity<?> signUpUser(@RequestBody Map<String, Object> credentials, HttpSession session) {
        Map<String, Object> res = new HashMap<>();
        /////////////checkCredentials method needed which returns a json with found and role and username attribute
        Map<String, Object> user = userService.checkCredentials(credentials.get("email"), credentials.get("password"));
        if (user.get("found").equals(true)){
            session.setAttribute("username", user.get("username"));
            res.put("status", true);
            res.put("msg", "Signed in! Redirecting...");
            return ResponseEntity.ok(res);
        }
        res.put("status", false);
        res.put("msg", "Username or Password is incorrect!");
        return ResponseEntity.ok(res);
    }

    //returns user profile info
    @GetMapping("/data")
    public ResponseEntity<?> userData(HttpSession session) {
        if (session.getAttribute("username") == null){
            Map<String, Object> error = new HashMap<>();
            error.put("status", false);
            error.put("msg", "Sign in first!");
            return ResponseEntity.ok(error);
        }

        /////////////getUserInfo to return user data(info) or null if not found
        Map<String, Object> res = userService.getUserInfo(session.getAttribute("username"));
        if (res.get("status").equals(true)){
            Map<String, Object> user = new HashMap<>();
            user.put("username", ((User)res.get("obj")).getUsername());
            user.put("firstName", ((User)res.get("obj")).getFirstName());
            user.put("lastName", ((User)res.get("obj")).getLastName());
            user.put("email", ((User)res.get("obj")).getEmail());
            user.put("bio", ((User)res.get("obj")).getBio());
            return ResponseEntity.ok(user);
        }
        else {
            res.put("status", false);
            res.put("msg", "User data not found!");
            return ResponseEntity.ok(res);
        }
    }



    //returns the classrooms of a users
    @GetMapping("/classrooms")
    public ResponseEntity<?> userData(HttpSession session) {
        if (session.getAttribute("username") == null){
            Map<String, Object> error = new HashMap<>();
            error.put("status", false);
            error.put("msg", "Sign in first!");
            return ResponseEntity.ok(error);
        }

        /////////////getUserClassrooms to return user classrooms or empty user it does not have a classroom
        Map<String, Object> res = userService.getUserClassrooms(session.getAttribute("username"));
        return ResponseEntity.ok(res);
    }


    @GetMapping("/getProfile")
    public ResponseEntity<?> getProfileLink(HttpSession session) throws NoSuchAlgorithmException {
        Map<String, Object> res = new HashMap<>();
        if (session.getAttribute("username") == null){
            res.put("status", false);
            res.put("msg", "Sign in first!");
            return ResponseEntity.ok(res);
        }


        String link = "/profileImage/" + encryptProfileId("drproject" + session.getAttribute("username") + "drproject");
        res.put("status", true);
        res.put("imageLink", link);
        return ResponseEntity.ok(res);
    }

    @PatchMapping("/updateProfile")
    public ResponseEntity<?> updateProfile(@RequestBody Map<String, Object> updateInfo, HttpSession session) {
        Map<String, Object> res = new HashMap<>();
        if (session.getAttribute("username") == null){
            res.put("status", false);
            res.put("msg", "Sign in first!");
            return ResponseEntity.ok(res);
        }
        ////////// updateProfileInfo function that takes the username of the user thatwants to update the profile and a map named updateInfo(checks the 4 values for not being empty) and returns the status/msg/obj
        res = userService.updateProfileInfo(updateInfo, session.getAttribute("username"));
        return ResponseEntity.ok(res);
    }
    @PatchMapping("/changePass")
    public ResponseEntity<?> updateProfile(@RequestBody Map<String, Object> updateInfo, HttpSession session) {
        Map<String, Object> res = new HashMap<>();
        if (session.getAttribute("username") == null){
            res.put("status", false);
            res.put("msg", "Sign in first!");
            return ResponseEntity.ok(res);
        }
        ////////// changepass function that takes old and new pass and username which first checks old and if correct changes to new pass
        res = userService.changePassword(updateInfo.get("newPass"), updateInfo.get("oldPass"), session.getAttribute("username"));
        return ResponseEntity.ok(res);
    }

    public static String encryptProfileId(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] md5Bytes = md.digest(input.getBytes());
        return Base64.getEncoder().encodeToString(md5Bytes);
    }





    /*@GetMapping("/isLogedIn")
    public ResponseEntity<?> login(HttpSession session) {
        Map<String, Object> res = new HashMap<>();
        res.put("status", false);
        if (session.getAttribute("username") != null) {
            res.put("status", true);
        }
        return ResponseEntity.ok(res);
    }*/

}

