package com.example.userapi.controller;

import com.example.userapi.model.Transaction;
import com.example.userapi.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing transactions nested under a user.
 *
 * Transactions support GET, POST, and DELETE but NOT PUT (no updates).
 * Financial transactions are immutable by design - corrections are recorded
 * as new transactions rather than modifying existing ones. This preserves
 * an accurate audit trail.
 */
@RestController
@RequestMapping("/api/users/{userId}/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * GET /api/users/{userId}/transactions
     *
     * Lists all transactions for a user. Always returns 200 OK.
     */
    @GetMapping
    public List<Transaction> getTransactionsByUserId(@PathVariable Long userId) {
        return transactionService.getTransactionsByUserId(userId);
    }

    /**
     * GET /api/users/{userId}/transactions/{id}
     *
     * Retrieves a single transaction by ID. Returns 200 or 404.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long userId,
                                                          @PathVariable Long id) {
        return transactionService.getTransactionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/users/{userId}/transactions
     *
     * Records a new transaction for the user. Returns 201 or 404 (user not found).
     *
     * Expected JSON body:
     * {
     *   "type": "DEBIT",
     *   "category": "GROCERIES",
     *   "description": "Weekly grocery shopping",
     *   "amount": 85.30,
     *   "timestamp": "2026-03-07T10:30:00"
     * }
     */
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@PathVariable Long userId,
                                                         @RequestBody Transaction transaction) {
        return transactionService.createTransaction(userId, transaction)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /api/users/{userId}/transactions/{id}
     *
     * Deletes a transaction. Returns 204 No Content or 404.
     * Note: No PUT endpoint exists - transactions are immutable.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long userId,
                                                   @PathVariable Long id) {
        if (transactionService.deleteTransaction(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
