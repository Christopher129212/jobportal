package com.example.jobportal.controller;

import com.example.jobportal.model.Application;
import com.example.jobportal.model.Category;
import com.example.jobportal.model.Job;
import com.example.jobportal.model.User;
import com.example.jobportal.service.ApplicationService;
import com.example.jobportal.service.CategoryService;
import com.example.jobportal.service.JobService;
import com.example.jobportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private JobService jobService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "User deleted"));
    }

    @GetMapping("/jobs")
    public List<Job> getAllJobs() {
        return jobService.getAllJobs();
    }

    @DeleteMapping("/jobs/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.ok(Map.of("message", "Job deleted"));
    }

    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @PostMapping("/categories")
    public ResponseEntity<?> addCategory(@RequestBody Category category) {
        if (categoryService.findByName(category.getName()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Category already exists"));
        }
        return ResponseEntity.ok(categoryService.saveCategory(category));
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        List<Job> jobs = jobService.getJobsByCategory(id);
        if (!jobs.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Category in use by " + jobs.size() + " jobs"));
        }
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(Map.of("message", "Category deleted"));
    }

    @GetMapping("/applications")
    public List<Application> getAllApplications() {
        return applicationService.getAllApplications();
    }

    @GetMapping("/stats/jobs-by-category")
    public Map<String, Long> getJobsByCategory() {
        return jobService.getAllJobs().stream()
                .collect(Collectors.groupingBy(job -> job.getCategory().getName(), Collectors.counting()));
    }

    @GetMapping("/stats/applications-by-status")
    public Map<String, Long> getApplicationsByStatus() {
        return applicationService.getAllApplications().stream()
                .collect(Collectors.groupingBy(Application::getStatus, Collectors.counting()));
    }
}