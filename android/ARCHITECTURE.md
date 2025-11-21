# Architecture Documentation

## Overview

This application follows **Clean Architecture** principles with a clear separation between layers, ensuring maintainability, testability, and scalability.

## Layer Structure

### 1. Presentation Layer (`ui/`)

**Responsibility**: Display data and handle user interactions

**Components**:
- **Composables** - UI components built with Jetpack Compose
- **ViewModels** - Manage UI state and business logic
- **UI States** - Sealed classes representing screen states

**Example**:
```kotlin
@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val repository: AccountDataSource
) : ViewModel() {
    
    private val _accounts = MutableStateFlow<List<Account>>(emptyList())
    val accounts: StateFlow<List<Account>> = _accounts
    
    fun fetchAccounts() {
        viewModelScope.launch {
            when (val result = repository.getAccounts()) {
                is NetworkResult.Success -> {
                    _accounts.value = result.data?.map { it.toDomain() } ?: emptyList()
                }
                is NetworkResult.Error -> {
                    // Handle error
                }
            }
        }
    }
}
```

### 2. Domain Layer (`domain/`)

**Responsibility**: Business logic and domain models

**Components**:
- **Models** - Pure Kotlin data classes representing business entities
- **No dependencies** on Android framework or external libraries

**Example**:
```kotlin
data class Account(
    val id: Long = 0,
    val name: String,
    val type: AccountType,
    val balance: Double,
    val color: Long
)
```

### 3. Data Layer (`data/`)

**Responsibility**: Data management and external communication

**Components**:

#### a. Repository Interfaces (`data/repository/`)
Define contracts for data operations:
```kotlin
interface AccountDataSource {
    suspend fun getAccounts(): NetworkResult<List<AccountDto>>
    suspend fun createAccount(request: AccountRequest): NetworkResult<AccountDto>
}
```

#### b. Repository Implementations (`data/remote/`)
Concrete implementations using Retrofit:
```kotlin
class AccountRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : BaseRepository(), AccountDataSource {
    
    override suspend fun getAccounts(): NetworkResult<List<AccountDto>> =
        withContext(Dispatchers.IO) {
            safeApiCall { apiService.getAccounts() }
        }
}
```

#### c. DTOs (`data/remote/dto/`)
Data Transfer Objects for API communication:
```kotlin
data class AccountDto(
    val id: Long,
    val userId: Long,
    val name: String,
    val type: AccountType,
    val balance: Double,
    val color: Long,
    val createdAt: String,
    val updatedAt: String
)
```

#### d. Mappers (`data/mapper/`)
Convert between DTOs and Domain models:
```kotlin
fun AccountDto.toDomain(): Account {
    return Account(
        id = id,
        name = name,
        type = type,
        balance = balance,
        color = color
    )
}
```

#### e. Local Database (`data/local/`)
Room database for offline storage:
- **Entities** - Room database tables
- **DAOs** - Data Access Objects
- **Database** - Room database instance

### 4. Network Layer (`network/`)

**Responsibility**: Network utilities and error handling

**Components**:

#### NetworkResult
Sealed class for API response states:
```kotlin
sealed class NetworkResult<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : NetworkResult<T>(data)
    class Error<T>(message: String, data: T? = null) : NetworkResult<T>(data, message)
    class Loading<T> : NetworkResult<T>()
}
```

#### BaseRepository
Centralized error handling:
```kotlin
abstract class BaseRepository {
    protected suspend fun <T> safeApiCall(apiCall: suspend () -> T): NetworkResult<T> {
        return try {
            val response = apiCall()
            NetworkResult.Success(response)
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "An unknown error occurred")
        }
    }
}
```

### 5. Dependency Injection (`di/`)

**Responsibility**: Provide dependencies using Hilt

**Modules**:

#### NetworkModule
Provides network-related dependencies:
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit
    
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService
}
```

#### RepositoryModule
Binds DataSource interfaces to implementations:
```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAccountDataSource(
        accountRepositoryImpl: AccountRepositoryImpl
    ): AccountDataSource
}
```

#### AppModule
Provides database and DAO dependencies:
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideAppDatabase(app: Application): AppDatabase
}
```

## Data Flow

### Read Operation Flow

```
1. User interacts with UI (Composable)
2. Composable calls ViewModel method
3. ViewModel calls DataSource method
4. Repository Implementation (extends BaseRepository):
   - Calls safeApiCall
   - Makes Retrofit API call
   - Returns NetworkResult
5. ViewModel processes NetworkResult:
   - Success: Maps DTO to Domain model using mapper
   - Error: Updates error state
6. ViewModel updates StateFlow
7. Composable observes StateFlow and updates UI
```

### Write Operation Flow

```
1. User submits form in UI
2. Composable calls ViewModel method with data
3. ViewModel creates Request DTO
4. ViewModel calls DataSource create/update method
5. Repository Implementation:
   - Calls safeApiCall with Request DTO
   - Returns NetworkResult
6. ViewModel processes result:
   - Success: Refreshes data by calling fetch
   - Error: Updates error state
7. UI reflects updated state
```

## Design Patterns

### 1. Repository Pattern
Abstracts data sources behind interfaces, allowing easy swapping of implementations.

### 2. Dependency Injection
Uses Hilt for compile-time dependency injection, improving testability.

### 3. MVVM (Model-View-ViewModel)
- **Model**: Domain models and data layer
- **View**: Composables
- **ViewModel**: Manages UI state and business logic

### 4. Sealed Classes
Used for:
- UI states (Loading, Success, Error)
- Navigation routes
- Network results

### 5. State Management
- **StateFlow** for reactive state updates
- **MutableStateFlow** for internal state management
- **Flow** for data streams

## Error Handling

### Network Errors
Handled by `BaseRepository.safeApiCall`:
- Catches exceptions
- Wraps in `NetworkResult.Error`
- Provides error messages to UI

### UI Error Display
ViewModels expose error states:
```kotlin
sealed class AccountsUiState {
    object Idle : AccountsUiState()
    object Loading : AccountsUiState()
    object Success : AccountsUiState()
    data class Error(val message: String) : AccountsUiState()
}
```

## Testing Strategy

### Unit Tests
- **ViewModels**: Test business logic with mocked repositories
- **Repositories**: Test data transformations
- **Mappers**: Test DTO to Domain conversions

### Integration Tests
- **API Integration**: Test Retrofit service with mock server
- **Database**: Test Room DAOs with in-memory database

### UI Tests
- **Composables**: Test UI rendering and interactions
- **Navigation**: Test screen transitions

## Best Practices

1. **Single Responsibility**: Each class has one clear purpose
2. **Dependency Inversion**: Depend on abstractions (interfaces), not concretions
3. **Immutability**: Use `val` and immutable data classes
4. **Null Safety**: Leverage Kotlin's null safety features
5. **Coroutines**: Use structured concurrency with viewModelScope
6. **Resource Management**: Use `withContext` for IO operations
7. **Type Safety**: Use sealed classes for finite states

## Performance Considerations

1. **Lazy Loading**: Load data on demand
2. **Caching**: Room database for offline support
3. **Pagination**: Implement for large datasets (future enhancement)
4. **Image Loading**: Use Coil for efficient image loading (if needed)
5. **Background Processing**: Use Dispatchers.IO for network/database operations

## Security

1. **Token Storage**: EncryptedSharedPreferences for JWT tokens
2. **API Security**: JWT authentication on all endpoints
3. **Input Validation**: Validate user input before API calls
4. **ProGuard**: Code obfuscation in release builds
