package com.example.jobportal.repository;

import com.example.jobportal.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
    // Delete all jobs posted by a specific employer
    void deleteByEmployerId(Long employerId);
}