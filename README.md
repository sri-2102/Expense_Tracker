# Expense Tracker Application

A comprehensive Spring Boot application for money calculation and expense tracking with authentication, budgeting, and reporting features.

## Technology Stack

- **Backend**: Spring Boot 3.1.4
- **Java Version**: Java 20
- **Build Tool**: Maven 4.0.0
- **Database**: MySQL
- **Security**: Spring Security with JWT authentication
- **PDF Generation**: iText 7
- **Charts**: JFreeChart

## Features

### Authentication & Authorization
- User registration and login
- JWT-based authentication
- Role-based access control (USER, ADMIN)
- Password encryption with BCrypt

### Expense Management
- Create, read, update, and delete expenses
- Categorize expenses (15+ categories)
- Filter expenses by date range and category
- Track expense history

### Budget Management
- Set monthly budget limits by category
- Automatic budget limit notifications
- Track budget vs. actual spending

### Reports & Analytics
- Generate monthly and yearly PDF reports
- Export expense data to PDF
- Visualize expenses with pie charts
- View expenses by category

## Project Structure

```
src/
├── main/
│   ├── java/com/expense/tracker/
│   │   ├── controller/         # REST API controllers
│   │   │   ├── AuthController.java
│   │   │   ├── ExpenseController.java
│   │   │   ├── BudgetController.java
│   │   │   └── ReportController.java
│   │   ├── dto/               # Data Transfer Objects
│   │   │   ├── RegisterRequest.java
│   │   │   ├── LoginRequest.java
│   │   │   ├── AuthResponse.java
│   │   │   ├── ExpenseRequest.java
│   │   │   ├── ExpenseResponse.java
│   │   │   ├── BudgetRequest.java
│   │   │   └── BudgetResponse.java
│   │   ├── entity/            # JPA entities
│   │   │   ├── User.java
│   │   │   ├── Expense.java
│   │   │   └── Budget.java
│   │   ├── repository/        # Data access layer
│   │   │   ├── UserRepository.java
│   │   │   ├── ExpenseRepository.java
│   │   │   └── BudgetRepository.java
│   │   ├── security/          # Security configuration
│   │   │   ├── SecurityConfig.java
│   │   │   ├── JwtTokenProvider.java
│   │   │   └── JwtAuthenticationFilter.java
│   │   ├── service/           # Business logic
│   │   │   ├── AuthService.java
│   │   │   ├── CustomUserDetailsService.java
│   │   │   ├── ExpenseService.java
│   │   │   ├── BudgetService.java
│   │   │   ├── ChartService.java
│   │   │   └── ReportService.java
│   │   └── ExpenseTrackerApplication.java
│   └── resources/
│       └── application.properties
└── test/
    ├── java/com/expense/tracker/
    │   ├── controller/
    │   │   └── ExpenseControllerTest.java
    │   └── service/
    │       └── ExpenseServiceTest.java
    └── resources/
        └── application-test.properties
```

