package com.example.jobportal.service;

import com.example.jobportal.model.Job;
import com.example.jobportal.model.User;
import com.example.jobportal.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    // ----- POST JOB -----
    public Job postJob(Job job, User employer) {
        job.setEmployer(employer);
        return jobRepository.save(job);
    }

    // ----- GET ALL JOBS -----
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    // ----- GET JOB BY ID -----
    public Job getJobById(Long id) {
        return jobRepository.findById(id).orElse(null);
    }

    // ----- DELETE JOB -----
    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }

    // ----- UPDATE JOB -----
    public Job updateJob(Job updatedJob) {
        Job existing = jobRepository.findById(updatedJob.getId()).orElse(null);
        if (existing == null) return null;
        existing.setTitle(updatedJob.getTitle());
        existing.setDescription(updatedJob.getDescription());
        existing.setCompany(updatedJob.getCompany());
        existing.setLocation(updatedJob.getLocation());
        existing.setSalary(updatedJob.getSalary());
        existing.setImageUrl(updatedJob.getImageUrl());
        existing.setCategory(updatedJob.getCategory());
        return jobRepository.save(existing);
    }

    // ----- SEARCH JOBS -----
    public List<Job> searchJobs(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return jobRepository.findAll();
        }
        String lower = keyword.toLowerCase();
        return jobRepository.findAll().stream()
                .filter(job ->
                        job.getTitle().toLowerCase().contains(lower) ||
                                job.getCompany().toLowerCase().contains(lower) ||
                                (job.getDescription() != null && job.getDescription().toLowerCase().contains(lower))
                )
                .collect(Collectors.toList());
    }

    // ----- GET JOBS BY CATEGORY -----
    public List<Job> getJobsByCategory(Long categoryId) {
        if (categoryId == null) return jobRepository.findAll();
        return jobRepository.findAll().stream()
                .filter(job -> job.getCategory() != null && job.getCategory().getId().equals(categoryId))
                .collect(Collectors.toList());
    }

    // ----- SEARCH BY KEYWORD AND CATEGORY -----
    public List<Job> searchJobsByKeywordAndCategory(String keyword, Long categoryId) {
        return jobRepository.findAll().stream()
                .filter(job -> {
                    boolean matchKeyword = true;
                    if (keyword != null && !keyword.trim().isEmpty()) {
                        String lower = keyword.toLowerCase();
                        matchKeyword =
                                job.getTitle().toLowerCase().contains(lower) ||
                                        job.getCompany().toLowerCase().contains(lower) ||
                                        (job.getDescription() != null && job.getDescription().toLowerCase().contains(lower));
                    }
                    boolean matchCategory = true;
                    if (categoryId != null) {
                        matchCategory = job.getCategory() != null && job.getCategory().getId().equals(categoryId);
                    }
                    return matchKeyword && matchCategory;
                })
                .collect(Collectors.toList());
    }

    // ----- PAGINATION -----
    public Page<Job> getAllJobsPaginated(Pageable pageable) {
        List<Job> allJobs = jobRepository.findAll();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allJobs.size());
        List<Job> pageContent = allJobs.subList(start, end);
        return new PageImpl<>(pageContent, pageable, allJobs.size());
    }

    // ----- PAGINATED SEARCH -----
    public Page<Job> searchJobsPaginated(String keyword, Pageable pageable) {
        List<Job> filtered = searchJobs(keyword);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filtered.size());
        List<Job> pageContent = filtered.subList(start, end);
        return new PageImpl<>(pageContent, pageable, filtered.size());
    }

    // ----- PAGINATED BY CATEGORY -----
    public Page<Job> getJobsByCategoryPaginated(Long categoryId, Pageable pageable) {
        List<Job> filtered = getJobsByCategory(categoryId);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filtered.size());
        List<Job> pageContent = filtered.subList(start, end);
        return new PageImpl<>(pageContent, pageable, filtered.size());
    }
}