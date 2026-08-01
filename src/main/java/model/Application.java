package com.example.jobportal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "applications", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"job_id", "job_seeker_id"})
})
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "job_id")
    @JsonIgnore
    private Job job;

    @ManyToOne
    @JoinColumn(name = "job_seeker_id")
    @JsonIgnore
    private User jobSeeker;

    private LocalDateTime appliedDate = LocalDateTime.now();
    private String status;

    @Column(columnDefinition = "TEXT")
    private String coverLetter;

    private String resumePath;

    public Application() {}

    public Application(Job job, User jobSeeker, String coverLetter) {
        this.job = job;
        this.jobSeeker = jobSeeker;
        this.coverLetter = coverLetter;
        this.status = "PENDING";
    }

    // ----- Getters & Setters -----
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Job getJob() { return job; }
    public void setJob(Job job) { this.job = job; }

    public User getJobSeeker() { return jobSeeker; }
    public void setJobSeeker(User jobSeeker) { this.jobSeeker = jobSeeker; }

    public LocalDateTime getAppliedDate() { return appliedDate; }
    public void setAppliedDate(LocalDateTime appliedDate) { this.appliedDate = appliedDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCoverLetter() { return coverLetter; }
    public void setCoverLetter(String coverLetter) { this.coverLetter = coverLetter; }

    public String getResumePath() { return resumePath; }
    public void setResumePath(String resumePath) { this.resumePath = resumePath; }
}