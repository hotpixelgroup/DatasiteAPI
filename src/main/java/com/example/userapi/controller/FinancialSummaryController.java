package com.example.userapi.controller;

import com.example.userapi.dto.FinancialSummary;
import com.example.userapi.service.FinancialSummaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for retrieving a user's calculated financial summary.
 *
 * This is a read-only endpoint - there is only one GET method. The financial
 * summary is computed on the fly from the user's bank accounts, credit cards,
 * and transactions (not stored in the database), so it is always up to date.
 */
@RestController
@RequestMapping("/api/users/{userId}/financial-summary")
public class FinancialSummaryController {

    private final FinancialSummaryService financialSummaryService;

    public FinancialSummaryController(FinancialSummaryService financialSummaryService) {
        this.financialSummaryService = financialSummaryService;
    }

    /**
     * GET /api/users/{userId}/financial-summary
     *
     * Returns a calculated financial summary for the user including:
     *   - totalBankBalance: sum of all bank account balances
     *   - totalCreditCardDebt: sum of all credit card balances owed
     *   - netWorth: bank balance minus credit card debt
     *   - creditScore: 300-850 range score
     *   - monthlyIncome: sum of CREDIT transactions this month
     *   - monthlyExpenses: sum of DEBIT transactions this month
     *
     * Returns 200 OK with the summary, or 404 if the user doesn't exist.
     */
    @GetMapping
    public ResponseEntity<FinancialSummary> getSummary(@PathVariable Long userId) {
        return financialSummaryService.getSummaryForUser(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
