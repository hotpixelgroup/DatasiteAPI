package com.example.userapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Represents a credit card belonging to a user.
 *
 * Each user can have multiple credit cards. The 'balance' field represents
 * the outstanding amount owed (money spent but not yet paid off), while
 * 'creditLimit' is the maximum the cardholder is allowed to borrow.
 *
 * Available credit = creditLimit - balance.
 */
@Entity
@Table(name = "credit_cards")
public class CreditCard {

    /**
     * Primary key, auto-incremented by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The 16-digit card number (e.g. "4111111111111111").
     * In a real application, this would be encrypted or tokenized for PCI compliance.
     */
    private String cardNumber;

    /**
     * Card expiry date in "MM/YYYY" format (e.g. "12/2028").
     * Stored as a String for display simplicity. A production system might
     * use java.time.YearMonth for type safety.
     */
    private String expiryDate;

    /**
     * The maximum amount the cardholder can borrow (e.g. 5000.00).
     * Set by the card issuer based on creditworthiness.
     */
    private BigDecimal creditLimit;

    /**
     * The current outstanding balance (amount owed).
     * This is money that has been spent on the card but not yet paid off.
     * A balance of 0.00 means the card is fully paid.
     */
    private BigDecimal balance;

    /**
     * @ManyToOne - Many credit cards can belong to one user.
     * @JoinColumn(name = "user_id") - FK column in credit_cards table.
     * @JsonBackReference("user-creditcards") - Excludes 'user' from JSON output
     *   to prevent infinite recursion. Paired with @JsonManagedReference on User.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference("user-creditcards")
    private User user;

    /**
     * No-arg constructor required by JPA.
     */
    public CreditCard() {
    }

    /**
     * Convenience constructor for seeding data.
     */
    public CreditCard(String cardNumber, String expiryDate, BigDecimal creditLimit, BigDecimal balance) {
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.creditLimit = creditLimit;
        this.balance = balance;
    }

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
