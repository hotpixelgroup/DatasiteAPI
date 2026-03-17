package com.example.userapi.service;

import com.example.userapi.model.CreditCard;
import com.example.userapi.repository.CreditCardRepository;
import com.example.userapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for CreditCard business logic.
 *
 * Same structural pattern as BankAccountService: injects both its own repository
 * and UserRepository, uses Optional returns, boolean for deletes.
 */
@Service
public class CreditCardService {

    private final CreditCardRepository creditCardRepository;
    private final UserRepository userRepository;

    public CreditCardService(CreditCardRepository creditCardRepository,
                             UserRepository userRepository) {
        this.creditCardRepository = creditCardRepository;
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all credit cards for a specific user.
     */
    public List<CreditCard> getCardsByUserId(Long userId) {
        return creditCardRepository.findByUserId(userId);
    }

    /**
     * Retrieves a single credit card by its ID.
     */
    public Optional<CreditCard> getCardById(Long id) {
        return creditCardRepository.findById(id);
    }

    /**
     * Creates a new credit card for a user.
     * Returns empty Optional if the user doesn't exist (triggers 404 in controller).
     */
    public Optional<CreditCard> createCard(Long userId, CreditCard card) {
        return userRepository.findById(userId).map(user -> {
            card.setUser(user);
            return creditCardRepository.save(card);
        });
    }

    /**
     * Updates an existing credit card's details.
     * Updates all mutable fields: card number, expiry, limit, and balance.
     */
    public Optional<CreditCard> updateCard(Long id, CreditCard updatedCard) {
        return creditCardRepository.findById(id).map(card -> {
            card.setCardNumber(updatedCard.getCardNumber());
            card.setExpiryDate(updatedCard.getExpiryDate());
            card.setCreditLimit(updatedCard.getCreditLimit());
            card.setBalance(updatedCard.getBalance());
            return creditCardRepository.save(card);
        });
    }

    /**
     * Deletes a credit card by ID.
     *
     * @return true if deleted, false if not found
     */
    public boolean deleteCard(Long id) {
        if (creditCardRepository.existsById(id)) {
            creditCardRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
