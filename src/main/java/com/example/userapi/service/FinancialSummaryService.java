package com.example.userapi.service;

import com.example.userapi.dto.FinancialSummary;
import com.example.userapi.model.BankAccount;
import com.example.userapi.model.CreditCard;
import com.example.userapi.model.Transaction;
import com.example.userapi.repository.BankAccountRepository;
import com.example.userapi.repository.CreditCardRepository;
import com.example.userapi.repository.TransactionRepository;
import com.example.userapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service that calculates a user's financial summary on the fly.
 *
 * This service does not have its own repository - it reads from the existing
 * BankAccount, CreditCard, and Transaction repositories and computes aggregated
 * metrics. The result is returned as a FinancialSummary DTO (not persisted).
 *
 * All calculations use BigDecimal arithmetic to avoid floating-point rounding errors.
 */
@Service
public class FinancialSummaryService {

    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;
    private final CreditCardRepository creditCardRepository;
    private final TransactionRepository transactionRepository;

    public FinancialSummaryService(UserRepository userRepository,
                                   BankAccountRepository bankAccountRepository,
                                   CreditCardRepository creditCardRepository,
                                   TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.creditCardRepository = creditCardRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * Calculates a comprehensive financial summary for a user.
     *
     * The summary includes:
     *   - totalBankBalance: sum of all bank account balances
     *   - totalCreditCardDebt: sum of all credit card outstanding balances
     *   - netWorth: bank balance minus credit card debt
     *   - creditScore: a deterministic demo score derived from the user ID
     *   - monthlyIncome: sum of CREDIT transactions in the current calendar month
     *   - monthlyExpenses: sum of DEBIT transactions in the current calendar month
     *
     * @param userId the user's ID
     * @return Optional with the calculated summary, or empty if user doesn't exist
     */
    public Optional<FinancialSummary> getSummaryForUser(Long userId) {
        return userRepository.findById(userId).map(user -> {

            // Fetch all financial data for the user
            List<BankAccount> accounts = bankAccountRepository.findByUserId(userId);
            List<CreditCard> cards = creditCardRepository.findByUserId(userId);
            List<Transaction> transactions = transactionRepository.findByUserId(userId);

            // Sum all bank account balances using Java Streams.
            // .map(BankAccount::getBalance) extracts the balance from each account.
            // .reduce(BigDecimal.ZERO, BigDecimal::add) sums them, starting from 0.
            BigDecimal totalBankBalance = accounts.stream()
                    .map(BankAccount::getBalance)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Sum all credit card outstanding balances (total debt)
            BigDecimal totalCreditCardDebt = cards.stream()
                    .map(CreditCard::getBalance)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Net worth = what you have minus what you owe
            BigDecimal netWorth = totalBankBalance.subtract(totalCreditCardDebt);

            // Credit score: deterministic formula for demo purposes.
            // Produces scores between 650-849 across the 10 seeded users.
            // A real application would call an external credit bureau API.
            int creditScore = 650 + (int) (userId * 17 % 200);

            // Calculate the start of the current calendar month for filtering transactions.
            // withDayOfMonth(1) sets the day to the 1st.
            // .toLocalDate().atStartOfDay() sets the time to 00:00:00.
            LocalDateTime startOfMonth = LocalDateTime.now()
                    .withDayOfMonth(1)
                    .toLocalDate()
                    .atStartOfDay();

            // Sum all CREDIT (income) transactions from the current month
            BigDecimal monthlyIncome = transactions.stream()
                    .filter(t -> "CREDIT".equals(t.getType()))
                    .filter(t -> !t.getTimestamp().isBefore(startOfMonth))
                    .map(Transaction::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Sum all DEBIT (expense) transactions from the current month
            BigDecimal monthlyExpenses = transactions.stream()
                    .filter(t -> "DEBIT".equals(t.getType()))
                    .filter(t -> !t.getTimestamp().isBefore(startOfMonth))
                    .map(Transaction::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            return new FinancialSummary(
                    totalBankBalance, totalCreditCardDebt, netWorth,
                    creditScore, monthlyIncome, monthlyExpenses
            );
        });
    }
}
