package com.example.userapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a financial transaction (income or expense) belonging to a user.
 *
 * Transactions are the building blocks of a user's financial history. Each transaction
 * records money coming in (CREDIT) or going out (DEBIT), categorised for analysis.
 *
 * The 'amount' field is always positive - the 'type' field indicates direction:
 *   - CREDIT = money received (salary, freelance income, refunds)
 *   - DEBIT  = money spent (rent, groceries, entertainment)
 *
 * Transactions are intentionally immutable in this API design - there is no PUT/update
 * endpoint. In financial systems, you record corrections or reversals as new transactions
 * rather than modifying history, to maintain an accurate audit trail.
 */
@Entity
@Table(name = "transactions")
public class Transaction {

    /**
     * Primary key, auto-incremented by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Transaction direction: "CREDIT" (money in) or "DEBIT" (money out).
     */
    private String type;

    /**
     * Spending/income category for analysis and reporting.
     * Examples: "SALARY", "RENT", "GROCERIES", "ENTERTAINMENT", "UTILITIES",
     *           "FREELANCE", "DINING", "TRANSPORT", "HEALTHCARE", "SHOPPING".
     */
    private String category;

    /**
     * Human-readable description of what the transaction was for.
     * e.g. "Monthly salary", "Grocery store purchase", "Electric bill".
     */
    private String description;

    /**
     * The monetary amount of the transaction (always positive).
     * The direction (in/out) is determined by the 'type' field.
     * BigDecimal ensures exact decimal precision for financial calculations.
     */
    private BigDecimal amount;

    /**
     * When the transaction occurred. LocalDateTime stores both date and time
     * without timezone info (suitable for this demo; production systems would
     * use ZonedDateTime or store UTC with timezone metadata).
     *
     * Spring Boot auto-configures Jackson's JavaTimeModule, so this serializes
     * to ISO-8601 format in JSON: "2026-03-07T14:30:00"
     */
    private LocalDateTime timestamp;

    /**
     * @ManyToOne - Many transactions belong to one user.
     * @JoinColumn(name = "user_id") - FK column in the transactions table.
     * @JsonBackReference("user-transactions") - Prevents infinite JSON recursion.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference("user-transactions")
    private User user;

    /**
     * No-arg constructor required by JPA.
     */
    public Transaction() {
    }

    /**
     * Convenience constructor for seeding data.
     */
    public Transaction(String type, String category, String description, BigDecimal amount, LocalDateTime timestamp) {
        this.type = type;
        this.category = category;
        this.description = description;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
