package com.drproject.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StaticController {

    @GetMapping(value = {"/", ""})
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup.html";
    }
    @GetMapping("/login")
    public String login(HttpSession session) {
        if (session.getAttribute("username") != null) { return "redirect:/panel"; }
        return "login.html";
    }

    @GetMapping("/panel")
    public String panel(HttpSession session) {
        if (session.getAttribute("username") != null){
            return "panel.html";                                     //////panel.html missing
        }
        return "redirect:/login?msg=Sign in first!";
    }

    @GetMapping("/signout")
    public String logout(HttpSession session) {
        // Invalidate the session
        session.invalidate();
        return "redirect:/login";
    }
}

