package com.vedant.urlshortener.service;

import com.vedant.urlshortener.dto.LoginRequest;
import com.vedant.urlshortener.dto.RegisterRequest;
import com.vedant.urlshortener.model.User;
import com.vedant.urlshortener.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String registerUser(RegisterRequest request) {
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        return "User registered successfully";
    }

    public String loginUser(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);

        if (user == null) {
            return "Email not found";
        }

        if (!user.getPassword().equals(request.getPassword())) {
            return "Incorrect password";
        }

        return "Login successful";
    }
}
