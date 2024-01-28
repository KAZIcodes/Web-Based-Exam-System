package com.drproject.controllers;

import com.drproject.entity.User;
import com.drproject.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
        Map<String, Object> user = userService.checkCredentials(credentials.get("username"), credentials.get("password"));
        if (user.get("found") == true){
            session.setAttribute("username", credentials.get("username"));
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

        Map<String, Object> res = new HashMap<>();
        /////////////getUserInfo to return user data(info) or null if not found
        Map<String, Object> user = userService.getUserInfo(session.getAttribute("username"));
        if (user != null){
            ///maybe some code to get just needed info from user and put it in a JSON to send
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
        Map<String, Object> classrooms = userService.getUserClassrooms(session.getAttribute("username"));
        if (classrooms != null){
            ///maybe some code to get just needed info from user and put it in a JSON to send
            return ResponseEntity.ok(classrooms);
        }
        else {
            Map<String, Object> res = new HashMap<>();
            res.put("status", false);
            res.put("msg", "User data not found!");
            return ResponseEntity.ok(res);
        }
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

