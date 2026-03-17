package com.example.userapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Handles the root URL (/) so visitors see a helpful overview instead of
 * the default Spring Boot "Whitelabel Error Page".
 *
 * Returns a JSON object listing every available endpoint in the API,
 * grouped by resource. LinkedHashMap is used instead of HashMap to
 * preserve insertion order in the JSON output.
 */
@RestController
public class RootController {

    /**
     * GET /
     *
     * Returns a welcome message and a map of all available API endpoints.
     */
    @GetMapping("/")
    public Map<String, Object> root() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("application", "User Financial API");
        response.put("version", "1.0.0");

        Map<String, String> endpoints = new LinkedHashMap<>();

        // Users
        endpoints.put("GET    /api/users", "List all users with financial data");
        endpoints.put("GET    /api/users/{id}", "Get a user by ID");
        endpoints.put("POST   /api/users", "Create a new user");
        endpoints.put("PUT    /api/users/{id}", "Update a user");
        endpoints.put("DELETE /api/users/{id}", "Delete a user");

        // Bank Accounts
        endpoints.put("GET    /api/users/{userId}/accounts", "List user's bank accounts");
        endpoints.put("GET    /api/users/{userId}/accounts/{id}", "Get a bank account by ID");
        endpoints.put("POST   /api/users/{userId}/accounts", "Create a bank account");
        endpoints.put("PUT    /api/users/{userId}/accounts/{id}", "Update a bank account");
        endpoints.put("DELETE /api/users/{userId}/accounts/{id}", "Delete a bank account");

        // Credit Cards
        endpoints.put("GET    /api/users/{userId}/credit-cards", "List user's credit cards");
        endpoints.put("GET    /api/users/{userId}/credit-cards/{id}", "Get a credit card by ID");
        endpoints.put("POST   /api/users/{userId}/credit-cards", "Create a credit card");
        endpoints.put("PUT    /api/users/{userId}/credit-cards/{id}", "Update a credit card");
        endpoints.put("DELETE /api/users/{userId}/credit-cards/{id}", "Delete a credit card");

        // Transactions
        endpoints.put("GET    /api/users/{userId}/transactions", "List user's transactions");
        endpoints.put("GET    /api/users/{userId}/transactions/{id}", "Get a transaction by ID");
        endpoints.put("POST   /api/users/{userId}/transactions", "Record a new transaction");
        endpoints.put("DELETE /api/users/{userId}/transactions/{id}", "Delete a transaction");

        // Financial Summary
        endpoints.put("GET    /api/users/{userId}/financial-summary", "Get calculated financial summary");

        response.put("endpoints", endpoints);
        return response;
    }
}
