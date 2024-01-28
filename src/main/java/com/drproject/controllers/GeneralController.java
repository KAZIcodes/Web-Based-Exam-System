package com.drproject.controllers;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import com.drproject.entity.User;
import com.drproject.service.UserService;
import jakarta.persistence.Entity;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
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
    public ResponseEntity<?> signUpUser(@RequestBody Contact contact) {
        Map<String, Object> res = generalService.saveContactForm(contct);   /////a method to save the contact in the data base and returns a status and msg
        return ResponseEntity.ok(res);
    }

    @GetMapping("/profiles/{profileId}")
    public ResponseEntity<Resource> getImage(@PathVariable String profileId) throws IOException {
        try {
            // Specify the directory where your images are stored
            Path imageDirectory = Paths.get("static/profiles");

            // Construct the full path to the requested image
            Path imagePath = imageDirectory.resolve(profileId);

            // Create a FileSystemResource based on the image path
            FileSystemResource fileSystemResource = new FileSystemResource(imagePath);

            // Check if the file exists
            if (fileSystemResource.exists()) {
                // If the file exists, return it as ResponseEntity
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG) // You can set the appropriate media type based on your image format
                        .body(fileSystemResource);
            } else {
                // If the file does not exist, return a default profile
                Path directory = Paths.get("static/Pics");
                String imageName = "graduate-student-cartoon-avatar-on-white-background-elements-graduating-student-illustration-people-illustration-vector.jpeg";
                Path fullpath = imageDirectory.resolve(imageName);
                FileSystemResource defaultFile = new FileSystemResource(imagePath);
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG) // You can set the appropriate media type based on your image format
                        .body(defaultFile);
            }
        } catch (Exception e) {
            // Handle exceptions (e.g., IOException) and return an appropriate response
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/profiles/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file, HttpSession session) {
        Map<String, Object> res = new HashMap<>();

        if (file.isEmpty()) {
            res.put("status", false);
            res.put("msg", "Empty fil sent!");
            return ResponseEntity.ok(res);
        }

        try {
            // Get the original filename
            String fileName = UserController.encryptProfileId("drproject" + session.getAttribute("username") + "drproject");

            // Construct the full path for the uploaded image
            Path imagePath = Paths.get("static/profiles", fileName);

            // Save the file to the specified path
            file.transferTo(imagePath.toFile());

            // You can do additional processing or store the file path in the database if needed
            res.put("status", true);
            res.put("msg", "DONE successfully!");
            return ResponseEntity.ok(res);
        } catch (IOException e) {
            res.put("status", false);
            res.put("msg", "Unexpected error!");
            return ResponseEntity.ok(res);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}




