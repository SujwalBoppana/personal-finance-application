# System Architecture

## Overview

The Personal Finance Application follows a modern, scalable architecture with clear separation of concerns between the mobile client and backend server.

## High-Level Architecture

\\\

                     Mobile Client (Android)                  
    
                      Presentation Layer                    
           (Jetpack Compose UI + ViewModels)               
    
                                                             
    
                     Domain Layer                           
                (Use Cases + Models)                       
    
                                                             
    
                      Data Layer                            
          (Repositories + Remote/Local Sources)            
    

                        
                   HTTP/REST API
                        

                 Backend Server (Spring Boot)               
  
                   Controller Layer                       
              (REST API Endpoints)                        
  
                                                            
  
                    Service Layer                         
                (Business Logic)                          
  
                                                            
  
                  Repository Layer                        
              (Data Access Objects)                       
  

                        
                
                    Database    
                  (PostgreSQL)  
                
\\\

## Android Application Architecture

### MVVM + Clean Architecture

#### Presentation Layer
- **UI Components** (Jetpack Compose)
  - Screens: Full-page composables
  - Components: Reusable UI elements
  - Theme: Colors, typography, shapes
  
- **ViewModels**
  - Manage UI state
  - Handle user interactions
  - Coordinate with use cases
  - Expose Flow/LiveData to UI

\\\kotlin
@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val createTransactionUseCase: CreateTransactionUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<UiState<List<Transaction>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Transaction>>> = _uiState.asStateFlow()
    
    fun loadTransactions() {
        viewModelScope.launch {
            getTransactionsUseCase()
                .collect { result ->
                    _uiState.value = when (result) {
                        is NetworkResult.Success -> UiState.Success(result.data)
                        is NetworkResult.Error -> UiState.Error(result.message)
                        is NetworkResult.Loading -> UiState.Loading
                    }
                }
        }
    }
}
\\\

#### Domain Layer
- **Use Cases**
  - Single responsibility
  - Business logic coordination
  - Independent of frameworks

\\\kotlin
class GetTransactionsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(): Flow<NetworkResult<List<Transaction>>> =
        repository.getTransactions()
}
\\\

- **Domain Models**
  - Pure Kotlin data classes
  - Business entities
  - No framework dependencies

#### Data Layer
- **Repositories**
  - Abstract data sources
  - Implement domain interfaces
  - Handle data caching

\\\kotlin
class TransactionRepositoryImpl @Inject constructor(
    private val remoteDataSource: TransactionRemoteDataSource,
    private val localDataSource: TransactionLocalDataSource
) : TransactionRepository {
    
    override fun getTransactions(): Flow<NetworkResult<List<Transaction>>> = flow {
        emit(NetworkResult.Loading())
        
        // Try local first
        val cachedData = localDataSource.getTransactions()
        if (cachedData.isNotEmpty()) {
            emit(NetworkResult.Success(cachedData))
        }
        
        // Fetch from network
        when (val result = remoteDataSource.getTransactions()) {
            is NetworkResult.Success -> {
                localDataSource.saveTransactions(result.data)
                emit(result)
            }
            is NetworkResult.Error -> emit(result)
        }
    }
}
\\\

- **Data Sources**
  - Remote: API calls via Retrofit
  - Local: Room database (future enhancement)

### Dependency Injection (Hilt)

\\\kotlin
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}
\\\

### State Management

\\\kotlin
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

sealed class NetworkResult<T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error<T>(val message: String) : NetworkResult<T>()
    class Loading<T> : NetworkResult<T>()
}
\\\

### Navigation

Using Jetpack Compose Navigation:

\\\kotlin
sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Dashboard : Screen("dashboard")
    object Transactions : Screen("transactions")
    object TransactionDetail : Screen("transactions/{id}") {
        fun createRoute(id: Long) = "transactions/\"
    }
}
\\\

## Backend Architecture

### Layered Architecture

#### Controller Layer
- REST API endpoints
- Request validation
- Response formatting
- Exception handling

\\\kotlin
@RestController
@RequestMapping("/api/transactions")
class TransactionController(
    private val transactionService: TransactionService
) {
    @GetMapping
    fun getTransactions(
        @RequestParam(required = false) startDate: LocalDate?,
        @RequestParam(required = false) endDate: LocalDate?,
        pageable: Pageable
    ): ResponseEntity<Page<TransactionDto>> {
        return ResponseEntity.ok(
            transactionService.getTransactions(startDate, endDate, pageable)
        )
    }
    
    @PostMapping
    fun createTransaction(
        @Valid @RequestBody dto: CreateTransactionDto
    ): ResponseEntity<TransactionDto> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(transactionService.createTransaction(dto))
    }
}
\\\

#### Service Layer
- Business logic
- Transaction management
- Data validation
- Business rule enforcement

\\\kotlin
@Service
class TransactionService(
    private val transactionRepository: TransactionRepository,
    private val categoryService: CategoryService,
    private val userService: UserService
) {
    @Transactional
    fun createTransaction(dto: CreateTransactionDto): TransactionDto {
        val user = userService.getCurrentUser()
        val category = categoryService.getCategoryById(dto.categoryId)
        
        val transaction = Transaction(
            amount = dto.amount,
            type = dto.type,
            category = category,
            user = user,
            date = dto.date,
            description = dto.description
        )
        
        return transactionRepository.save(transaction).toDto()
    }
}
\\\

#### Repository Layer
- Data access abstraction
- Query execution
- CRUD operations

\\\kotlin
@Repository
interface TransactionRepository : JpaRepository<Transaction, Long> {
    
    fun findByUserIdAndDateBetween(
        userId: Long,
        startDate: LocalDate,
        endDate: LocalDate,
        pageable: Pageable
    ): Page<Transaction>
    
    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId AND t.category.id = :categoryId")
    fun findByUserAndCategory(
        @Param("userId") userId: Long,
        @Param("categoryId") categoryId: Long
    ): List<Transaction>
}
\\\

### Security

#### JWT Authentication

\\\kotlin
@Configuration
@EnableWebSecurity
class SecurityConfig {
    
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
            .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
        
        return http.build()
    }
}
\\\

#### Password Encryption

\\\kotlin
@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService
) {
    fun register(dto: RegisterDto): UserDto {
        val user = User(
            email = dto.email,
            password = passwordEncoder.encode(dto.password),
            firstName = dto.firstName,
            lastName = dto.lastName
        )
        return userRepository.save(user).toDto()
    }
}
\\\

### Database Schema

\\\
       
     users                 categories    
       
 id (PK)                 id (PK)         
 email                   name            
 password                type            
 first_name              icon            
 last_name               color           
 created_at              user_id (FK)    
       
                                  
                                  
         
                  
         
            transactions      
         
          id (PK)             
          amount              
          type                
          user_id (FK)        
          category_id (FK)    
          date                
          description         
          created_at          
          updated_at          
         
                  
         
              budgets         
         
          id (PK)             
          user_id (FK)        
          category_id (FK)    
          amount              
          period              
          start_date          
          end_date            
         
\\\

## Data Flow

### Example: Creating a Transaction

\\\
                    
   UI    ViewModel  UseCase  Repository    API    
                    
                                                                        
      User Action                                                       
                                                       
                      Execute                                           
                                                        
                                      Get Data                          
                                                      
                                                        HTTP Request    
                                                       
                                                                        
                                                       
                                                         HTTP Response  
                                                      
                                       Network Result                   
                                                        
                        UI State                                        
                                                       
      Render                                                            
                                                                        
\\\

## Technology Stack

### Android
- **Language**: Kotlin
- **UI**: Jetpack Compose
- **Architecture**: MVVM + Clean Architecture
- **DI**: Hilt (Dagger)
- **Networking**: Retrofit + OkHttp
- **Async**: Kotlin Coroutines + Flow
- **Navigation**: Jetpack Navigation Compose
- **Testing**: JUnit, Mockk, Turbine

### Backend
- **Language**: Kotlin
- **Framework**: Spring Boot 3.x
- **Database**: PostgreSQL (production), H2 (development)
- **ORM**: Spring Data JPA (Hibernate)
- **Security**: Spring Security + JWT
- **Build Tool**: Gradle (Kotlin DSL)
- **Testing**: JUnit 5, MockK, Spring Boot Test

## Design Patterns

### Android
- **MVVM**: Separation of UI and business logic
- **Repository Pattern**: Abstract data sources
- **Observer Pattern**: Flow/StateFlow for reactive updates
- **Factory Pattern**: ViewModel creation
- **Singleton**: ApiService, Database instances

### Backend
- **MVC**: Controller-Service-Repository layers
- **DTO Pattern**: Data transfer between layers
- **Builder Pattern**: Query building
- **Strategy Pattern**: Different authentication strategies
- **Template Method**: Common CRUD operations

## Performance Considerations

### Android
- **Lazy Loading**: Load data on demand
- **Pagination**: Large lists split into pages
- **Caching**: Store frequently accessed data locally
- **Background Processing**: Use Coroutines for heavy operations
- **Image Optimization**: Compress and cache images

### Backend
- **Database Indexing**: Index frequently queried columns
- **Connection Pooling**: Reuse database connections
- **Caching**: Redis for frequently accessed data (future)
- **Pagination**: Limit query results
- **Async Processing**: Background jobs for heavy tasks

## Scalability

### Horizontal Scaling
- Stateless backend design
- Load balancer distribution
- Database replication

### Vertical Scaling
- Optimize queries
- Increase server resources
- Database tuning

## Monitoring & Logging

### Backend
- **Spring Boot Actuator**: Health checks, metrics
- **Logback**: Structured logging
- **Error Tracking**: Log exceptions with context

### Android
- **Crashlytics** (future): Crash reporting
- **Firebase Analytics** (future): User behavior tracking

## Future Enhancements

1. **Offline Support**: Room database for local storage
2. **Real-time Sync**: WebSocket connections
3. **Push Notifications**: FCM integration
4. **Biometric Auth**: Fingerprint/Face recognition
5. **Export Data**: PDF/CSV export functionality
6. **Backup & Restore**: Cloud backup integration
7. **Multi-currency**: Support for different currencies
8. **Recurring Transactions**: Auto-create weekly/monthly transactions
9. **Analytics Dashboard**: Advanced charts and insights
10. **Microservices**: Split backend into smaller services
