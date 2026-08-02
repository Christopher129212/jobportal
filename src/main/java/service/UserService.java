package com.example.jobportal.service;

import com.example.jobportal.model.User;
import com.example.jobportal.repository.ApplicationRepository;
import com.example.jobportal.repository.JobRepository;
import com.example.jobportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ----- DELETE USER WITH ASSOCIATIONS -----
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return;

        // 1. Delete all applications made by this user (as job seeker)
        applicationRepository.deleteAll(user.getApplications());

        // 2. Delete all jobs posted by this user (as employer)
        jobRepository.deleteAll(user.getJobs());

        // 3. Finally delete the user itself
        userRepository.delete(user);
    }

    public User updateProfile(Long userId, String fullName, String password) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return null;

        if (fullName != null && !fullName.trim().isEmpty()) {
            user.setFullName(fullName.trim());
        }
        if (password != null && !password.trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(password.trim()));
        }
        return userRepository.save(user);
    }
}