package com.example.userapi.config;

import com.example.userapi.model.BankAccount;
import com.example.userapi.model.CreditCard;
import com.example.userapi.model.Transaction;
import com.example.userapi.model.User;
import com.example.userapi.repository.BankAccountRepository;
import com.example.userapi.repository.CreditCardRepository;
import com.example.userapi.repository.TransactionRepository;
import com.example.userapi.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Seeds the database with sample users and comprehensive financial data on startup.
 *
 * Creates 10 users, each with:
 *   - 1-2 bank accounts (checking and/or savings)
 *   - 1-2 credit cards with varying limits and balances
 *   - 5-8 transactions spanning the last 30 days (mix of income and expenses)
 *
 * All financial amounts use BigDecimal for monetary precision.
 * The data is diverse across users to demonstrate realistic variation.
 */
@Configuration
public class DataSeeder {

    /**
     * CommandLineRunner bean that runs after the application context loads.
     * Spring injects all four repositories automatically.
     *
     * Order of operations:
     *   1. Save all users first (so they get auto-generated IDs)
     *   2. For each user, create and save bank accounts, credit cards, and transactions
     *   3. Each child entity is linked to its user via setUser() before saving
     */
    @Bean
    CommandLineRunner seedUsers(UserRepository userRepository,
                                BankAccountRepository bankAccountRepository,
                                CreditCardRepository creditCardRepository,
                                TransactionRepository transactionRepository) {
        return args -> {
            // --- Step 1: Create and save all 10 users ---
            // saveAll returns the list with auto-generated IDs populated
            List<User> users = userRepository.saveAll(List.of(
                new User("Alice Johnson", "alice.johnson@example.com"),
                new User("Bob Smith", "bob.smith@example.com"),
                new User("Charlie Brown", "charlie.brown@example.com"),
                new User("Diana Prince", "diana.prince@example.com"),
                new User("Edward Davis", "edward.davis@example.com"),
                new User("Fiona Clark", "fiona.clark@example.com"),
                new User("George Wilson", "george.wilson@example.com"),
                new User("Hannah Moore", "hannah.moore@example.com"),
                new User("Isaac Taylor", "isaac.taylor@example.com"),
                new User("Julia Anderson", "julia.anderson@example.com")
            ));

            // Current time reference for generating transaction timestamps
            LocalDateTime now = LocalDateTime.now();

            // --- Step 2: Seed financial data for each user ---

            // ===== User 1: Alice Johnson =====
            // Junior web developer, modest income, responsible saver
            User alice = users.get(0);
            bankAccountRepository.saveAll(List.of(
                createAccount("1234567890", "CHECKING", "GB82WEST12345698765432", "5420.50", alice),
                createAccount("1234567891", "SAVINGS", "GB82WEST12345698765433", "12000.00", alice)
            ));
            creditCardRepository.save(
                createCard("4111111111111111", "12/2028", "5000.00", "1230.45", alice)
            );
            transactionRepository.saveAll(List.of(
                createTx("CREDIT", "SALARY", "Monthly salary", "4500.00", now.minusDays(15), alice),
                createTx("DEBIT", "RENT", "Monthly rent payment", "1200.00", now.minusDays(14), alice),
                createTx("DEBIT", "GROCERIES", "Supermarket purchase", "85.30", now.minusDays(10), alice),
                createTx("DEBIT", "ENTERTAINMENT", "Cinema tickets", "24.00", now.minusDays(7), alice),
                createTx("DEBIT", "UTILITIES", "Electric bill", "95.00", now.minusDays(5), alice),
                createTx("CREDIT", "FREELANCE", "Freelance web design", "800.00", now.minusDays(3), alice)
            ));

            // ===== User 2: Bob Smith =====
            // Homeowner with mortgage, bi-weekly pay, two credit cards
            User bob = users.get(1);
            bankAccountRepository.save(
                createAccount("2345678901", "CHECKING", "GB29NWBK60161331926819", "3280.75", bob)
            );
            creditCardRepository.saveAll(List.of(
                createCard("4222222222222222", "06/2027", "8000.00", "3450.00", bob),
                createCard("5333333333333333", "09/2029", "3000.00", "890.25", bob)
            ));
            transactionRepository.saveAll(List.of(
                createTx("CREDIT", "SALARY", "Bi-weekly paycheck", "2250.00", now.minusDays(14), bob),
                createTx("CREDIT", "SALARY", "Bi-weekly paycheck", "2250.00", now.minusDays(1), bob),
                createTx("DEBIT", "MORTGAGE", "Mortgage payment", "1800.00", now.minusDays(12), bob),
                createTx("DEBIT", "GROCERIES", "Grocery store", "120.45", now.minusDays(8), bob),
                createTx("DEBIT", "TRANSPORT", "Gas station fill-up", "65.00", now.minusDays(6), bob),
                createTx("DEBIT", "DINING", "Restaurant dinner", "78.50", now.minusDays(4), bob),
                createTx("DEBIT", "SUBSCRIPTION", "Streaming services", "29.99", now.minusDays(2), bob)
            ));

            // ===== User 3: Charlie Brown =====
            // Higher earner, good savings, investment income
            User charlie = users.get(2);
            bankAccountRepository.saveAll(List.of(
                createAccount("3456789012", "CHECKING", "GB76LOYD30949301273801", "8150.00", charlie),
                createAccount("3456789013", "SAVINGS", "GB76LOYD30949301273802", "25000.00", charlie)
            ));
            creditCardRepository.save(
                createCard("4333333333333333", "03/2028", "10000.00", "2100.00", charlie)
            );
            transactionRepository.saveAll(List.of(
                createTx("CREDIT", "SALARY", "Monthly salary", "6200.00", now.minusDays(16), charlie),
                createTx("DEBIT", "RENT", "Apartment rent", "1500.00", now.minusDays(15), charlie),
                createTx("DEBIT", "GROCERIES", "Organic market", "145.80", now.minusDays(11), charlie),
                createTx("DEBIT", "HEALTHCARE", "Dental checkup", "250.00", now.minusDays(9), charlie),
                createTx("DEBIT", "SHOPPING", "New laptop accessories", "189.99", now.minusDays(5), charlie),
                createTx("CREDIT", "INVESTMENT", "Dividend payout", "320.00", now.minusDays(2), charlie)
            ));

            // ===== User 4: Diana Prince =====
            // Top earner, mortgage, active spender, freelance side income
            User diana = users.get(3);
            bankAccountRepository.save(
                createAccount("4567890123", "CHECKING", "GB15MIDL40051512345678", "15800.00", diana)
            );
            creditCardRepository.save(
                createCard("4444444444444444", "11/2027", "15000.00", "4200.00", diana)
            );
            transactionRepository.saveAll(List.of(
                createTx("CREDIT", "SALARY", "Monthly salary", "7500.00", now.minusDays(17), diana),
                createTx("DEBIT", "MORTGAGE", "Mortgage payment", "2200.00", now.minusDays(16), diana),
                createTx("DEBIT", "GROCERIES", "Weekly groceries", "95.60", now.minusDays(12), diana),
                createTx("DEBIT", "UTILITIES", "Gas and water bill", "145.00", now.minusDays(10), diana),
                createTx("DEBIT", "TRANSPORT", "Monthly train pass", "180.00", now.minusDays(8), diana),
                createTx("DEBIT", "DINING", "Birthday dinner", "125.00", now.minusDays(4), diana),
                createTx("DEBIT", "SHOPPING", "Winter clothing", "340.00", now.minusDays(2), diana),
                createTx("CREDIT", "FREELANCE", "Consulting fee", "1500.00", now.minusDays(1), diana)
            ));

            // ===== User 5: Edward Davis =====
            // Lower income, near credit limit, budget-conscious
            User edward = users.get(4);
            bankAccountRepository.saveAll(List.of(
                createAccount("5678901234", "CHECKING", "GB33BUKB20041538290008", "2100.00", edward),
                createAccount("5678901235", "SAVINGS", "GB33BUKB20041538290009", "8500.00", edward)
            ));
            creditCardRepository.save(
                createCard("4555555555555555", "08/2028", "4000.00", "3750.00", edward)
            );
            transactionRepository.saveAll(List.of(
                createTx("CREDIT", "SALARY", "Monthly salary", "3200.00", now.minusDays(15), edward),
                createTx("DEBIT", "RENT", "Studio apartment rent", "900.00", now.minusDays(14), edward),
                createTx("DEBIT", "GROCERIES", "Grocery delivery", "67.20", now.minusDays(9), edward),
                createTx("DEBIT", "ENTERTAINMENT", "Concert tickets", "150.00", now.minusDays(7), edward),
                createTx("DEBIT", "UTILITIES", "Internet bill", "55.00", now.minusDays(3), edward)
            ));

            // ===== User 6: Fiona Clark =====
            // Solid earner, homeowner, two cards with low utilisation
            User fiona = users.get(5);
            bankAccountRepository.save(
                createAccount("6789012345", "CHECKING", "GB09HAFC40273612345678", "11200.00", fiona)
            );
            creditCardRepository.saveAll(List.of(
                createCard("4666666666666666", "02/2029", "12000.00", "1850.00", fiona),
                createCard("5777777777777777", "05/2027", "5000.00", "420.00", fiona)
            ));
            transactionRepository.saveAll(List.of(
                createTx("CREDIT", "SALARY", "Monthly salary", "5800.00", now.minusDays(16), fiona),
                createTx("DEBIT", "MORTGAGE", "Mortgage payment", "1650.00", now.minusDays(15), fiona),
                createTx("DEBIT", "GROCERIES", "Farmers market", "92.40", now.minusDays(11), fiona),
                createTx("DEBIT", "HEALTHCARE", "Gym membership", "45.00", now.minusDays(8), fiona),
                createTx("DEBIT", "DINING", "Brunch with friends", "52.00", now.minusDays(6), fiona),
                createTx("DEBIT", "SHOPPING", "Home decor items", "215.00", now.minusDays(3), fiona),
                createTx("CREDIT", "FREELANCE", "Photography gig", "600.00", now.minusDays(1), fiona)
            ));

            // ===== User 7: George Wilson =====
            // Mid-range earner, renter, solid savings, moderate spender
            User george = users.get(6);
            bankAccountRepository.saveAll(List.of(
                createAccount("7890123456", "CHECKING", "GB72HBUK40127612345698", "4600.00", george),
                createAccount("7890123457", "SAVINGS", "GB72HBUK40127612345699", "18000.00", george)
            ));
            creditCardRepository.save(
                createCard("4777777777777777", "01/2029", "7000.00", "980.00", george)
            );
            transactionRepository.saveAll(List.of(
                createTx("CREDIT", "SALARY", "Monthly salary", "5000.00", now.minusDays(15), george),
                createTx("DEBIT", "RENT", "Apartment rent", "1350.00", now.minusDays(14), george),
                createTx("DEBIT", "GROCERIES", "Bulk store shopping", "210.00", now.minusDays(10), george),
                createTx("DEBIT", "TRANSPORT", "Car insurance premium", "120.00", now.minusDays(8), george),
                createTx("DEBIT", "ENTERTAINMENT", "Video game purchase", "69.99", now.minusDays(5), george),
                createTx("DEBIT", "SUBSCRIPTION", "Cloud storage plan", "9.99", now.minusDays(2), george)
            ));

            // ===== User 8: Hannah Moore =====
            // Moderate earner, shared flat, tutoring side income
            User hannah = users.get(7);
            bankAccountRepository.save(
                createAccount("8901234567", "CHECKING", "GB48ABBY09012312345678", "6750.00", hannah)
            );
            creditCardRepository.save(
                createCard("4888888888888888", "07/2028", "6000.00", "2340.00", hannah)
            );
            transactionRepository.saveAll(List.of(
                createTx("CREDIT", "SALARY", "Monthly salary", "4200.00", now.minusDays(16), hannah),
                createTx("DEBIT", "RENT", "Shared flat rent", "750.00", now.minusDays(15), hannah),
                createTx("DEBIT", "GROCERIES", "Weekly shop", "78.90", now.minusDays(12), hannah),
                createTx("DEBIT", "UTILITIES", "Phone bill", "35.00", now.minusDays(9), hannah),
                createTx("DEBIT", "ENTERTAINMENT", "Museum membership", "85.00", now.minusDays(6), hannah),
                createTx("DEBIT", "DINING", "Takeaway order", "32.50", now.minusDays(4), hannah),
                createTx("CREDIT", "FREELANCE", "Tutoring sessions", "450.00", now.minusDays(2), hannah),
                createTx("DEBIT", "SHOPPING", "Running shoes", "129.00", now.minusDays(1), hannah)
            ));

            // ===== User 9: Isaac Taylor =====
            // High earner, homeowner, investment income, two credit cards
            User isaac = users.get(8);
            bankAccountRepository.saveAll(List.of(
                createAccount("9012345678", "CHECKING", "GB62BARC20035344773456", "9200.00", isaac),
                createAccount("9012345679", "SAVINGS", "GB62BARC20035344773457", "32000.00", isaac)
            ));
            creditCardRepository.saveAll(List.of(
                createCard("4999999999999999", "04/2028", "10000.00", "1550.00", isaac),
                createCard("5111111111111111", "10/2029", "8000.00", "3200.00", isaac)
            ));
            transactionRepository.saveAll(List.of(
                createTx("CREDIT", "SALARY", "Monthly salary", "6800.00", now.minusDays(15), isaac),
                createTx("DEBIT", "MORTGAGE", "Mortgage payment", "1900.00", now.minusDays(14), isaac),
                createTx("DEBIT", "GROCERIES", "Online grocery order", "135.60", now.minusDays(10), isaac),
                createTx("DEBIT", "UTILITIES", "Electricity and gas", "180.00", now.minusDays(8), isaac),
                createTx("DEBIT", "TRANSPORT", "Fuel costs", "75.00", now.minusDays(5), isaac),
                createTx("CREDIT", "INVESTMENT", "Stock dividends", "420.00", now.minusDays(3), isaac),
                createTx("DEBIT", "DINING", "Anniversary dinner", "185.00", now.minusDays(1), isaac)
            ));

            // ===== User 10: Julia Anderson =====
            // Mid earner, city renter, graphic design side hustle
            User julia = users.get(9);
            bankAccountRepository.save(
                createAccount("0123456789", "CHECKING", "GB29RBOS83040210126939", "7400.00", julia)
            );
            creditCardRepository.save(
                createCard("4000000000000000", "12/2027", "9000.00", "5100.00", julia)
            );
            transactionRepository.saveAll(List.of(
                createTx("CREDIT", "SALARY", "Monthly salary", "5500.00", now.minusDays(16), julia),
                createTx("DEBIT", "RENT", "City centre flat rent", "1400.00", now.minusDays(15), julia),
                createTx("DEBIT", "GROCERIES", "Weekly groceries", "110.00", now.minusDays(11), julia),
                createTx("DEBIT", "HEALTHCARE", "Prescription medication", "45.00", now.minusDays(9), julia),
                createTx("DEBIT", "TRANSPORT", "Monthly bus pass", "65.00", now.minusDays(7), julia),
                createTx("DEBIT", "ENTERTAINMENT", "Theatre tickets", "95.00", now.minusDays(4), julia),
                createTx("CREDIT", "FREELANCE", "Graphic design project", "950.00", now.minusDays(2), julia),
                createTx("DEBIT", "SUBSCRIPTION", "Software subscriptions", "49.99", now.minusDays(1), julia)
            ));

            System.out.println("Seeded " + users.size() + " users with financial data.");
        };
    }

    // --- Helper methods to reduce repetition in seed data ---
    // These create entity objects with the user relationship set, ready for saving.

    /**
     * Creates a BankAccount entity linked to the given user.
     * Uses String parameters for BigDecimal values to avoid floating-point constructor pitfalls.
     * new BigDecimal("5420.50") is exact; new BigDecimal(5420.50) can introduce rounding errors.
     */
    private BankAccount createAccount(String accountNumber, String accountType,
                                      String iban, String balance, User user) {
        BankAccount account = new BankAccount(accountNumber, accountType, iban, new BigDecimal(balance));
        account.setUser(user);
        return account;
    }

    /**
     * Creates a CreditCard entity linked to the given user.
     */
    private CreditCard createCard(String cardNumber, String expiryDate,
                                  String creditLimit, String balance, User user) {
        CreditCard card = new CreditCard(cardNumber, expiryDate, new BigDecimal(creditLimit), new BigDecimal(balance));
        card.setUser(user);
        return card;
    }

    /**
     * Creates a Transaction entity linked to the given user.
     * Shorthand alias "Tx" used in the method name for readability in the seed data above.
     */
    private Transaction createTx(String type, String category, String description,
                                 String amount, LocalDateTime timestamp, User user) {
        Transaction tx = new Transaction(type, category, description, new BigDecimal(amount), timestamp);
        tx.setUser(user);
        return tx;
    }
}
