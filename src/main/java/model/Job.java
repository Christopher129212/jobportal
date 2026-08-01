package com.example.jobportal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String company;
    private String location;
    private String salary;
    private String imageUrl;
    private LocalDateTime postedDate = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "employer_id")
    @JsonIgnore   // Keep this to avoid serializing the full employer object
    private User employer;

    @ManyToOne
    @JoinColumn(name = "category_id")
    // @JsonIgnore  REMOVE THIS – we want to include category in JSON
    private Category category;

    public Job() {}

    public Job(String title, String description, String company, String location,
               String salary, String imageUrl, User employer, Category category) {
        this.title = title;
        this.description = description;
        this.company = company;
        this.location = location;
        this.salary = salary;
        this.imageUrl = imageUrl;
        this.employer = employer;
        this.category = category;
    }

    // ----- Getters & Setters -----
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getSalary() { return salary; }
    public void setSalary(String salary) { this.salary = salary; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public LocalDateTime getPostedDate() { return postedDate; }
    public void setPostedDate(LocalDateTime postedDate) { this.postedDate = postedDate; }

    public User getEmployer() { return employer; }
    public void setEmployer(User employer) { this.employer = employer; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
}