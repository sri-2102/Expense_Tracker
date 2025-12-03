package com.expense.tracker.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class HomeController {
    
    @GetMapping("/")
    public Map<String, Object> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("application", "Expense Tracker API");
        response.put("version", "1.0.0");
        response.put("status", "running");
        response.put("timestamp", LocalDateTime.now());
        response.put("message", "Welcome to Expense Tracker API");
        
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("register", "POST /api/auth/register");
        endpoints.put("login", "POST /api/auth/login");
        endpoints.put("expenses", "GET /api/expenses (requires authentication)");
        endpoints.put("budgets", "GET /api/budgets (requires authentication)");
        endpoints.put("reports", "GET /api/reports/monthly/pdf?month=12&year=2025 (requires authentication)");
        
        response.put("endpoints", endpoints);
        return response;
    }
    
    @GetMapping("/api/test")
    public Map<String, String> test() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "API is working correctly");
        return response;
    }
}
