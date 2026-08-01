package com.example.jobportal.controller;

import com.example.jobportal.model.User;
import com.example.jobportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email already exists"));
        }
        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Cannot register as admin"));
        }
        User saved = userService.registerUser(user);
        return ResponseEntity.ok(Map.of("message", "User registered", "userId", saved.getId()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");
        User user = userService.findByEmail(email).orElse(null);
        if (user == null || !userService.checkPassword(password, user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Login successful");
        response.put("userId", user.getId());
        response.put("role", user.getRole());
        response.put("fullName", user.getFullName());
        return ResponseEntity.ok(response);
    }
}