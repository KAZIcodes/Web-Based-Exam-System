package com.drproject.controllers;

import com.drproject.entity.User;
import com.drproject.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signUpUser(@RequestBody User user) {
        User savedUser = userService.signUpUser(user);
        return ResponseEntity.ok(savedUser);
    }

}

