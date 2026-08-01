package com.example.jobportal.controller;

import com.example.jobportal.DbSeeder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class SeedController {

    @Autowired
    private DbSeeder dbSeeder;

    @GetMapping("/seed")
    public String seed() {
        try {
            dbSeeder.run(null);
            return "✅ Seeding completed! Check your database.";
        } catch (Exception e) {
            return "❌ Error: " + e.getMessage();
        }
    }
}