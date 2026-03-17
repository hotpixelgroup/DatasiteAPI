package com.example.userapi.dto;

import java.math.BigDecimal;

/**
 * A Data Transfer Object (DTO) representing a user's calculated financial summary.
 *
 * This is NOT a JPA entity - it has no @Entity annotation and no database table.
 * It is computed on the fly from the user's bank accounts, credit cards, and
 * transactions each time it is requested, ensuring the data is always fresh.
 *
 * Why a DTO instead of an entity?
 *   - Financial summaries are derived data (calculated from other entities).
 *   - Storing them would introduce staleness: every account change or new transaction
 *     would require recalculating and re-saving the summary.
 *   - Computing on request guarantees accuracy without extra database writes.
 *
 * Jackson serializes this to JSON using the getters, just like an entity.
 */
public class FinancialSummary {

    /**
     * Sum of all bank account balances (checking + savings).
     */
    private BigDecimal totalBankBalance;

    /**
     * Sum of all credit card outstanding balances (total amount owed).
     */
    private BigDecimal totalCreditCardDebt;

    /**
     * Net worth = totalBankBalance - totalCreditCardDebt.
     * A simplified measure of financial health. Positive means assets
     * exceed debts; negative means the user owes more than they have.
     */
    private BigDecimal netWorth;

    /**
     * Credit score in the 300-850 range.
     * In this demo, it is deterministically derived from the user's ID
     * to produce a consistent score per user. A real system would pull
     * this from a credit bureau API (e.g. Experian, Equifax, TransUnion).
     */
    private int creditScore;

    /**
     * Total income received in the current calendar month.
     * Calculated by summing all CREDIT transactions with timestamps
     * from the 1st of the current month to now.
     */
    private BigDecimal monthlyIncome;

    /**
     * Total expenses in the current calendar month.
     * Calculated by summing all DEBIT transactions with timestamps
     * from the 1st of the current month to now.
     */
    private BigDecimal monthlyExpenses;

    /**
     * No-arg constructor for Jackson deserialization (if needed).
     */
    public FinancialSummary() {
    }

    /**
     * All-args constructor used by FinancialSummaryService to build the response.
     */
    public FinancialSummary(BigDecimal totalBankBalance, BigDecimal totalCreditCardDebt,
                            BigDecimal netWorth, int creditScore,
                            BigDecimal monthlyIncome, BigDecimal monthlyExpenses) {
        this.totalBankBalance = totalBankBalance;
        this.totalCreditCardDebt = totalCreditCardDebt;
        this.netWorth = netWorth;
        this.creditScore = creditScore;
        this.monthlyIncome = monthlyIncome;
        this.monthlyExpenses = monthlyExpenses;
    }

    // --- Getters and Setters ---

    public BigDecimal getTotalBankBalance() {
        return totalBankBalance;
    }

    public void setTotalBankBalance(BigDecimal totalBankBalance) {
        this.totalBankBalance = totalBankBalance;
    }

    public BigDecimal getTotalCreditCardDebt() {
        return totalCreditCardDebt;
    }

    public void setTotalCreditCardDebt(BigDecimal totalCreditCardDebt) {
        this.totalCreditCardDebt = totalCreditCardDebt;
    }

    public BigDecimal getNetWorth() {
        return netWorth;
    }

    public void setNetWorth(BigDecimal netWorth) {
        this.netWorth = netWorth;
    }

    public int getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(int creditScore) {
        this.creditScore = creditScore;
    }

    public BigDecimal getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(BigDecimal monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public BigDecimal getMonthlyExpenses() {
        return monthlyExpenses;
    }

    public void setMonthlyExpenses(BigDecimal monthlyExpenses) {
        this.monthlyExpenses = monthlyExpenses;
    }
}
