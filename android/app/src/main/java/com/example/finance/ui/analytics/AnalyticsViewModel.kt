package com.example.finance.ui.analytics

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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

data class AnalyticsState(
    val pieChartData: List<PieChartData> = emptyList(),
    val barChartData: List<BarChartData> = emptyList(),
    val insights: List<String> = emptyList(),
    val totalExpenseThisMonth: Double = 0.0,
    val isLoading: Boolean = false,
    val error: String? = null
)

data class PieChartData(
    val category: String,
    val amount: Double,
    val color: Long,
    val percentage: Float
)

data class BarChartData(
    val label: String,
    val amount: Double,
    val percentageOfMax: Float
)

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val transactionRepository: TransactionDataSource
) : ViewModel() {

    private val _state = MutableStateFlow(AnalyticsState(isLoading = true))
    val state: StateFlow<AnalyticsState> = _state

    init {
        fetchData()
    }

    fun fetchData() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            
            when (val result = transactionRepository.getTransactions(null, null)) {
                is NetworkResult.Success -> {
                    val transactions = result.data?.map { it.toDomain() } ?: emptyList()
                    processTransactions(transactions)
                }
                is NetworkResult.Error -> {
                    _state.value = _state.value.copy(isLoading = false, error = result.message ?: "Failed to load analytics")
                }
                is NetworkResult.Loading -> {
                    _state.value = _state.value.copy(isLoading = true)
                }
            }
        }
    }

    private fun processTransactions(transactions: List<Transaction>) {
        val currentMonth = DateUtils.getCurrentMonth()
        val currentYear = DateUtils.getCurrentYear()

        // --- Pie Chart Data (Current Month Expenses) ---
        val currentMonthExpenses = transactions.filter {
            DateUtils.isSameMonth(it.date, currentMonth, currentYear) && 
            it.type == TransactionType.EXPENSE
        }

        val totalExpenseThisMonth = currentMonthExpenses.sumOf { it.amount }
        
        val expensesByCategory = currentMonthExpenses
            .groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { it.amount } }
            .toList()
            .sortedByDescending { it.second }

        // Assign colors dynamically (simple palette)
        val colors = listOf(
            0xFFEF5350, 0xFFEC407A, 0xFFAB47BC, 0xFF7E57C2, 0xFF5C6BC0,
            0xFF42A5F5, 0xFF29B6F6, 0xFF26C6DA, 0xFF26A69A, 0xFF66BB6A
        )

        val pieChartData = expensesByCategory.mapIndexed { index, (category, amount) ->
            PieChartData(
                category = category,
                amount = amount,
                color = colors[index % colors.size],
                percentage = if (totalExpenseThisMonth > 0) (amount / totalExpenseThisMonth).toFloat() else 0f
            )
        }

        // --- Bar Chart Data (Last 6 Months) ---
        val barChartData = mutableListOf<BarChartData>()
        val maxMonths = 6
        var maxMonthlyExpense = 0.0

        for (i in maxMonths - 1 downTo 0) {
            val monthCal = Calendar.getInstance()
            monthCal.add(Calendar.MONTH, -i)
            val month = monthCal.get(Calendar.MONTH)
            val year = monthCal.get(Calendar.YEAR)
            val monthLabel = SimpleDateFormat("MMM", Locale.getDefault()).format(monthCal.time)

            val monthlyTotal = transactions
                .filter {
                    DateUtils.isSameMonth(it.date, month, year) &&
                    it.type == TransactionType.EXPENSE
                }
                .sumOf { it.amount }

            if (monthlyTotal > maxMonthlyExpense) {
                maxMonthlyExpense = monthlyTotal
            }

            barChartData.add(BarChartData(monthLabel, monthlyTotal, 0f)) // Update percentage later
        }

        val finalBarChartData = barChartData.map {
            it.copy(percentageOfMax = if (maxMonthlyExpense > 0) (it.amount / maxMonthlyExpense).toFloat() else 0f)
        }

        // --- Insights ---
        val insights = mutableListOf<String>()
        if (totalExpenseThisMonth > 0) {
            val topCategory = pieChartData.firstOrNull()
            if (topCategory != null) {
                insights.add("You spent the most on ${topCategory.category} this month (${(topCategory.percentage * 100).toInt()}%).")
            }
            
            val prevMonthCal = Calendar.getInstance()
            prevMonthCal.add(Calendar.MONTH, -1)
            val prevMonthTotal = finalBarChartData.getOrNull(4)?.amount ?: 0.0 // 5th item is prev month (0-5 index)
            
            if (totalExpenseThisMonth > prevMonthTotal && prevMonthTotal > 0) {
                val increase = ((totalExpenseThisMonth - prevMonthTotal) / prevMonthTotal) * 100
                insights.add("Spending increased by ${increase.toInt()}% compared to last month.")
            } else if (totalExpenseThisMonth < prevMonthTotal) {
                insights.add("Great job! You've spent less than last month.")
            }
        } else {
            insights.add("No expenses recorded for this month yet.")
        }

        _state.value = AnalyticsState(
            pieChartData = pieChartData,
            barChartData = finalBarChartData,
            insights = insights,
            totalExpenseThisMonth = totalExpenseThisMonth,
            isLoading = false
        )
    }
}
