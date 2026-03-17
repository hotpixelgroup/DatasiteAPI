package com.example.userapi.controller;

import com.example.userapi.model.BankAccount;
import com.example.userapi.service.BankAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing bank accounts nested under a user.
 *
 * All endpoints are prefixed with /api/users/{userId}/accounts, following REST
 * conventions for sub-resources. The {userId} in the path establishes the
 * parent-child relationship between users and their accounts.
 *
 * @RestController - Combines @Controller + @ResponseBody. Return values are
 *   serialized to JSON automatically by Jackson.
 * @RequestMapping - Sets the base URL path. {userId} is a path variable
 *   extracted by @PathVariable in each method.
 */
@RestController
@RequestMapping("/api/users/{userId}/accounts")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    /**
     * GET /api/users/{userId}/accounts
     *
     * Lists all bank accounts for a user. Returns an empty array if the user
     * has no accounts. Always returns 200 OK.
     */
    @GetMapping
    public List<BankAccount> getAccountsByUserId(@PathVariable Long userId) {
        return bankAccountService.getAccountsByUserId(userId);
    }

    /**
     * GET /api/users/{userId}/accounts/{id}
     *
     * Retrieves a single bank account by its ID.
     * Returns 200 OK with the account, or 404 Not Found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BankAccount> getAccountById(@PathVariable Long userId,
                                                      @PathVariable Long id) {
        return bankAccountService.getAccountById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/users/{userId}/accounts
     *
     * Creates a new bank account for the specified user.
     * Returns 201 Created with the saved account, or 404 if the user doesn't exist.
     *
     * The service handles linking the account to the user via the @ManyToOne relationship.
     */
    @PostMapping
    public ResponseEntity<BankAccount> createAccount(@PathVariable Long userId,
                                                     @RequestBody BankAccount account) {
        return bankAccountService.createAccount(userId, account)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * PUT /api/users/{userId}/accounts/{id}
     *
     * Updates an existing bank account's details (account number, type, IBAN, balance).
     * Returns 200 OK with the updated account, or 404 if not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BankAccount> updateAccount(@PathVariable Long userId,
                                                     @PathVariable Long id,
                                                     @RequestBody BankAccount account) {
        return bankAccountService.updateAccount(id, account)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /api/users/{userId}/accounts/{id}
     *
     * Deletes a bank account. Returns 204 No Content on success, 404 if not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long userId,
                                              @PathVariable Long id) {
        if (bankAccountService.deleteAccount(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
