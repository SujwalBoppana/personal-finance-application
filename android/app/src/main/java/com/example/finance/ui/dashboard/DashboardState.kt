package com.example.finance.ui.dashboard

import com.example.finance.domain.model.Transaction

data class DashboardState(
    val todayIncome: Double = 0.0,
    val todayExpense: Double = 0.0,
    val monthIncome: Double = 0.0,
    val monthExpense: Double = 0.0,
    val topCategories: List<CategoryExpense> = emptyList(),
    val recentTransactions: List<Transaction> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class CategoryExpense(
    val categoryName: String,
    val amount: Double,
    val percentage: Float // 0.0 to 1.0 relative to max category or total? Usually relative to max for bar chart width
)
