package com.example.userapi.service;

import com.example.userapi.model.BankAccount;
import com.example.userapi.repository.BankAccountRepository;
import com.example.userapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for BankAccount business logic.
 *
 * Follows the same patterns as UserService:
 *   - Constructor injection of repositories
 *   - Optional returns for single-entity lookups
 *   - Boolean return for delete to signal success/failure
 *
 * This service injects both BankAccountRepository (for account CRUD) and
 * UserRepository (to verify the user exists when creating an account).
 */
@Service
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;

    /**
     * Constructor injection - Spring provides both repository beans automatically.
     */
    public BankAccountService(BankAccountRepository bankAccountRepository,
                              UserRepository userRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all bank accounts for a specific user.
     * Uses the custom derived query method on BankAccountRepository.
     *
     * @param userId the user's ID
     * @return list of accounts (empty if user has none or doesn't exist)
     */
    public List<BankAccount> getAccountsByUserId(Long userId) {
        return bankAccountRepository.findByUserId(userId);
    }

    /**
     * Retrieves a single bank account by its ID.
     *
     * @param id the account's primary key
     * @return Optional containing the account, or empty if not found
     */
    public Optional<BankAccount> getAccountById(Long id) {
        return bankAccountRepository.findById(id);
    }

    /**
     * Creates a new bank account for a user.
     *
     * Looks up the user first to set the @ManyToOne relationship.
     * If the user doesn't exist, returns Optional.empty() so the
     * controller can respond with 404.
     *
     * @param userId  the ID of the user who owns this account
     * @param account the account data from the request body
     * @return Optional with the saved account (including generated ID), or empty if user not found
     */
    public Optional<BankAccount> createAccount(Long userId, BankAccount account) {
        return userRepository.findById(userId).map(user -> {
            account.setUser(user);
            return bankAccountRepository.save(account);
        });
    }

    /**
     * Updates an existing bank account's details.
     *
     * Finds the account, copies the new field values, and saves.
     * The user relationship is not changed during an update.
     *
     * @param id             the ID of the account to update
     * @param updatedAccount the new data from the request body
     * @return Optional with the updated account, or empty if not found
     */
    public Optional<BankAccount> updateAccount(Long id, BankAccount updatedAccount) {
        return bankAccountRepository.findById(id).map(account -> {
            account.setAccountNumber(updatedAccount.getAccountNumber());
            account.setAccountType(updatedAccount.getAccountType());
            account.setIban(updatedAccount.getIban());
            account.setBalance(updatedAccount.getBalance());
            return bankAccountRepository.save(account);
        });
    }

    /**
     * Deletes a bank account by ID.
     *
     * @param id the account's primary key
     * @return true if deleted, false if not found
     */
    public boolean deleteAccount(Long id) {
        if (bankAccountRepository.existsById(id)) {
            bankAccountRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
