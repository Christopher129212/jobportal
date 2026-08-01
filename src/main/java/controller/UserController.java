package com.example.jobportal.controller;

import com.example.jobportal.model.User;
import com.example.jobportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    // ----- GET USER PROFILE -----
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserProfile(@PathVariable Long id) {
        User user = userService.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        // Don't send password back
        user.setPassword(null);
        return ResponseEntity.ok(user);
    }

    // ----- UPDATE USER PROFILE -----
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfile(@PathVariable Long id,
                                           @RequestBody Map<String, String> updates) {
        String fullName = updates.get("fullName");
        String password = updates.get("password");
        User updated = userService.updateProfile(id, fullName, password);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        updated.setPassword(null); // Don't send password back
        return ResponseEntity.ok(updated);
    }
}