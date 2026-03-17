package com.example.userapi.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The User entity represents a row in the "users" database table.
 * This is a JPA (Java Persistence API) entity - JPA is the standard Java API
 * for mapping Java objects to database tables (called ORM - Object-Relational Mapping).
 * Hibernate is the JPA implementation that Spring Boot uses under the hood.
 *
 * @Entity  - Tells JPA that this class maps to a database table. Without this,
 *            Hibernate would ignore this class entirely.
 * @Table(name = "users") - Specifies the table name. Without this, JPA would default
 *            to the class name "User", but "user" is a reserved keyword in some databases
 *            (including H2), so we explicitly name it "users" to avoid conflicts.
 */
@Entity
@Table(name = "users")
public class User {

    /**
     * @Id - Marks this field as the primary key of the table.
     *       Every JPA entity must have exactly one @Id field.
     *
     * @GeneratedValue(strategy = GenerationType.IDENTITY) - Tells the database to
     *       auto-generate the ID value. IDENTITY strategy means the database itself
     *       handles auto-increment (1, 2, 3, ...) when a new row is inserted.
     *       We never set this manually - the database assigns it.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * These fields map to columns in the "users" table automatically.
     * JPA uses the field names as column names by default (i.e. "name" and "email").
     * They are private, following Java encapsulation - accessed via getters/setters below.
     */
    private String name;
    private String email;

    /**
     * @OneToMany - One user has many bank accounts. This is the "parent" side of
     *   the relationship; the "child" side is the @ManyToOne in BankAccount.
     *
     * mappedBy = "user" - Tells JPA that the BankAccount entity's 'user' field
     *   owns the foreign key column. Without this, JPA would create a separate
     *   join table (users_bank_accounts), which is unnecessary.
     *
     * cascade = CascadeType.ALL - All JPA operations on User cascade to its accounts:
     *   - persist (save) a User -> also saves its accounts
     *   - remove (delete) a User -> also deletes its accounts
     *   This means deleting a user automatically cleans up all their financial data.
     *
     * orphanRemoval = true - If an account is removed from this list (e.g.
     *   user.getBankAccounts().remove(account)), JPA also deletes it from the database.
     *   Without this, removing from the list would only break the relationship,
     *   leaving an orphaned row in bank_accounts.
     *
     * @JsonManagedReference("user-accounts") - The "forward" side of the JSON
     *   serialization pair. When Jackson serializes a User to JSON, this list IS included.
     *   The matching @JsonBackReference("user-accounts") on BankAccount's 'user' field
     *   is excluded, preventing infinite recursion: User -> accounts -> user -> accounts...
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("user-accounts")
    private List<BankAccount> bankAccounts = new ArrayList<>();

    /**
     * One user has many credit cards. Same cascade and orphanRemoval behaviour
     * as bank accounts - deleting a user removes all their cards.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("user-creditcards")
    private List<CreditCard> creditCards = new ArrayList<>();

    /**
     * One user has many transactions. Full transaction history is cascaded
     * with the user lifecycle - deleting a user removes all their transactions.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("user-transactions")
    private List<Transaction> transactions = new ArrayList<>();

    /**
     * No-argument constructor (required by JPA).
     * JPA/Hibernate needs this to create User objects via reflection when
     * reading rows from the database. Without it, Hibernate would throw an error.
     */
    public User() {
    }

    /**
     * Convenience constructor for creating User objects in application code.
     * Note: we don't include 'id' here because the database generates it.
     * The list fields (bankAccounts, creditCards, transactions) initialise
     * to empty ArrayLists via their field declarations above.
     */
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // --- Getters and Setters ---
    // JPA uses these to read/write field values.
    // Spring's JSON serializer (Jackson) also uses these to convert User objects
    // to/from JSON in HTTP request and response bodies.

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<BankAccount> getBankAccounts() {
        return bankAccounts;
    }

    public void setBankAccounts(List<BankAccount> bankAccounts) {
        this.bankAccounts = bankAccounts;
    }

    public List<CreditCard> getCreditCards() {
        return creditCards;
    }

    public void setCreditCards(List<CreditCard> creditCards) {
        this.creditCards = creditCards;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
