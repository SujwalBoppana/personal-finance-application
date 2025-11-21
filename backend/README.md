# Personal Finance Backend

A REST API backend built with Kotlin and Spring Boot for the Personal Finance Android application.

## Technology Stack

- **Language**: Kotlin 1.9.22
- **Framework**: Spring Boot 3.2.2
- **Database**: PostgreSQL 16
- **Security**: JWT (JSON Web Tokens)
- **Documentation**: Swagger/OpenAPI 3.0
- **Build Tool**: Gradle 8.5
- **Java Version**: 17

## Features

- **JWT Authentication**: Secure user registration and login
- **RESTful API**: Full CRUD operations for all resources
- **User-scoped Data**: Each user can only access their own data
- **Input Validation**: Request validation using Jakarta Validation
- **Error Handling**: Global exception handling with meaningful error messages
- **API Documentation**: Interactive Swagger UI
- **Docker Support**: Easy deployment with Docker and Docker Compose

## Prerequisites

- Java 17 or higher
- PostgreSQL 16 (or use Docker)
- Gradle 8.5 (or use the included Gradle wrapper)

## Getting Started

### Option 1: Run with Docker (Recommended)

1. **Build and start the services**:
   ```bash
   docker-compose up -d
   ```

2. **Access the application**:
   - API: http://localhost:8080
   - Swagger UI: http://localhost:8080/swagger-ui.html

3. **Stop the services**:
   ```bash
   docker-compose down
   ```

### Option 2: Run Locally

1. **Install PostgreSQL** and create a database:
   ```sql
   CREATE DATABASE financedb;
   ```

2. **Update database configuration** in `src/main/resources/application.yml`:
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/financedb
       username: your_username
       password: your_password
   ```

3. **Build the project**:
   ```bash
   ./gradlew build
   ```

4. **Run the application**:
   ```bash
   ./gradlew bootRun
   ```

5. **Access the application**:
   - API: http://localhost:8080
   - Swagger UI: http://localhost:8080/swagger-ui.html

## API Endpoints

### Authentication

- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login and get JWT token

### Accounts

- `GET /api/accounts` - Get all accounts
- `GET /api/accounts/{id}` - Get account by ID
- `POST /api/accounts` - Create new account
- `PUT /api/accounts/{id}` - Update account
- `DELETE /api/accounts/{id}` - Delete account

### Transactions

- `GET /api/transactions` - Get all transactions (supports filters: accountId, type)
- `GET /api/transactions/{id}` - Get transaction by ID
- `POST /api/transactions` - Create new transaction
- `PUT /api/transactions/{id}` - Update transaction
- `DELETE /api/transactions/{id}` - Delete transaction

### Budgets

- `GET /api/budgets` - Get all budgets (supports filters: month, year)
- `GET /api/budgets/{id}` - Get budget by ID
- `POST /api/budgets` - Create new budget
- `PUT /api/budgets/{id}` - Update budget
- `DELETE /api/budgets/{id}` - Delete budget

### Categories

- `GET /api/categories` - Get all categories (supports filter: type)
- `GET /api/categories/{id}` - Get category by ID
- `POST /api/categories` - Create new category
- `PUT /api/categories/{id}` - Update category
- `DELETE /api/categories/{id}` - Delete category

## Authentication

All endpoints except `/api/auth/**` require authentication. Include the JWT token in the Authorization header:

```
Authorization: Bearer <your_jwt_token>
```

## Environment Variables

You can override the default configuration using environment variables:

- `SPRING_DATASOURCE_URL` - Database URL
- `SPRING_DATASOURCE_USERNAME` - Database username
- `SPRING_DATASOURCE_PASSWORD` - Database password
- `JWT_SECRET` - JWT secret key (change in production!)
- `JWT_EXPIRATION` - JWT expiration time in milliseconds (default: 86400000 = 24 hours)
- `SERVER_PORT` - Server port (default: 8080)

## Database Schema

The application uses JPA/Hibernate with `ddl-auto: update`, which automatically creates and updates the database schema.

### Tables

- **users**: User accounts
- **accounts**: Financial accounts (cash, bank, card, etc.)
- **transactions**: Income and expense transactions
- **budgets**: Monthly budgets by category
- **categories**: Transaction categories

## Development

### Running Tests

```bash
./gradlew test
```

### Building for Production

```bash
./gradlew build
java -jar build/libs/personal-finance-backend-1.0.0.jar
```

## Security Considerations

**IMPORTANT**: Before deploying to production:

1. **Change the JWT secret** in `application.yml` to a strong, random 256-bit key
2. **Use environment variables** for sensitive configuration
3. **Enable HTTPS** for secure communication
4. **Configure CORS** to allow only your Android app's domain
5. **Set up proper database credentials** and restrict access

## API Documentation

Interactive API documentation is available at:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

## Troubleshooting

### Database Connection Issues

- Ensure PostgreSQL is running
- Check database credentials in `application.yml`
- Verify the database exists

### Port Already in Use

Change the server port in `application.yml`:
```yaml
server:
  port: 8081
```

### JWT Token Issues

- Ensure the JWT secret is properly configured
- Check token expiration time
- Verify the Authorization header format: `Bearer <token>`

## License

This project is part of the Personal Finance Android application.
