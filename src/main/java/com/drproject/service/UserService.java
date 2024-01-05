package com.drproject.service;

import com.drproject.entity.User;
import com.drproject.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User signUpUser(User user) {
        // Additional validation, hashing of password, etc., can be done here
        return userRepository.save(user); // Save the user to the database
    }
}

