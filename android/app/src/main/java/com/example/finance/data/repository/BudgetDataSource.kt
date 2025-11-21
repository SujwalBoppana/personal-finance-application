package com.example.finance.data.repository

import com.example.finance.data.remote.dto.BudgetDto
import com.example.finance.data.remote.dto.BudgetRequest
import com.example.finance.network.NetworkResult

interface BudgetDataSource {
    suspend fun getBudgets(month: Int?, year: Int?): NetworkResult<List<BudgetDto>>
    suspend fun getBudget(id: Long): NetworkResult<BudgetDto>
    suspend fun createBudget(request: BudgetRequest): NetworkResult<BudgetDto>
    suspend fun updateBudget(id: Long, request: BudgetRequest): NetworkResult<BudgetDto>
    suspend fun deleteBudget(id: Long): NetworkResult<Unit>
}
