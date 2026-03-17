package com.example.userapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Represents a bank account belonging to a user.
 *
 * Each user can have multiple bank accounts (e.g. a checking and a savings account).
 * The relationship is defined as @ManyToOne on this side (many accounts belong to one user)
 * and @OneToMany on the User side.
 *
 * BigDecimal is used for the balance instead of double/float because floating-point types
 * introduce rounding errors with decimal arithmetic (e.g. 0.1 + 0.2 != 0.3).
 * BigDecimal provides exact precision, which is essential for financial data.
 */
@Entity
@Table(name = "bank_accounts")
public class BankAccount {

    /**
     * Primary key, auto-incremented by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The bank account number (e.g. "1234567890").
     */
    private String accountNumber;

    /**
     * The type of account: "CHECKING" or "SAVINGS".
     * Stored as a plain String for simplicity. In a production system,
     * this could be an enum with @Enumerated(EnumType.STRING).
     */
    private String accountType;

    /**
     * International Bank Account Number (e.g. "GB82WEST12345698765432").
     * Used for international transfers. Format varies by country.
     */
    private String iban;

    /**
     * Current account balance. BigDecimal ensures exact decimal precision
     * for monetary values (no floating-point rounding errors).
     */
    private BigDecimal balance;

    /**
     * @ManyToOne - Many bank accounts can belong to one user.
     * @JoinColumn(name = "user_id") - Creates a foreign key column "user_id" in the
     *   bank_accounts table that references the users table's primary key.
     * @JsonBackReference("user-accounts") - Prevents infinite JSON recursion.
     *   When serializing a BankAccount to JSON, the 'user' field is excluded.
     *   The matching @JsonManagedReference("user-accounts") on User's bankAccounts list
     *   ensures the accounts ARE included when serializing a User.
     *   The string label "user-accounts" pairs these two annotations together.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference("user-accounts")
    private User user;

    /**
     * No-arg constructor required by JPA for reflective instantiation.
     */
    public BankAccount() {
    }

    /**
     * Convenience constructor for creating accounts in the DataSeeder.
     * Does not include 'id' (auto-generated) or 'user' (set separately via setUser).
     */
    public BankAccount(String accountNumber, String accountType, String iban, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.iban = iban;
        this.balance = balance;
    }

    // --- Getters and Setters ---
    // Used by JPA for database operations and Jackson for JSON serialization.

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
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
