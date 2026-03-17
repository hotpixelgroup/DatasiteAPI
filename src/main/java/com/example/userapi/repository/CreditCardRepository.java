package com.example.userapi.repository;

import com.example.userapi.model.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for CreditCard CRUD operations.
 *
 * Same pattern as BankAccountRepository - extends JpaRepository for free CRUD
 * methods and adds a custom derived query to find cards by user ID.
 */
public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {

    /**
     * Finds all credit cards belonging to a specific user.
     * Auto-generated SQL: SELECT * FROM credit_cards WHERE user_id = ?
     */
    List<CreditCard> findByUserId(Long userId);
}
