package com.example.jobportal.controller;

import com.example.jobportal.model.Job;
import com.example.jobportal.model.User;
import com.example.jobportal.model.Category;
import com.example.jobportal.service.JobService;
import com.example.jobportal.service.UserService;
import com.example.jobportal.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;   // <-- FIX: added import

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "*")
public class JobController {

    @Autowired
    private JobService jobService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    // ----- POST JOB -----
    @PostMapping("/post")
    public ResponseEntity<?> postJob(@RequestBody Job job,
                                     @RequestParam Long employerId,
                                     @RequestParam Long categoryId) {
        User employer = userService.findById(employerId).orElse(null);
        if (employer == null || !"EMPLOYER".equals(employer.getRole())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid employer"));
        }
        Category category = categoryService.getCategoryById(categoryId).orElse(null);
        if (category == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid category"));
        }
        job.setEmployer(employer);
        job.setCategory(category);
        Job saved = jobService.postJob(job, employer);
        return ResponseEntity.ok(saved);
    }

    // ----- GET ALL JOBS (PAGINATED) -----
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Job> jobPage = jobService.getAllJobsPaginated(pageable);
        Map<String, Object> response = Map.of(
                "jobs", jobPage.getContent(),
                "totalPages", jobPage.getTotalPages(),
                "totalElements", jobPage.getTotalElements(),
                "currentPage", jobPage.getNumber(),
                "size", jobPage.getSize()
        );
        return ResponseEntity.ok(response);
    }

    // ----- GET JOB BY ID -----
    @GetMapping("/{id}")
    public ResponseEntity<?> getJobById(@PathVariable Long id) {
        Job job = jobService.getJobById(id);
        if (job == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(job);
    }

    // ----- UPDATE JOB -----
    @PutMapping("/{id}")
    public ResponseEntity<?> updateJob(@PathVariable Long id,
                                       @RequestBody Job job,
                                       @RequestParam Long categoryId) {
        Job existing = jobService.getJobById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        Category category = categoryService.getCategoryById(categoryId).orElse(null);
        if (category == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid category"));
        }
        job.setId(id);
        job.setCategory(category);
        job.setEmployer(existing.getEmployer()); // keep original employer
        Job updated = jobService.updateJob(job);
        return ResponseEntity.ok(updated);
    }

    // ----- DELETE JOB -----
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable Long id) {
        Job job = jobService.getJobById(id);
        if (job == null) {
            return ResponseEntity.notFound().build();
        }
        jobService.deleteJob(id);
        return ResponseEntity.ok(Map.of("message", "Job deleted successfully"));
    }

    // ----- SEARCH WITH PAGINATION & CATEGORY -----
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Job> jobPage;

        if (categoryId != null && (keyword == null || keyword.isEmpty())) {
            // search by category only
            jobPage = jobService.getJobsByCategoryPaginated(categoryId, pageable);
        } else if (categoryId != null && keyword != null && !keyword.isEmpty()) {
            // search by keyword and category (filter after search)
            Page<Job> searchPage = jobService.searchJobsPaginated(keyword, pageable);
            List<Job> filtered = searchPage.getContent().stream()
                    .filter(job -> job.getCategory() != null && job.getCategory().getId().equals(categoryId))
                    .collect(Collectors.toList());
            jobPage = new PageImpl<>(filtered, pageable, filtered.size());
        } else {
            // search by keyword only
            jobPage = jobService.searchJobsPaginated(keyword, pageable);
        }

        Map<String, Object> response = Map.of(
                "jobs", jobPage.getContent(),
                "totalPages", jobPage.getTotalPages(),
                "totalElements", jobPage.getTotalElements(),
                "currentPage", jobPage.getNumber(),
                "size", jobPage.getSize()
        );
        return ResponseEntity.ok(response);
    }
}