package com.example.jobportal.repository;

import com.example.jobportal.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface JobRepository extends JpaRepository<Job, Long> {
    // Explicit delete by employer ID
    @Transactional
    @Modifying
    @Query("DELETE FROM Job j WHERE j.employer.id = :employerId")
    void deleteByEmployerId(@Param("employerId") Long employerId);
}