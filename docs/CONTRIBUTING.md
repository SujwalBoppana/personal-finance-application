# Contributing Guide

Thank you for considering contributing to the Personal Finance Application! This document provides guidelines and instructions for contributing.

## Code of Conduct

- Be respectful and inclusive
- Provide constructive feedback
- Focus on what is best for the community
- Show empathy towards other community members

## How Can I Contribute?

### Reporting Bugs

Before creating bug reports, please check existing issues. When creating a bug report, include:

- **Clear title** describing the issue
- **Steps to reproduce** the behavior
- **Expected behavior** vs actual behavior
- **Screenshots** if applicable
- **Environment details** (OS, Android version, etc.)

**Example:**
\\\markdown
**Bug:** Transaction list not updating after adding new transaction

**Steps to Reproduce:**
1. Open the app
2. Click "Add Transaction"
3. Fill in details and save
4. Return to transaction list

**Expected:** New transaction appears in the list
**Actual:** List doesn't update until app restart

**Environment:**
- Android Version: 13
- Device: Pixel 5 Emulator
- App Version: 1.0.0
\\\

### Suggesting Enhancements

Enhancement suggestions are tracked as GitHub issues. When creating an enhancement suggestion, include:

- **Clear and descriptive title**
- **Detailed description** of the suggested enhancement
- **Use cases** explaining why this would be useful
- **Mockups** or examples if applicable

### Pull Requests

1. **Fork the repository** and create your branch from \main\
2. **Follow the project structure** and coding conventions
3. **Write tests** for new features
4. **Update documentation** as needed
5. **Ensure tests pass** before submitting
6. **Write meaningful commit messages**

## Development Process

### Branch Naming Convention

Use descriptive branch names with prefixes:
- \eature/\ - New features
- \ix/\ - Bug fixes
- \docs/\ - Documentation changes
- \efactor/\ - Code refactoring
- \	est/\ - Adding or updating tests
- \chore/\ - Maintenance tasks

**Examples:**
\\\
feature/budget-alerts
fix/transaction-date-picker
docs/api-endpoints
refactor/repository-pattern
\\\

### Commit Message Convention

Follow [Conventional Commits](https://www.conventionalcommits.org/):

\\\
<type>(<scope>): <subject>

<body>

<footer>
\\\

**Types:**
- \eat\: New feature
- \ix\: Bug fix
- \docs\: Documentation changes
- \style\: Code style changes (formatting, etc.)
- \efactor\: Code refactoring
- \	est\: Adding or updating tests
- \chore\: Maintenance tasks
- \perf\: Performance improvements

**Examples:**
\\\
feat(android): add budget alert notifications

fix(backend): resolve null pointer in transaction service

docs(api): update authentication endpoint documentation

refactor(android): migrate to Kotlin Flow in repositories
\\\

## Coding Standards

### Kotlin (Android & Backend)

#### Naming Conventions
\\\kotlin
// Classes: PascalCase
class TransactionRepository

// Functions: camelCase
fun calculateTotalExpenses()

// Constants: UPPER_SNAKE_CASE
const val MAX_TRANSACTION_LIMIT = 1000

// Variables: camelCase
val userName = "John"
\\\

#### Code Organization
\\\kotlin
// Bad
fun processData(data: List<String>): List<String> {
    val result = mutableListOf<String>()
    for (item in data) {
        val processed = item.trim().uppercase()
        if (processed.isNotEmpty()) result.add(processed)
    }
    return result
}

// Good
fun processData(data: List<String>): List<String> =
    data.map { it.trim().uppercase() }
        .filter { it.isNotEmpty() }
\\\

#### Null Safety
\\\kotlin
// Prefer safe calls and elvis operator
val length = text?.length ?: 0

// Use let for null checks
user?.let { 
    println(it.name)
}
\\\

### Android Specific

#### MVVM Architecture
\\\
app/
 data/
    model/
    repository/
    source/
 domain/
    model/
    usecase/
 ui/
     screens/
     components/
     theme/
\\\

#### Composable Naming
\\\kotlin
// Screen composables: noun
@Composable
fun TransactionListScreen()

// Component composables: noun
@Composable
fun TransactionItem()

// Preview composables: PreviewXxx
@Preview
@Composable
fun PreviewTransactionItem()
\\\

#### State Management
\\\kotlin
// Use sealed classes for UI state
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}
\\\

### Backend Specific

#### REST API Design
\\\kotlin
// Use appropriate HTTP methods
@GetMapping("/transactions")           // Retrieve
@PostMapping("/transactions")          // Create
@PutMapping("/transactions/{id}")      // Update
@DeleteMapping("/transactions/{id}")   // Delete

// Use plural nouns for resources
/api/transactions  
/api/transaction   

// Use kebab-case for multi-word resources
/api/spending-categories  
/api/spendingCategories   
\\\

#### Service Layer
\\\kotlin
@Service
class TransactionService(
    private val repository: TransactionRepository
) {
    @Transactional
    fun createTransaction(dto: TransactionDto): Transaction {
        // Business logic here
    }
}
\\\

## Testing Guidelines

### Unit Tests

#### Android
\\\kotlin
class TransactionViewModelTest {
    @Test
    fun \should load transactions successfully\() = runTest {
        // Arrange
        val repository = FakeTransactionRepository()
        val viewModel = TransactionViewModel(repository)
        
        // Act
        viewModel.loadTransactions()
        
        // Assert
        val state = viewModel.uiState.value
        assertTrue(state is UiState.Success)
    }
}
\\\

#### Backend
\\\kotlin
@SpringBootTest
class TransactionServiceTest {
    @Autowired
    lateinit var service: TransactionService
    
    @Test
    fun \should create transaction successfully\() {
        // Arrange
        val dto = TransactionDto(...)
        
        // Act
        val result = service.createTransaction(dto)
        
        // Assert
        assertNotNull(result.id)
        assertEquals(dto.amount, result.amount)
    }
}
\\\

### Integration Tests

\\\kotlin
@AutoConfigureMockMvc
@SpringBootTest
class TransactionControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc
    
    @Test
    fun \should return transactions list\() {
        mockMvc.perform(get("/api/transactions"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("\$.content").isArray)
    }
}
\\\

## Documentation

### Code Comments

\\\kotlin
/**
 * Calculates the total budget remaining for a specific category.
 *
 * @param categoryId The ID of the category
 * @param period The time period for calculation
 * @return The remaining budget amount, or null if no budget exists
 * @throws IllegalArgumentException if categoryId is invalid
 */
fun calculateRemainingBudget(
    categoryId: Long,
    period: BudgetPeriod
): Double?
\\\

### API Documentation

Update \docs/API.md\ when adding or modifying endpoints:

\\\markdown
### Get Transaction Summary
**GET** \/api/transactions/summary\

Returns aggregated transaction data.

**Query Parameters:**
- \startDate\: Start date (ISO-8601)
- \endDate\: End date (ISO-8601)

**Response:**
\\\json
{
  "totalIncome": 5000.00,
  "totalExpenses": 3500.00
}
\\\
\\\

## Pull Request Process

### Before Submitting

- [ ] Code follows the project's style guidelines
- [ ] Self-review of code completed
- [ ] Comments added for complex logic
- [ ] Documentation updated
- [ ] Tests added for new features
- [ ] All tests pass locally
- [ ] No merge conflicts with \main\

### PR Description Template

\\\markdown
## Description
Brief description of changes

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Breaking change
- [ ] Documentation update

## Changes Made
- List specific changes
- One per line

## Testing
Describe how to test the changes

## Screenshots (if applicable)
Add screenshots for UI changes

## Checklist
- [ ] Code follows style guidelines
- [ ] Self-review completed
- [ ] Tests added/updated
- [ ] Documentation updated
\\\

### Review Process

1. **Automated Checks**: CI/CD workflows must pass
2. **Code Review**: At least one approval required
3. **Testing**: Reviewer tests changes locally
4. **Merge**: Squash and merge to \main\

## Project Structure

### Android
\\\
android/
 app/
    src/
       main/
          java/com/example/finance/
             data/
             domain/
             ui/
             MainActivity.kt
          res/
       test/
    build.gradle.kts
    proguard-rules.pro
 build.gradle.kts
\\\

### Backend
\\\
backend/
 src/
    main/
       kotlin/com/example/finance/
          controller/
          service/
          repository/
          model/
          Application.kt
       resources/
           application.yml
    test/
 build.gradle.kts
 settings.gradle.kts
\\\

## Getting Help

- **Questions**: Create a GitHub Discussion
- **Bugs**: Create an Issue with the \ug\ label
- **Features**: Create an Issue with the \enhancement\ label
- **Documentation**: Create an Issue with the \documentation\ label

## Recognition

Contributors will be recognized in:
- \CONTRIBUTORS.md\ file
- Release notes
- GitHub contributors page

Thank you for contributing! 
