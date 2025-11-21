# API Integration Guide

## Overview

This document describes how the Android application integrates with the Spring Boot backend API.

## Base Configuration

### Network Module Setup

The API base URL is configured in `NetworkModule.kt`:

```kotlin
private const val BASE_URL = "http://10.0.2.2:8080/api/"
```

**Important**: 
- `10.0.2.2` is the special IP for Android emulator to access host machine's localhost
- For physical devices, use your computer's IP address: `http://<YOUR_IP>:8080/api/`
- For production, use HTTPS: `https://api.yourapp.com/api/`

## Authentication Flow

### 1. Registration

**Endpoint**: `POST /api/auth/register`

**Request**:
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Response**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "email": "user@example.com",
    "createdAt": "2024-01-01T00:00:00",
    "updatedAt": "2024-01-01T00:00:00"
  }
}
```

**Android Implementation**:
```kotlin
// AuthRepositoryImpl.kt
override suspend fun register(email: String, password: String): NetworkResult<AuthResponse> =
    withContext(Dispatchers.IO) {
        safeApiCall {
            val response = apiService.register(RegisterRequest(email, password))
            tokenManager.saveToken(response.token)
            response
        }
    }
```

### 2. Login

**Endpoint**: `POST /api/auth/login`

**Request/Response**: Same as registration

### 3. Token Management

Tokens are stored securely using `EncryptedSharedPreferences`:

```kotlin
// TokenManager.kt
fun saveToken(token: String)
fun getToken(): String?
fun clearToken()
fun isLoggedIn(): Boolean
```

### 4. Authenticated Requests

All API requests automatically include the JWT token via `AuthInterceptor`:

```kotlin
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenManager.getToken()
        val newRequest = if (token != null) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            chain.request()
        }
        return chain.proceed(newRequest)
    }
}
```

## API Endpoints

### Accounts

#### Get All Accounts
```
GET /api/accounts
Authorization: Bearer <token>

Response: 200 OK
[
  {
    "id": 1,
    "userId": 1,
    "name": "Main Checking",
    "type": "BANK",
    "balance": 1500.00,
    "color": 4283215696,
    "createdAt": "2024-01-01T00:00:00",
    "updatedAt": "2024-01-01T00:00:00"
  }
]
```

#### Create Account
```
POST /api/accounts
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "Savings Account",
  "type": "BANK",
  "balance": 5000.00,
  "color": 4278255360
}

Response: 201 Created
{
  "id": 2,
  "userId": 1,
  "name": "Savings Account",
  ...
}
```

#### Update Account
```
PUT /api/accounts/{id}
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "Updated Name",
  "type": "BANK",
  "balance": 5500.00,
  "color": 4278255360
}
```

#### Delete Account
```
DELETE /api/accounts/{id}
Authorization: Bearer <token>

Response: 204 No Content
```

### Transactions

#### Get All Transactions
```
GET /api/transactions?accountId={accountId}&type={type}
Authorization: Bearer <token>

Query Parameters:
- accountId (optional): Filter by account
- type (optional): INCOME, EXPENSE, or TRANSFER

Response: 200 OK
[
  {
    "id": 1,
    "userId": 1,
    "accountId": 1,
    "amount": 50.00,
    "category": "Groceries",
    "date": "2024-01-15T10:30:00",
    "note": "Weekly shopping",
    "type": "EXPENSE",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  }
]
```

#### Create Transaction
```
POST /api/transactions
Authorization: Bearer <token>
Content-Type: application/json

{
  "accountId": 1,
  "amount": 75.50,
  "category": "Dining",
  "date": "2024-01-16T19:00:00",
  "note": "Dinner with friends",
  "type": "EXPENSE"
}
```

### Budgets

#### Get Budgets
```
GET /api/budgets?month={month}&year={year}
Authorization: Bearer <token>

Query Parameters:
- month (optional): 1-12
- year (optional): e.g., 2024

Response: 200 OK
[
  {
    "id": 1,
    "userId": 1,
    "category": "Groceries",
    "amount": 500.00,
    "month": 1,
    "year": 2024,
    "createdAt": "2024-01-01T00:00:00",
    "updatedAt": "2024-01-01T00:00:00"
  }
]
```

#### Create Budget
```
POST /api/budgets
Authorization: Bearer <token>
Content-Type: application/json

{
  "category": "Entertainment",
  "amount": 200.00,
  "month": 1,
  "year": 2024
}
```

### Categories

#### Get Categories
```
GET /api/categories?type={type}
Authorization: Bearer <token>

Query Parameters:
- type (optional): INCOME or EXPENSE

Response: 200 OK
[
  {
    "id": 1,
    "userId": 1,
    "name": "Groceries",
    "icon": "shopping_cart",
    "type": "EXPENSE",
    "createdAt": "2024-01-01T00:00:00",
    "updatedAt": "2024-01-01T00:00:00"
  }
]
```

## Data Mapping

### DTO to Domain Conversion

DTOs from the API are mapped to Domain models:

```kotlin
// Mappers.kt
fun TransactionDto.toDomain(): Transaction {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val parsedDate = try {
        dateFormat.parse(date) ?: Date()
    } catch (e: Exception) {
        Date()
    }

    return Transaction(
        id = id,
        amount = amount,
        category = category,
        date = parsedDate,
        accountId = accountId,
        note = note,
        type = type
    )
}
```

### Domain to Request Conversion

When creating/updating, Domain models are converted to Request DTOs:

```kotlin
// In ViewModel
val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
val dateString = dateFormat.format(Date())

val request = TransactionRequest(
    amount = amount,
    category = category,
    date = dateString,
    accountId = accountId,
    note = note,
    type = type
)
```

## Error Handling

### Network Errors

All API calls are wrapped in `safeApiCall`:

```kotlin
when (val result = repository.getAccounts()) {
    is NetworkResult.Success -> {
        // Handle success
        val accounts = result.data?.map { it.toDomain() } ?: emptyList()
    }
    is NetworkResult.Error -> {
        // Handle error
        val errorMessage = result.message ?: "Unknown error"
    }
    is NetworkResult.Loading -> {
        // Show loading state
    }
}
```

### Common HTTP Status Codes

- **200 OK** - Successful GET request
- **201 Created** - Successful POST request
- **204 No Content** - Successful DELETE request
- **400 Bad Request** - Invalid request data
- **401 Unauthorized** - Missing or invalid token
- **404 Not Found** - Resource not found
- **500 Internal Server Error** - Server error

## Testing API Integration

### Using Swagger UI

The backend provides Swagger UI at:
```
http://localhost:8080/swagger-ui.html
```

### Manual Testing with cURL

```bash
# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123"}'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password123"}'

# Get Accounts (with token)
curl -X GET http://localhost:8080/api/accounts \
  -H "Authorization: Bearer <your-token>"
```

## Troubleshooting

### Cannot Connect to Backend

1. **Emulator**: Ensure using `10.0.2.2` instead of `localhost`
2. **Physical Device**: Use computer's IP address on same network
3. **Firewall**: Check if port 8080 is accessible
4. **Backend**: Verify backend is running on port 8080

### 401 Unauthorized

1. Check if token is being saved after login
2. Verify token is included in request headers
3. Check token expiration (JWT tokens may expire)

### Date Parsing Issues

Ensure date format matches backend:
```kotlin
SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
```

### Network Timeout

Adjust timeout in `NetworkModule.kt`:
```kotlin
.connectTimeout(30, TimeUnit.SECONDS)
.readTimeout(30, TimeUnit.SECONDS)
.writeTimeout(30, TimeUnit.SECONDS)
```
