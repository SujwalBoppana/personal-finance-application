package com.example.finance.ui.budgets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finance.data.remote.dto.BudgetDto
import com.example.finance.data.remote.dto.BudgetRequest
import com.example.finance.data.remote.dto.CategoryDto
import com.example.finance.data.repository.BudgetDataSource
import com.example.finance.data.repository.CategoryDataSource
import com.example.finance.data.repository.TransactionDataSource
import com.example.finance.domain.model.TransactionType
import com.example.finance.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

data class BudgetState(
    val budgets: List<BudgetProgress> = emptyList(),
    val categories: List<CategoryDto> = emptyList(),
    val totalBudget: Double = 0.0,
    val totalSpent: Double = 0.0
)

data class BudgetProgress(
    val budget: BudgetDto,
    val spent: Double,
    val progress: Float // 0.0 to 1.0 (or > 1.0 if exceeded)
)

@HiltViewModel
class BudgetsViewModel @Inject constructor(
    private val budgetRepository: BudgetDataSource,
    private val transactionRepository: TransactionDataSource,
    private val categoryRepository: CategoryDataSource
) : ViewModel() {

    private val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1 // 1-indexed for backend
    private val currentYear = Calendar.getInstance().get(Calendar.YEAR)

    private val _uiState = MutableStateFlow<BudgetsUiState>(BudgetsUiState.Idle)
    val uiState: StateFlow<BudgetsUiState> = _uiState
    
    private val _budgetState = MutableStateFlow(BudgetState())
    val state: StateFlow<BudgetState> = _budgetState

    init {
        fetchData()
    }

    fun fetchData() {
        viewModelScope.launch {
            _uiState.value = BudgetsUiState.Loading
            
            // Fetch budgets
            val budgetsResult = budgetRepository.getBudgets(currentMonth, currentYear)
            val budgets = if (budgetsResult is NetworkResult.Success) budgetsResult.data ?: emptyList() else emptyList()
            
            // Fetch transactions for calculation
            val transactionsResult = transactionRepository.getTransactions(null, TransactionType.EXPENSE)
            val transactions = if (transactionsResult is NetworkResult.Success) transactionsResult.data ?: emptyList() else emptyList()
            
            // Fetch categories
            val categoriesResult = categoryRepository.getCategories(TransactionType.EXPENSE)
            val categories = if (categoriesResult is NetworkResult.Success) categoriesResult.data ?: emptyList() else emptyList()

            // Calculate progress
            val monthTransactions = transactions.filter {
                // Simple date check - in real app use proper date parsing
                // Assuming backend returns date string ISO 8601
                true // Simplified for now, backend filtering preferred
            }

            val budgetProgressList = budgets.map { budget ->
                val spent = monthTransactions
                    .filter { it.category == budget.category }
                    .sumOf { it.amount }
                
                BudgetProgress(
                    budget = budget,
                    spent = spent,
                    progress = if (budget.amount > 0) (spent / budget.amount).toFloat() else 0f
                )
            }

            val totalBudget = budgets.sumOf { it.amount }
            val totalSpent = monthTransactions.sumOf { it.amount }

            _budgetState.value = BudgetState(
                budgets = budgetProgressList,
                categories = categories,
                totalBudget = totalBudget,
                totalSpent = totalSpent
            )
            
            _uiState.value = BudgetsUiState.Success
        }
    }

    fun addBudget(category: String, amount: Double) {
        viewModelScope.launch {
            _uiState.value = BudgetsUiState.Loading
            val request = BudgetRequest(
                category = category,
                amount = amount,
                month = currentMonth,
                year = currentYear
            )
            
            when (val result = budgetRepository.createBudget(request)) {
                is NetworkResult.Success -> {
                    fetchData()
                    _uiState.value = BudgetsUiState.Success
                }
                is NetworkResult.Error -> {
                    _uiState.value = BudgetsUiState.Error(result.message ?: "Failed to add budget")
                }
                is NetworkResult.Loading -> {
                    _uiState.value = BudgetsUiState.Loading
                }
            }
        }
    }
    
    fun updateBudget(id: Long, category: String, amount: Double) {
         viewModelScope.launch {
            _uiState.value = BudgetsUiState.Loading
            val request = BudgetRequest(
                category = category,
                amount = amount,
                month = currentMonth,
                year = currentYear
            )
            
            when (val result = budgetRepository.updateBudget(id, request)) {
                is NetworkResult.Success -> {
                    fetchData()
                    _uiState.value = BudgetsUiState.Success
                }
                is NetworkResult.Error -> {
                    _uiState.value = BudgetsUiState.Error(result.message ?: "Failed to update budget")
                }
                is NetworkResult.Loading -> {
                    _uiState.value = BudgetsUiState.Loading
                }
            }
        }
    }

    fun deleteBudget(id: Long) {
        viewModelScope.launch {
            _uiState.value = BudgetsUiState.Loading
            when (val result = budgetRepository.deleteBudget(id)) {
                is NetworkResult.Success -> {
                    fetchData()
                    _uiState.value = BudgetsUiState.Success
                }
                is NetworkResult.Error -> {
                    _uiState.value = BudgetsUiState.Error(result.message ?: "Failed to delete budget")
                }
                is NetworkResult.Loading -> {
                    _uiState.value = BudgetsUiState.Loading
                }
            }
        }
    }
}

sealed class BudgetsUiState {
    object Idle : BudgetsUiState()
    object Loading : BudgetsUiState()
    object Success : BudgetsUiState()
    data class Error(val message: String) : BudgetsUiState()
}
