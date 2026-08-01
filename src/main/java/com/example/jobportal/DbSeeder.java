package com.example.jobportal;

import com.example.jobportal.model.Category;
import com.example.jobportal.model.Job;
import com.example.jobportal.model.User;
import com.example.jobportal.repository.ApplicationRepository;
import com.example.jobportal.repository.JobRepository;
import com.example.jobportal.service.CategoryService;
import com.example.jobportal.service.JobService;
import com.example.jobportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class DbSeeder implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private JobService jobService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Override
    public void run(String... args) throws Exception {
        // ----- 1. Delete existing data -----
        System.out.println("🗑️ Clearing all applications...");
        applicationRepository.deleteAll();

        System.out.println("🗑️ Clearing all jobs...");
        jobRepository.deleteAll();

        // ----- 2. Admin -----
        if (userService.findByEmail("admin@jobportal.com").isEmpty()) {
            User admin = new User("admin@jobportal.com", "admin123", "Admin", "ADMIN");
            userService.registerUser(admin);
            System.out.println("✅ Admin: admin@jobportal.com / admin123");
        }

        // ----- 3. Categories -----
        Category tech = createCategory("Tech", "Technology & Software");
        Category healthcare = createCategory("Healthcare", "Medical & Health");
        Category education = createCategory("Education", "Teaching & Learning");
        Category finance = createCategory("Finance", "Banking & Investment");
        Category startup = createCategory("Startup", "Fast-growing companies");

        // ----- 4. Employers -----
        User google = createEmployer("Google", "google@example.com");
        User meta = createEmployer("Meta", "meta@example.com");
        User amazon = createEmployer("Amazon", "amazon@example.com");
        User netflix = createEmployer("Netflix", "netflix@example.com");
        User apple = createEmployer("Apple", "apple@example.com");
        User oracle = createEmployer("Oracle", "oracle@example.com");
        User tesla = createEmployer("Tesla", "tesla@example.com");

        // ----- 5. Job Seekers -----
        createJobSeeker("Alice Johnson", "alice@example.com");
        createJobSeeker("Bob Smith", "bob@example.com");
        createJobSeeker("Carol White", "carol@example.com");

        // ----- 6. 25 Jobs with Categories -----
        List<JobData> jobs = Arrays.asList(
                // TECH (5)
                new JobData("Senior Java Developer",
                        "Build enterprise apps with Java, Spring Boot, microservices.",
                        "Google", "Mountain View, CA", "$140,000 - $180,000",
                        "https://picsum.photos/seed/java/400/300", google, tech),
                new JobData("React Frontend Engineer",
                        "Responsive web apps with React, Next.js, TypeScript.",
                        "Meta", "Menlo Park, CA", "$130,000 - $175,000",
                        "https://picsum.photos/seed/react/400/300", meta, tech),
                new JobData("AWS Cloud Engineer",
                        "AWS infrastructure: EC2, S3, Lambda, CloudFormation.",
                        "Amazon", "Seattle, WA", "$140,000 - $180,000",
                        "https://picsum.photos/seed/aws/400/300", amazon, tech),
                new JobData("Data Scientist",
                        "ML models with Python, TensorFlow, PyTorch.",
                        "Google", "Mountain View, CA", "$150,000 - $200,000",
                        "https://picsum.photos/seed/datascience/400/300", google, tech),
                new JobData("DevOps Engineer",
                        "CI/CD pipelines with Jenkins, GitLab, GitHub Actions.",
                        "Netflix", "Los Gatos, CA", "$130,000 - $170,000",
                        "https://picsum.photos/seed/devops/400/300", netflix, tech),

                // HEALTHCARE (5)
                new JobData("Registered Nurse",
                        "Full-time RN in a state-of-the-art hospital.",
                        "HealthPlus", "Chicago, IL", "$75,000 - $95,000",
                        "https://picsum.photos/seed/nurse/400/300", google, healthcare),
                new JobData("Healthcare Administrator",
                        "Manage daily operations of a medical facility.",
                        "HealthPlus", "Boston, MA", "$85,000 - $110,000",
                        "https://picsum.photos/seed/healthcare/400/300", google, healthcare),
                new JobData("Medical Researcher",
                        "Cutting-edge research in R&D department.",
                        "HealthPlus", "Seattle, WA", "$100,000 - $140,000",
                        "https://picsum.photos/seed/research/400/300", google, healthcare),
                new JobData("Physician Assistant",
                        "Join our healthcare team. PA certification required.",
                        "HealthPlus", "New York, NY", "$90,000 - $120,000",
                        "https://picsum.photos/seed/physician/400/300", google, healthcare),
                new JobData("Mental Health Counselor",
                        "Provide counseling and support. LPC or LCSW required.",
                        "HealthPlus", "Denver, CO", "$65,000 - $85,000",
                        "https://picsum.photos/seed/counselor/400/300", google, healthcare),

                // EDUCATION (5)
                new JobData("High School Teacher",
                        "Passionate educator for science department.",
                        "EduWorld", "Austin, TX", "$55,000 - $75,000",
                        "https://picsum.photos/seed/teacher/400/300", apple, education),
                new JobData("Curriculum Developer",
                        "Design innovative educational content.",
                        "EduWorld", "Remote", "$70,000 - $90,000",
                        "https://picsum.photos/seed/curriculum/400/300", apple, education),
                new JobData("University Professor",
                        "Teach computer science at top university.",
                        "EduWorld", "Cambridge, MA", "$90,000 - $120,000",
                        "https://picsum.photos/seed/professor/400/300", apple, education),
                new JobData("Special Education Teacher",
                        "Work with students with special needs.",
                        "EduWorld", "Los Angeles, CA", "$60,000 - $80,000",
                        "https://picsum.photos/seed/sped/400/300", apple, education),
                new JobData("Online Learning Coordinator",
                        "Manage online learning platform.",
                        "EduWorld", "Remote", "$65,000 - $85,000",
                        "https://picsum.photos/seed/online/400/300", apple, education),

                // FINANCE (5)
                new JobData("Investment Analyst",
                        "Analyze financial data and provide recommendations.",
                        "FinancePro", "New York, NY", "$90,000 - $120,000",
                        "https://picsum.photos/seed/investment/400/300", oracle, finance),
                new JobData("Financial Planner",
                        "Help clients achieve financial goals.",
                        "FinancePro", "Miami, FL", "$80,000 - $110,000",
                        "https://picsum.photos/seed/financial/400/300", oracle, finance),
                new JobData("Risk Manager",
                        "Identify and mitigate financial risks.",
                        "FinancePro", "Chicago, IL", "$95,000 - $125,000",
                        "https://picsum.photos/seed/risk/400/300", oracle, finance),
                new JobData("Accountant",
                        "Manage financial records and prepare reports.",
                        "FinancePro", "Dallas, TX", "$70,000 - $90,000",
                        "https://picsum.photos/seed/accountant/400/300", oracle, finance),
                new JobData("Treasury Analyst",
                        "Manage cash flow, liquidity, investment strategies.",
                        "FinancePro", "San Francisco, CA", "$85,000 - $110,000",
                        "https://picsum.photos/seed/treasury/400/300", oracle, finance),

                // STARTUP (5)
                new JobData("Full Stack Developer",
                        "Exciting startup opportunity with React, Node.js, MongoDB.",
                        "StartupHub", "San Francisco, CA", "$110,000 - $140,000",
                        "https://picsum.photos/seed/fullstack/400/300", tesla, startup),
                new JobData("Product Manager",
                        "Lead product development for AI-powered platform.",
                        "StartupHub", "Remote", "$120,000 - $150,000",
                        "https://picsum.photos/seed/product/400/300", tesla, startup),
                new JobData("Marketing Manager",
                        "Lead marketing campaigns, SEO, digital marketing.",
                        "StartupHub", "Chicago, IL", "$70,000 - $95,000",
                        "https://picsum.photos/seed/marketing/400/300", tesla, startup),
                new JobData("UX/UI Designer",
                        "Design intuitive interfaces with Figma.",
                        "StartupHub", "Austin, TX", "$80,000 - $110,000",
                        "https://picsum.photos/seed/ux/400/300", tesla, startup),
                new JobData("Data Analyst",
                        "Analyze user data to drive business decisions.",
                        "StartupHub", "Remote", "$75,000 - $100,000",
                        "https://picsum.photos/seed/dataanalyst/400/300", tesla, startup)
        );

        // ----- 7. Insert jobs -----
        int count = 0;
        for (JobData jobData : jobs) {
            try {
                createJob(jobData);
                count++;
                System.out.println("✅ Inserted: " + jobData.title + " → Category: " + jobData.category.getName());
            } catch (Exception e) {
                System.err.println("❌ Failed: " + jobData.title + " - " + e.getMessage());
            }
        }

        System.out.println("✅ Total " + count + " jobs seeded.");
        System.out.println("   Tech: 5, Healthcare: 5, Education: 5, Finance: 5, Startup: 5");
    }

    // ----- Helper Methods -----

    private Category createCategory(String name, String desc) {
        return categoryService.findByName(name).orElseGet(() ->
                categoryService.saveCategory(new Category(name, desc))
        );
    }

    private User createEmployer(String company, String email) {
        return userService.findByEmail(email).orElseGet(() ->
                userService.registerUser(new User(email, "password123", company + " HR", "EMPLOYER"))
        );
    }

    private void createJobSeeker(String fullName, String email) {
        if (userService.findByEmail(email).isEmpty()) {
            userService.registerUser(new User(email, "password123", fullName, "JOB_SEEKER"));
        }
    }

    private void createJob(JobData jobData) {
        Job job = new Job();
        job.setTitle(jobData.title);
        job.setDescription(jobData.description);
        job.setCompany(jobData.company);
        job.setLocation(jobData.location);
        job.setSalary(jobData.salary);
        job.setImageUrl(jobData.imageUrl);
        job.setEmployer(jobData.employer);
        job.setCategory(jobData.category);
        job.setPostedDate(LocalDateTime.now());
        jobService.postJob(job, jobData.employer);
    }

    private static class JobData {
        String title, description, company, location, salary, imageUrl;
        User employer;
        Category category;

        JobData(String title, String description, String company, String location,
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
    }
}