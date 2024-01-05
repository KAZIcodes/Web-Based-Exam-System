package com.drproject.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StaticController {

    @GetMapping("/signup")
    public String signup() {
        return "signup.html"; // Returns a view name (e.g., hello.html)
    }
}

