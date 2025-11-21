package com.example.finance.data.repository

import com.example.finance.data.remote.dto.TransactionDto
import com.example.finance.data.remote.dto.TransactionRequest
import com.example.finance.domain.model.TransactionType
import com.example.finance.network.NetworkResult

interface TransactionDataSource {
    suspend fun getTransactions(accountId: Long?, type: TransactionType?): NetworkResult<List<TransactionDto>>
    suspend fun getTransaction(id: Long): NetworkResult<TransactionDto>
    suspend fun createTransaction(request: TransactionRequest): NetworkResult<TransactionDto>
    suspend fun updateTransaction(id: Long, request: TransactionRequest): NetworkResult<TransactionDto>
    suspend fun deleteTransaction(id: Long): NetworkResult<Unit>
}
