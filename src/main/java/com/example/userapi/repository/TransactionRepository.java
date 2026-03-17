package com.example.userapi.repository;

import com.example.userapi.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for Transaction CRUD operations.
 *
 * Includes two custom derived query methods:
 *   - findByUserId: all transactions for a user
 *   - findByUserIdAndType: filtered by transaction type (CREDIT or DEBIT)
 *
 * Spring Data JPA parses the method names and auto-generates the SQL.
 * "And" in the method name adds a WHERE clause: WHERE user_id = ? AND type = ?
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Finds all transactions belonging to a specific user.
     * Auto-generated SQL: SELECT * FROM transactions WHERE user_id = ?
     */
    List<Transaction> findByUserId(Long userId);

    /**
     * Finds transactions for a user filtered by type ("CREDIT" or "DEBIT").
     * Auto-generated SQL: SELECT * FROM transactions WHERE user_id = ? AND type = ?
     *
     * Useful for calculating monthly income (CREDIT only) or expenses (DEBIT only).
     */
    List<Transaction> findByUserIdAndType(Long userId, String type);
}
