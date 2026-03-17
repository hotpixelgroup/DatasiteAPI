package com.example.userapi.repository;

import com.example.userapi.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for BankAccount CRUD operations.
 *
 * Extends JpaRepository<BankAccount, Long> to inherit all standard CRUD methods
 * (findAll, findById, save, deleteById, etc.) without writing any implementation.
 *
 * The custom method findByUserId is a Spring Data JPA "derived query method".
 * Spring parses the method name and auto-generates the SQL:
 *   findByUserId(Long userId) -> SELECT * FROM bank_accounts WHERE user_id = ?
 * No @Query annotation or manual SQL needed.
 */
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    /**
     * Finds all bank accounts belonging to a specific user.
     *
     * @param userId the ID of the user whose accounts to retrieve
     * @return list of bank accounts (empty list if user has none)
     */
    List<BankAccount> findByUserId(Long userId);
}
