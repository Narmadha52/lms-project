package com.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        try {
            jdbcTemplate.execute("SELECT 1");
            return ResponseEntity.ok("OK - DB Connected");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("DB Connection Failed: " + e.getMessage());
        }
    }
}
