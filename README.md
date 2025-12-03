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

## Database Configuration

### MySQL Setup

1. Create a MySQL database:
```sql
CREATE DATABASE expense_tracker;
```

2. Update `application.properties` with your MySQL credentials:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/expense_tracker?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - User login

### Expenses
- `POST /api/expenses` - Create expense
- `GET /api/expenses` - Get all expenses
- `GET /api/expenses/{id}` - Get expense by ID
- `GET /api/expenses/category/{category}` - Get expenses by category
- `GET /api/expenses/date-range` - Get expenses by date range
- `GET /api/expenses/total` - Get total expenses
- `PUT /api/expenses/{id}` - Update expense
- `DELETE /api/expenses/{id}` - Delete expense

### Budgets
- `POST /api/budgets` - Create budget
- `GET /api/budgets` - Get all budgets
- `GET /api/budgets/{id}` - Get budget by ID
- `GET /api/budgets/period` - Get budgets by month/year
- `PUT /api/budgets/{id}` - Update budget
- `DELETE /api/budgets/{id}` - Delete budget

### Reports
- `GET /api/reports/monthly/pdf` - Generate monthly PDF report
- `GET /api/reports/yearly/pdf` - Generate yearly PDF report
- `GET /api/reports/chart/pie` - Generate pie chart
- `GET /api/reports/expenses-by-category` - Get expenses by category

## Expense Categories

- FOOD_AND_DINING
- TRANSPORTATION
- HOUSING
- UTILITIES
- HEALTHCARE
- ENTERTAINMENT
- SHOPPING
- EDUCATION
- TRAVEL
- INSURANCE
- SAVINGS
- PERSONAL_CARE
- GIFTS_AND_DONATIONS
- BUSINESS
- OTHER

## Building and Running

### Prerequisites
- Java 20 or higher
- Maven 4.0.0 or higher
- MySQL 8.0 or higher

### Build the application
```bash
mvn clean install
```

### Run the application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Run tests
```bash
mvn test
```

## Security

- All API endpoints (except `/api/auth/**`) require JWT authentication
- Include the JWT token in the Authorization header: `Bearer <token>`
- Passwords are encrypted using BCrypt
- CORS is configured for development (localhost:3000, localhost:5173)

## JWT Configuration

Update the JWT secret in `application.properties`:
```properties
jwt.secret=your-secret-key
jwt.expiration=86400000  # 24 hours in milliseconds
```

## Testing

The application includes:
- Unit tests for service layer
- Integration tests for controllers
- H2 in-memory database for testing

## Frontend Integration

The backend is ready for frontend integration. CORS is configured to accept requests from:
- http://localhost:3000 (React default)
- http://localhost:5173 (Vite default)

Update CORS configuration in `SecurityConfig.java` to add more allowed origins.

## License

This project is for educational purposes.
