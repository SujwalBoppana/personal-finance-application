# Personal Finance Android Application

A modern Android application for personal finance management built with Clean Architecture principles, Jetpack Compose, and Kotlin.

## Features

- 📊 **Dashboard** - Overview of financial status with today's and monthly income/expense
- 💰 **Accounts** - Manage multiple accounts (Cash, Bank, Card, Wallet)
- 📝 **Transactions** - Track income and expenses with categories
- 🎯 **Budgets** - Set and monitor monthly budgets by category
- 📈 **Analytics** - Visualize spending patterns with charts and insights
- 🔒 **Security** - JWT authentication with encrypted local storage

## Architecture

This application follows **Clean Architecture** principles with clear separation of concerns:

```
app/
├── data/                    # Data Layer
│   ├── local/              # Room database, DAOs, entities
│   ├── remote/             # Retrofit API, DTOs, repository implementations
│   ├── repository/         # DataSource interfaces
│   └── mapper/             # DTO ↔ Domain mappers
├── domain/                 # Domain Layer
│   └── model/              # Domain models (Account, Transaction, etc.)
├── network/                # Network utilities
│   ├── NetworkResult       # Sealed class for API responses
│   └── BaseRepository      # Base class with safeApiCall
├── di/                     # Dependency Injection (Hilt modules)
└── ui/                     # Presentation Layer
    ├── accounts/           # Account management screens
    ├── transactions/       # Transaction screens
    ├── budgets/            # Budget screens
    ├── analytics/          # Analytics screens
    ├── dashboard/          # Dashboard screen
    └── navigation/         # Navigation graph
```

## Tech Stack

### Core
- **Kotlin** - Primary programming language
- **Jetpack Compose** - Modern declarative UI framework
- **Coroutines & Flow** - Asynchronous programming

### Architecture & DI
- **Hilt** - Dependency injection
- **ViewModel** - UI state management
- **Navigation Compose** - Type-safe navigation

### Data & Networking
- **Room** - Local database
- **Retrofit** - REST API client
- **OkHttp** - HTTP client with logging
- **Gson** - JSON serialization
- **Security Crypto** - Encrypted SharedPreferences for tokens

### Backend Integration
- **Spring Boot REST API** - Backend server
- **MySQL** - Database
- **JWT** - Authentication

## Getting Started

### Prerequisites

- Android Studio Hedgehog or later
- JDK 17 or later
- Android SDK 34
- Kotlin 1.9.20

### Backend Setup

1. Start the backend server (see backend documentation)
2. Ensure MySQL is running
3. Backend should be accessible at `http://localhost:8080`

### Android Setup

1. Clone the repository:
```bash
git clone <repository-url>
cd personal-finance-android
```

2. Open the project in Android Studio

3. Sync Gradle dependencies

4. Update the API base URL if needed in `NetworkModule.kt`:
```kotlin
private const val BASE_URL = "http://10.0.2.2:8080/api/" // For emulator
// Use "http://<your-ip>:8080/api/" for physical device
```

5. Build and run:
```bash
./gradlew assembleDebug
./gradlew installDebug
```

Or use Android Studio's Run button.

## Project Structure

### Data Flow

```
UI Layer (Compose) 
    ↓ observes StateFlow
ViewModel 
    ↓ calls
DataSource Interface 
    ↓ implemented by
Repository Implementation (extends BaseRepository)
    ↓ uses
Retrofit ApiService 
    ↓ returns
DTOs 
    ↓ mapped to
Domain Models
```

### Key Components

#### NetworkResult
Sealed class for consistent API response handling:
```kotlin
sealed class NetworkResult<T> {
    class Success<T>(data: T) : NetworkResult<T>(data)
    class Error<T>(message: String) : NetworkResult<T>(data, message)
    class Loading<T> : NetworkResult<T>()
}
```

#### BaseRepository
Provides centralized error handling:
```kotlin
protected suspend fun <T> safeApiCall(apiCall: suspend () -> T): NetworkResult<T>
```

#### Mappers
Convert DTOs to Domain models:
```kotlin
fun AccountDto.toDomain(): Account
fun TransactionDto.toDomain(): Transaction
```

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login user

### Accounts
- `GET /api/accounts` - Get all accounts
- `GET /api/accounts/{id}` - Get account by ID
- `POST /api/accounts` - Create account
- `PUT /api/accounts/{id}` - Update account
- `DELETE /api/accounts/{id}` - Delete account

### Transactions
- `GET /api/transactions` - Get all transactions
- `GET /api/transactions/{id}` - Get transaction by ID
- `POST /api/transactions` - Create transaction
- `PUT /api/transactions/{id}` - Update transaction
- `DELETE /api/transactions/{id}` - Delete transaction

### Budgets
- `GET /api/budgets` - Get all budgets
- `POST /api/budgets` - Create budget
- `PUT /api/budgets/{id}` - Update budget
- `DELETE /api/budgets/{id}` - Delete budget

### Categories
- `GET /api/categories` - Get all categories
- `POST /api/categories` - Create category
- `PUT /api/categories/{id}` - Update category
- `DELETE /api/categories/{id}` - Delete category

## Configuration

### Build Variants
- **Debug** - Development build with logging enabled
- **Release** - Production build with ProGuard

### Dependencies
See [libs.versions.toml](gradle/libs.versions.toml) for version catalog.

## Testing

### Unit Tests
```bash
./gradlew test
```

### Instrumented Tests
```bash
./gradlew connectedAndroidTest
```

## Security

- **JWT Tokens** - Stored in EncryptedSharedPreferences
- **HTTPS** - Use HTTPS in production
- **ProGuard** - Code obfuscation in release builds

## Contributing

1. Create a feature branch
2. Make your changes
3. Write/update tests
4. Submit a pull request

## License

[Your License Here]

## Contact

[Your Contact Information]
