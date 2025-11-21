package com.example.finance.data.remote

import com.example.finance.data.remote.dto.TransactionDto
import com.example.finance.data.remote.dto.TransactionRequest
import com.example.finance.data.repository.TransactionDataSource
import com.example.finance.domain.model.TransactionType
import com.example.finance.network.BaseRepository
import com.example.finance.network.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : BaseRepository(), TransactionDataSource {

    override suspend fun getTransactions(accountId: Long?, type: TransactionType?): NetworkResult<List<TransactionDto>> =
        withContext(Dispatchers.IO) {
            safeApiCall { apiService.getTransactions(accountId, type) }
        }

    override suspend fun getTransaction(id: Long): NetworkResult<TransactionDto> =
        withContext(Dispatchers.IO) {
            safeApiCall { apiService.getTransaction(id) }
        }

    override suspend fun createTransaction(request: TransactionRequest): NetworkResult<TransactionDto> =
        withContext(Dispatchers.IO) {
            safeApiCall { apiService.createTransaction(request) }
        }

    override suspend fun updateTransaction(id: Long, request: TransactionRequest): NetworkResult<TransactionDto> =
        withContext(Dispatchers.IO) {
            safeApiCall { apiService.updateTransaction(id, request) }
        }

    override suspend fun deleteTransaction(id: Long): NetworkResult<Unit> =
        withContext(Dispatchers.IO) {
            safeApiCall { apiService.deleteTransaction(id) }
        }
}
