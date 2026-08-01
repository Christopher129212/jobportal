package com.example.jobportal.controller;

import com.example.jobportal.model.Application;
import com.example.jobportal.model.Job;
import com.example.jobportal.model.User;
import com.example.jobportal.service.ApplicationService;
import com.example.jobportal.service.EmailService;
import com.example.jobportal.service.FileUploadService;
import com.example.jobportal.service.JobService;
import com.example.jobportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.Map;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "*")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private JobService jobService;

    @Autowired
    private UserService userService;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private EmailService emailService;

    // ===== TEST EMAIL =====
    @GetMapping("/test-email")
    public String testEmail() {
        emailService.sendSimpleEmail("YOUR_GMAIL_ADDRESS@gmail.com", "Test Subject", "Hello from JobPortal!");
        return "Test email sent! Check your inbox.";
    }

    // ===== APPLY =====
    @PostMapping("/apply")
    public ResponseEntity<?> apply(
            @RequestParam Long jobId,
            @RequestParam Long jobSeekerId,
            @RequestParam(required = false) String coverLetter,
            @RequestParam(required = false) MultipartFile resume) {

        Job job = jobService.getJobById(jobId);
        User jobSeeker = userService.findById(jobSeekerId).orElse(null);

        if (job == null || jobSeeker == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid job or user"));
        }

        String resumePath = null;
        if (resume != null && !resume.isEmpty()) {
            try {
                resumePath = fileUploadService.uploadResume(resume);
            } catch (IOException e) {
                return ResponseEntity.badRequest().body(Map.of("error", "Failed to upload resume: " + e.getMessage()));
            }
        }

        Application app = applicationService.applyForJob(job, jobSeeker, coverLetter, resumePath);

        // Email to seeker (confirmation)
        try {
            emailService.sendApplicationConfirmationToSeeker(
                    jobSeeker.getEmail(),
                    job.getTitle(),
                    job.getCompany()
            );
        } catch (Exception e) {
            System.err.println("Seeker confirmation failed: " + e.getMessage());
        }

        // Email to employer (notification)
        try {
            emailService.sendApplicationNotification(
                    job.getEmployer().getEmail(),
                    job.getTitle(),
                    jobSeeker.getFullName(),
                    coverLetter != null ? coverLetter : "No cover letter provided"
            );
        } catch (Exception e) {
            System.err.println("Employer notification failed: " + e.getMessage());
        }

        return ResponseEntity.ok(app);
    }

    // ===== GET MY APPLICATIONS =====
    @GetMapping("/my-applications/{jobSeekerId}")
    public ResponseEntity<?> getMyApplications(@PathVariable Long jobSeekerId) {
        return ResponseEntity.ok(applicationService.getApplicationsForJobSeeker(jobSeekerId));
    }

    // ===== GET JOB APPLICATIONS =====
    @GetMapping("/job-applications/{jobId}")
    public ResponseEntity<?> getJobApplications(@PathVariable Long jobId) {
        return ResponseEntity.ok(applicationService.getApplicationsForJob(jobId));
    }

    // ===== UPDATE STATUS =====
    @PutMapping("/{applicationId}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long applicationId,
                                          @RequestParam String status) {

        Application app = applicationService.getApplicationById(applicationId);
        if (app == null) {
            return ResponseEntity.notFound().build();
        }
        app.setStatus(status);
        applicationService.saveApplication(app);

        try {
            String seekerEmail = app.getJobSeeker().getEmail();
            String jobTitle = app.getJob().getTitle();
            String companyName = app.getJob().getCompany();

            if ("ACCEPTED".equalsIgnoreCase(status)) {
                emailService.sendApplicationAcceptedEmail(seekerEmail, jobTitle, companyName);
            } else if ("REJECTED".equalsIgnoreCase(status)) {
                emailService.sendApplicationRejectedEmail(seekerEmail, jobTitle, companyName);
            }
        } catch (Exception e) {
            System.err.println("Status email failed: " + e.getMessage());
        }

        return ResponseEntity.ok(Map.of("message", "Status updated to " + status));
    }

    // ===== DOWNLOAD RESUME =====
    @GetMapping("/download-resume/{filename}")
    public ResponseEntity<Resource> downloadResume(@PathVariable String filename) {
        try {
            Path filePath = fileUploadService.getResumePath(filename);
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}