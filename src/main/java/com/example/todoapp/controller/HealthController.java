package com.example.todoapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> status = new HashMap<>();
        
        // Check database connectivity
        try (Connection conn = dataSource.getConnection()) {
            status.put("database", "UP");
        } catch (Exception e) {
            status.put("database", "DOWN");
            status.put("error", e.getMessage());
        }

        status.put("service", "UP");
        
        return ResponseEntity.ok(status);
    }
} 