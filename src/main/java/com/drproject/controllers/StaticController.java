package com.drproject.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
public class StaticController {
    private final ARservice ARserivce;
    private final ClassroomService classroomService;

    public StaticController(ARservice ARserivce, ClassroomService classroomService) {   /////user service class needed
        this.ARserivce = ARserivce;
        this.classroomService = classroomService;
    }


    @GetMapping(value = {"/", ""})
    public String home() {
        return "html/homepage.html";
    }

    @GetMapping("/signup")
    public String signup() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(HttpSession session) {
        if (session.getAttribute("username") != null) {
            return "redirect:/panel";
        }
        return "html/login_signUp.html";
    }

    @GetMapping("/FAQ")
    public String faq() {
        return "html/faq.html";
    }
    @GetMapping("/aboutUs")
    public String aboutUs() {
        return "html/aboutUs.html";
    }
    @GetMapping("/contact")
    public String contact() {
        return "html/askQ.html";
    }

    @GetMapping("/panel")
    public String panel(HttpSession session) {
        if (session.getAttribute("username") != null){
            return "html/memberPanel.html";                                     //////panel.html missing
        }
        return "redirect:/login?msg=Sign in first!";
    }


    @GetMapping("/signout")
    public String logout(HttpSession session) {
        // Invalidate the session
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/classrooms/{classroomId}")
    public String getClassroom(@PathVariable String classroomId, HttpSession session) {
        if (session.getAttribute("username") == null){
            return "redirect:/login??msg=Sign in first!";
        }
        else {
            //gerUserRole method that takes username and classroomID and returns the role of the user in string format(teacher or student)
            String role = classroomService.getUserRole(session.getAttribute("username"), classroomId);
            if (role.equals("teacher"))
                return "html/teacherClassroom.html";
        }
        return "html/StudentClassroom.html";  ////////classroom html needed and then JS should get the data based on the classroomId
    }

    //returns quiz or poll pr assignment templates and has AR= param
    @GetMapping("/{classroomId}/sections/{sectionId}")
    public String getAR(@RequestParam("AR") String ARid, @PathVariable String classroomId) {
        AR ar = ARservice.getARdata(ARid);   /////AR service class needed and getARdata method needed and AR entity
        if (ar.getType() == "quiz"){
            return "quiz.html";
        }
        else if (ar.getType() == "assigenment"){    ////getType method needed for ARs
            return "assigenment.html";
        }
        else if (ar.getType() == "poll"){    ////getType method needed for ARs
            return "poll.html";
        }

        return "redirect:/classrooms/" + classroomId;  ////////quiz, poll, assignement, glossary html needed and then JS should get the data based on the ArID
    }

    @GetMapping("/panel/notifications")
    public String getNotifs(HttpSession session) {
        if (session.getAttribute("username") == null){
            return "redirect:/login?msg=Sign in first!";
        }

        return "html/notification.html";
    }

    @GetMapping("/panel/profile")
    public String getProfile(HttpSession session) {
        if (session.getAttribute("username") == null){
            return "redirect:/login?msg=Sign in first!";
        }

        return "html/profile.html";
    }
}

