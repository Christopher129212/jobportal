package com.example.jobportal.service;

import com.example.jobportal.model.User;
import com.example.jobportal.repository.ApplicationRepository;
import com.example.jobportal.repository.JobRepository;
import com.example.jobportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    // ----- REGISTER -----
    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // ----- FIND BY EMAIL -----
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // ----- FIND BY ID -----
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    // ----- CHECK PASSWORD -----
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    // ----- GET ALL USERS (for Admin) -----
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ----- DELETE USER WITH CASCADE -----
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return;

        // Delete all applications where this user is the job seeker
        applicationRepository.deleteByJobSeekerId(id);

        // Delete all jobs where this user is the employer
        jobRepository.deleteByEmployerId(id);

        // Now delete the user itself
        userRepository.delete(user);
    }

    // ----- UPDATE PROFILE -----
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