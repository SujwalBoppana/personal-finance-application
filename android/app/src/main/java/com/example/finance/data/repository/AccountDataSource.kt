package com.example.finance.data.repository

import com.example.finance.data.remote.dto.AccountDto
import com.example.finance.data.remote.dto.AccountRequest
import com.example.finance.network.NetworkResult

interface AccountDataSource {
    suspend fun getAccounts(): NetworkResult<List<AccountDto>>
    suspend fun getAccount(id: Long): NetworkResult<AccountDto>
    suspend fun createAccount(request: AccountRequest): NetworkResult<AccountDto>
    suspend fun updateAccount(id: Long, request: AccountRequest): NetworkResult<AccountDto>
    suspend fun deleteAccount(id: Long): NetworkResult<Unit>
}
