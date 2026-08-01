package com.example.jobportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    // ----- 1. Confirmation to Job Seeker after applying -----
    public void sendApplicationConfirmationToSeeker(String toEmail, String jobTitle, String companyName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("✅ Application Received for '" + jobTitle + "'");
            message.setText(
                    "Dear Applicant,\n\n" +
                            "Thank you for applying for the position of '" + jobTitle + "' at " + companyName + ".\n\n" +
                            "Your application has been successfully submitted and is now under review.\n\n" +
                            "You can track the status of your application anytime by logging into your JobPortal dashboard.\n\n" +
                            "We will notify you once the employer makes a decision.\n\n" +
                            "Best regards,\n" +
                            "JobPortal Team"
            );
            mailSender.send(message);
            System.out.println("✅ Confirmation email sent to seeker: " + toEmail);
        } catch (Exception e) {
            System.err.println("❌ Seeker confirmation failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ----- 2. Notification to Employer -----
    public void sendApplicationNotification(String toEmail, String jobTitle, String seekerName, String coverLetter) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("🔔 New Application for '" + jobTitle + "'");
            message.setText(
                    "Hello,\n\n" +
                            "A new application has been submitted for the position: " + jobTitle + "\n\n" +
                            "👤 Applicant: " + seekerName + "\n" +
                            "📝 Cover Letter:\n" + coverLetter + "\n\n" +
                            "Please log in to your employer dashboard to review the full application and download the resume.\n\n" +
                            "Best regards,\n" +
                            "JobPortal Team"
            );
            mailSender.send(message);
            System.out.println("✅ Employer notification sent: " + toEmail);
        } catch (Exception e) {
            System.err.println("❌ Employer notification failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ----- 3. Acceptance Email to Job Seeker -----
    public void sendApplicationAcceptedEmail(String toEmail, String jobTitle, String companyName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("🎉 Congratulations! Your Application for '" + jobTitle + "' was Accepted!");
            message.setText(
                    "Dear Applicant,\n\n" +
                            "🎊 We are delighted to inform you that your application for the position of '" + jobTitle + "' at " + companyName + " has been ACCEPTED!\n\n" +
                            "The employer will contact you shortly with the next steps (interview, onboarding, etc.).\n\n" +
                            "We wish you all the best in this new opportunity!\n\n" +
                            "Best regards,\n" +
                            "JobPortal Team"
            );
            mailSender.send(message);
            System.out.println("✅ Acceptance email sent to seeker: " + toEmail);
        } catch (Exception e) {
            System.err.println("❌ Acceptance email failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ----- 4. Rejection Email to Job Seeker -----
    public void sendApplicationRejectedEmail(String toEmail, String jobTitle, String companyName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("📄 Update on Your Application for '" + jobTitle + "'");
            message.setText(
                    "Dear Applicant,\n\n" +
                            "Thank you for your interest in the position of '" + jobTitle + "' at " + companyName + ".\n\n" +
                            "After careful review, the employer has decided to move forward with other candidates at this time.\n\n" +
                            "We encourage you to continue exploring other exciting opportunities on JobPortal.\n" +
                            "Your skills and experience are valuable – don't give up!\n\n" +
                            "We wish you the best in your job search.\n\n" +
                            "Best regards,\n" +
                            "JobPortal Team"
            );
            mailSender.send(message);
            System.out.println("✅ Rejection email sent to seeker: " + toEmail);
        } catch (Exception e) {
            System.err.println("❌ Rejection email failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ----- 5. Test Email (for debugging) -----
    public void sendSimpleEmail(String toEmail, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            System.out.println("✅ Test email sent to: " + toEmail);
        } catch (Exception e) {
            System.err.println("❌ Test email failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}