package com.drproject.controllers;

import com.drproject.entity.User;
import com.drproject.service.UserService;
import jakarta.persistence.Entity;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class GeneralController {
    private final GeneralService generalService;

    public GeneralController(GeneralService generalService) {
        this.generalService = generalService;
    }

    @GetMapping("/contact")
    public ResponseEntity<?> signUpUser(@Entity Contct contct) {
        Map<String, Object> res = generalService.saveContactForm(contct);   /////a method to save the contact in the data base and returns a status and msg
        return ResponseEntity.ok(res);
    }


}

