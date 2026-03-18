<img src="./datasite-logo.svg" width="225">

A Java REST API built with Spring Boot for take home interviews.

## 📋 For Datasite Engineers & Engineering Directors

> **Action required:** Before sharing this repository with a candidate, replace the **Take-Home Challenge** section below with the specific challenge you want the candidate to complete. Use the placeholder comments as a guide for what to include.

When defining a challenge, consider covering:

- **Context** — a brief description of the problem domain (e.g. a simplified version of a real Datasite workflow)
- **Requirements** — functional and non-functional requirements the candidate must address
- **Deliverables** — what the candidate should submit (e.g. a PR, a written design doc, a working prototype)
- **Evaluation criteria** — what you will assess (e.g. system design, code quality, scalability, trade-offs)
- **Time expectation** — a recommended time budget so candidates can scope their effort appropriately
- **Submission instructions** — how and where to submit (e.g. fork the repo and open a PR, email a zip, etc.)

Once the challenge is defined, remove this internal guidance block before sending the repo to the candidate.

---

## 🏗️ Take-Home Architecture Challenge — Senior Engineer

<!-- TODO (Engineering Director / Hiring Manager): Replace this section with your challenge brief. -->
<!-- Describe the problem, requirements, deliverables, evaluation criteria, time budget, and submission instructions. -->

> **[Challenge details to be added by the hiring team]**

---

## Tech Stack

- **Java 21**
- **Spring Boot 3.4.3**
- **Spring Data JPA** (Hibernate ORM)
- **H2** in-memory database
- **Maven** (via included Maven Wrapper)

## Prerequisites

- **Java 21** or later installed

Verify with:

```bash
java --version
```

You should see output like `openjdk 21.x.x`. No other dependencies are needed — Maven is included via the wrapper (`mvnw`).

## Getting Started

### 1. Clone the repository

```bash
git clone <repository-url>
cd Java-API
```

### 2. Build the project

```bash
./mvnw clean compile
```

This downloads Maven (first run only), resolves all dependencies, and compiles the source code. You should see `BUILD SUCCESS`.

### 3. Run the application

```bash
./mvnw spring-boot:run
```

The API starts on **http://localhost:8080**. You will see `Seeded 10 users with financial data.` in the console output confirming the database has been populated.

### 4. Verify it's working

Open your browser or run:

```bash
curl http://localhost:8080/
```

You should see a JSON response listing all available endpoints.

### 5. Stop the server

Press `Ctrl + C` in the terminal.

## API Endpoints

Visit `http://localhost:8080/` for a full interactive list. Summary below:

### Users

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users` | List all users (includes nested financial data) |
| GET | `/api/users/{id}` | Get a single user by ID |
| POST | `/api/users` | Create a new user |
| PUT | `/api/users/{id}` | Update a user |
| DELETE | `/api/users/{id}` | Delete a user (cascades to all financial data) |

### Bank Accounts

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users/{userId}/accounts` | List user's bank accounts |
| GET | `/api/users/{userId}/accounts/{id}` | Get a bank account by ID |
| POST | `/api/users/{userId}/accounts` | Create a bank account |
| PUT | `/api/users/{userId}/accounts/{id}` | Update a bank account |
| DELETE | `/api/users/{userId}/accounts/{id}` | Delete a bank account |

### Credit Cards

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users/{userId}/credit-cards` | List user's credit cards |
| GET | `/api/users/{userId}/credit-cards/{id}` | Get a credit card by ID |
| POST | `/api/users/{userId}/credit-cards` | Create a credit card |
| PUT | `/api/users/{userId}/credit-cards/{id}` | Update a credit card |
| DELETE | `/api/users/{userId}/credit-cards/{id}` | Delete a credit card |

### Transactions

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users/{userId}/transactions` | List user's transactions |
| GET | `/api/users/{userId}/transactions/{id}` | Get a transaction by ID |
| POST | `/api/users/{userId}/transactions` | Record a new transaction |
| DELETE | `/api/users/{userId}/transactions/{id}` | Delete a transaction |

No PUT for transactions — they are immutable by design.

### Financial Summary

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users/{userId}/financial-summary` | Calculated summary (net worth, credit score, monthly income/expenses) |

## Example Requests

### Create a user

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name": "Jane Doe", "email": "jane.doe@example.com"}'
```

### Add a bank account to user 1

```bash
curl -X POST http://localhost:8080/api/users/1/accounts \
  -H "Content-Type: application/json" \
  -d '{
    "accountNumber": "9876543210",
    "accountType": "SAVINGS",
    "iban": "GB00TEST00000000000000",
    "balance": 5000.00
  }'
```

### Add a credit card to user 1

```bash
curl -X POST http://localhost:8080/api/users/1/credit-cards \
  -H "Content-Type: application/json" \
  -d '{
    "cardNumber": "4000000000000001",
    "expiryDate": "06/2029",
    "creditLimit": 3000.00,
    "balance": 0.00
  }'
```

### Record a transaction for user 1

```bash
curl -X POST http://localhost:8080/api/users/1/transactions \
  -H "Content-Type: application/json" \
  -d '{
    "type": "DEBIT",
    "category": "GROCERIES",
    "description": "Weekly grocery shopping",
    "amount": 85.30,
    "timestamp": "2026-03-12T10:30:00"
  }'
```

### Get a user's financial summary

```bash
curl http://localhost:8080/api/users/1/financial-summary
```

Returns:

```json
{
  "totalBankBalance": 17420.50,
  "totalCreditCardDebt": 1230.45,
  "netWorth": 16190.05,
  "creditScore": 667,
  "monthlyIncome": 5300.00,
  "monthlyExpenses": 1404.30
}
```

## Seed Data

The application starts with 10 pre-loaded users, each with realistic financial data:

| User | Bank Accounts | Credit Cards | Transactions |
|------|:---:|:---:|:---:|
| Alice Johnson | 2 | 1 | 6 |
| Bob Smith | 1 | 2 | 7 |
| Charlie Brown | 2 | 1 | 6 |
| Diana Prince | 1 | 1 | 8 |
| Edward Davis | 2 | 1 | 5 |
| Fiona Clark | 1 | 2 | 7 |
| George Wilson | 2 | 1 | 6 |
| Hannah Moore | 1 | 1 | 8 |
| Isaac Taylor | 2 | 2 | 7 |
| Julia Anderson | 1 | 1 | 8 |

Data is stored in-memory (H2) and resets on every restart.

## Project Structure

```
src/main/java/com/example/userapi/
├── UserApiApplication.java           # Spring Boot entry point
├── config/
│   └── DataSeeder.java               # Seeds 10 users with financial data
├── controller/
│   ├── RootController.java           # GET / — endpoint listing
│   ├── UserController.java           # /api/users CRUD
│   ├── BankAccountController.java    # /api/users/{id}/accounts CRUD
│   ├── CreditCardController.java     # /api/users/{id}/credit-cards CRUD
│   ├── TransactionController.java    # /api/users/{id}/transactions CRD
│   └── FinancialSummaryController.java # /api/users/{id}/financial-summary
├── dto/
│   └── FinancialSummary.java         # Calculated summary (not stored)
├── model/
│   ├── User.java                     # User entity with @OneToMany relationships
│   ├── BankAccount.java              # Bank account entity
│   ├── CreditCard.java               # Credit card entity
│   └── Transaction.java              # Transaction entity
├── repository/
│   ├── UserRepository.java           # JPA repository for users
│   ├── BankAccountRepository.java    # JPA repository for accounts
│   ├── CreditCardRepository.java     # JPA repository for credit cards
│   └── TransactionRepository.java    # JPA repository for transactions
└── service/
    ├── UserService.java              # User business logic
    ├── BankAccountService.java       # Account business logic
    ├── CreditCardService.java        # Credit card business logic
    ├── TransactionService.java       # Transaction business logic
    └── FinancialSummaryService.java  # Calculates summary from live data
```

## H2 Database Console

While the app is running, you can inspect the database at:

```
http://localhost:8080/h2-console
```

Connection settings:
- **JDBC URL:** `jdbc:h2:mem:userdb`
- **Username:** `sa`
- **Password:** *(leave blank)*

## Useful Maven Commands

```bash
./mvnw clean compile        # Clean and compile
./mvnw spring-boot:run      # Run the application
./mvnw package              # Build a runnable JAR
java -jar target/user-api-1.0.0.jar  # Run the packaged JAR
```
