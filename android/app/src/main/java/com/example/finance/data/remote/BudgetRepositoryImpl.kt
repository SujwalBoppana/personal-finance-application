package com.example.finance.data.remote

import com.example.finance.data.remote.dto.BudgetDto
import com.example.finance.data.remote.dto.BudgetRequest
import com.example.finance.data.repository.BudgetDataSource
import com.example.finance.network.BaseRepository
import com.example.finance.network.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BudgetRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : BaseRepository(), BudgetDataSource {

    override suspend fun getBudgets(month: Int?, year: Int?): NetworkResult<List<BudgetDto>> =
        withContext(Dispatchers.IO) {
            safeApiCall { apiService.getBudgets(month, year) }
        }

    override suspend fun getBudget(id: Long): NetworkResult<BudgetDto> =
        withContext(Dispatchers.IO) {
            safeApiCall { apiService.getBudget(id) }
        }

    override suspend fun createBudget(request: BudgetRequest): NetworkResult<BudgetDto> =
        withContext(Dispatchers.IO) {
            safeApiCall { apiService.createBudget(request) }
        }

    override suspend fun updateBudget(id: Long, request: BudgetRequest): NetworkResult<BudgetDto> =
        withContext(Dispatchers.IO) {
            safeApiCall { apiService.updateBudget(id, request) }
        }

    override suspend fun deleteBudget(id: Long): NetworkResult<Unit> =
        withContext(Dispatchers.IO) {
            safeApiCall { apiService.deleteBudget(id) }
        }
}
