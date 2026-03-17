package com.example.userapi.service;

import com.example.userapi.model.Transaction;
import com.example.userapi.repository.TransactionRepository;
import com.example.userapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for Transaction business logic.
 *
 * Transactions are intentionally immutable - there is no update method.
 * In financial systems, corrections are recorded as new transactions
 * (e.g. a reversal) rather than editing existing records, to preserve
 * an accurate audit trail.
 *
 * Only create, read, and delete operations are supported.
 */
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all transactions for a specific user, ordered by database insertion.
     */
    public List<Transaction> getTransactionsByUserId(Long userId) {
        return transactionRepository.findByUserId(userId);
    }

    /**
     * Retrieves a single transaction by its ID.
     */
    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    /**
     * Records a new transaction for a user.
     * Returns empty Optional if the user doesn't exist.
     */
    public Optional<Transaction> createTransaction(Long userId, Transaction transaction) {
        return userRepository.findById(userId).map(user -> {
            transaction.setUser(user);
            return transactionRepository.save(transaction);
        });
    }

    /**
     * Deletes a transaction by ID.
     *
     * @return true if deleted, false if not found
     */
    public boolean deleteTransaction(Long id) {
        if (transactionRepository.existsById(id)) {
            transactionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
