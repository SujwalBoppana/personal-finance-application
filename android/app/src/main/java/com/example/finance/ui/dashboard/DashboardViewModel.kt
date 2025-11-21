package com.example.finance.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finance.data.mapper.toDomain
import com.example.finance.data.repository.TransactionDataSource
import com.example.finance.domain.model.Transaction
import com.example.finance.domain.model.TransactionType
import com.example.finance.network.NetworkResult
import com.example.finance.util.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val transactionRepository: TransactionDataSource
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState(isLoading = true))
    val state: StateFlow<DashboardState> = _state

    init {
        fetchData()
    }

    fun fetchData() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            
            when (val result = transactionRepository.getTransactions(null, null)) {
                is NetworkResult.Success -> {
                    val transactions = result.data?.map { it.toDomain() } ?: emptyList()
                    _state.value = calculateDashboardData(transactions).copy(isLoading = false)
                }
                is NetworkResult.Error -> {
                    _state.value = _state.value.copy(isLoading = false, error = result.message ?: "Failed to load dashboard")
                }
                is NetworkResult.Loading -> {
                    _state.value = _state.value.copy(isLoading = true)
                }
            }
        }
    }

    private fun calculateDashboardData(transactions: List<Transaction>): DashboardState {
        val today = Date()
        val currentMonth = DateUtils.getCurrentMonth()
        val currentYear = DateUtils.getCurrentYear()
        
        val todayTransactions = transactions.filter { DateUtils.isSameDay(it.date, today) }
        
        val todayIncome = todayTransactions
            .filter { it.type == TransactionType.INCOME }
            .sumOf { it.amount }
            
        val todayExpense = todayTransactions
            .filter { it.type == TransactionType.EXPENSE }
            .sumOf { it.amount }

        val monthTransactions = transactions.filter {
            DateUtils.isSameMonth(it.date, currentMonth, currentYear)
        }

        val monthIncome = monthTransactions
            .filter { it.type == TransactionType.INCOME }
            .sumOf { it.amount }
            
        val monthExpense = monthTransactions
            .filter { it.type == TransactionType.EXPENSE }
            .sumOf { it.amount }

        // Top Categories (Expenses only)
        val expenseTransactions = monthTransactions.filter { it.type == TransactionType.EXPENSE }
        val categoryTotals = expenseTransactions
            .groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { it.amount } }
            .toList()
            .sortedByDescending { it.second }
            .take(5)

        val maxCategoryAmount = categoryTotals.firstOrNull()?.second ?: 1.0
        
        val topCategories = categoryTotals.map { (category, amount) ->
            CategoryExpense(
                categoryName = category,
                amount = amount,
                percentage = (amount / maxCategoryAmount).toFloat()
            )
        }

        return DashboardState(
            todayIncome = todayIncome,
            todayExpense = todayExpense,
            monthIncome = monthIncome,
            monthExpense = monthExpense,
            topCategories = topCategories,
            recentTransactions = transactions.take(5)
        )
    }
}
