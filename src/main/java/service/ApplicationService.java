package com.example.jobportal.service;

import com.example.jobportal.model.Application;
import com.example.jobportal.model.Job;
import com.example.jobportal.model.User;
import com.example.jobportal.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    public Application applyForJob(Job job, User jobSeeker, String coverLetter, String resumePath) {
        Application app = new Application(job, jobSeeker, coverLetter);
        app.setResumePath(resumePath);
        return applicationRepository.save(app);
    }

    public List<Application> getApplicationsForJobSeeker(Long jobSeekerId) {
        return applicationRepository.findByJobSeekerId(jobSeekerId);
    }

    public List<Application> getApplicationsForJob(Long jobId) {
        return applicationRepository.findByJobId(jobId);
    }

    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    public Application getApplicationById(Long id) {
        return applicationRepository.findById(id).orElse(null);
    }

    public void saveApplication(Application app) {
        applicationRepository.save(app);
    }
}