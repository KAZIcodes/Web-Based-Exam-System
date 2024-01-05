package com.drproject.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<?> handleError() {
        // Handle 404 errors here
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("<h1>404 NOT FOUND !</h1>");
    }
}

