package com.example.userapi.controller;

import com.example.userapi.model.CreditCard;
import com.example.userapi.service.CreditCardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing credit cards nested under a user.
 *
 * Follows the same patterns as BankAccountController:
 *   - Nested under /api/users/{userId}/credit-cards
 *   - Full CRUD: GET (list), GET (by ID), POST, PUT, DELETE
 *   - ResponseEntity for HTTP status code control
 *   - Optional pattern for 404 handling
 */
@RestController
@RequestMapping("/api/users/{userId}/credit-cards")
public class CreditCardController {

    private final CreditCardService creditCardService;

    public CreditCardController(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }

    /**
     * GET /api/users/{userId}/credit-cards
     *
     * Lists all credit cards for a user. Always returns 200 OK.
     */
    @GetMapping
    public List<CreditCard> getCardsByUserId(@PathVariable Long userId) {
        return creditCardService.getCardsByUserId(userId);
    }

    /**
     * GET /api/users/{userId}/credit-cards/{id}
     *
     * Retrieves a single credit card by ID. Returns 200 or 404.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CreditCard> getCardById(@PathVariable Long userId,
                                                   @PathVariable Long id) {
        return creditCardService.getCardById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/users/{userId}/credit-cards
     *
     * Creates a new credit card for the user. Returns 201 or 404 (user not found).
     */
    @PostMapping
    public ResponseEntity<CreditCard> createCard(@PathVariable Long userId,
                                                  @RequestBody CreditCard card) {
        return creditCardService.createCard(userId, card)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * PUT /api/users/{userId}/credit-cards/{id}
     *
     * Updates an existing credit card's details. Returns 200 or 404.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CreditCard> updateCard(@PathVariable Long userId,
                                                  @PathVariable Long id,
                                                  @RequestBody CreditCard card) {
        return creditCardService.updateCard(id, card)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /api/users/{userId}/credit-cards/{id}
     *
     * Deletes a credit card. Returns 204 No Content or 404.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long userId,
                                           @PathVariable Long id) {
        if (creditCardService.deleteCard(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
