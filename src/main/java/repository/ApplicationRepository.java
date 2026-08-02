package com.example.jobportal.repository;

import com.example.jobportal.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByJobSeekerId(Long jobSeekerId);
    List<Application> findByJobId(Long jobId);

    // Explicit delete by job seeker ID (works guaranteed)
    @Transactional
    @Modifying
    @Query("DELETE FROM Application a WHERE a.jobSeeker.id = :jobSeekerId")
    void deleteByJobSeekerId(@Param("jobSeekerId") Long jobSeekerId);
}