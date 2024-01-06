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
        Map<String, Object> res = new HashMap<>();
        if (userService.signUpUser(user) == true){    ///////method to signup user which returns a msg json
            res.put("status", true);
            res.put("msg", "Signed up successfully!");
            return ResponseEntity.ok(res);
        }
        res.put("status", false);
        res.put("msg", "Sigin up failed!");
        return ResponseEntity.ok(res);
    }

    @PostMapping("/login")
    public ResponseEntity<?> signUpUser(@RequestBody Map<String, Object> credentials, HttpSession session) {
        Map<String, Object> res = new HashMap<>();
        /////////////checkCredentials method needed which returns a json with found and role and username attribute
        Map<String, Object> user = userService.checkCredentials(credentials.get("username"), credentials.get("password"));
        if (user.get("found") == true){
            session.setAttribute("username", credentials.get("username"));
            session.setAttribute("role", user.get("role"));
            res.put("status", true);
            res.put("msg", "Signed in! Redirecting...");
            return ResponseEntity.ok(res);
        }
        res.put("status", false);
        res.put("msg", "Username or Password is incorrect!");
        return ResponseEntity.ok(res);
    }

}

