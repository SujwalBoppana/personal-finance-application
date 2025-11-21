package com.example.finance.data.remote

import com.example.finance.data.remote.dto.AccountDto
import com.example.finance.data.remote.dto.AccountRequest
import com.example.finance.data.repository.AccountDataSource
import com.example.finance.network.BaseRepository
import com.example.finance.network.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : BaseRepository(), AccountDataSource {

    override suspend fun getAccounts(): NetworkResult<List<AccountDto>> =
        withContext(Dispatchers.IO) {
            safeApiCall { apiService.getAccounts() }
        }

    override suspend fun getAccount(id: Long): NetworkResult<AccountDto> =
        withContext(Dispatchers.IO) {
            safeApiCall { apiService.getAccount(id) }
        }

    override suspend fun createAccount(request: AccountRequest): NetworkResult<AccountDto> =
        withContext(Dispatchers.IO) {
            safeApiCall { apiService.createAccount(request) }
        }

    override suspend fun updateAccount(id: Long, request: AccountRequest): NetworkResult<AccountDto> =
        withContext(Dispatchers.IO) {
            safeApiCall { apiService.updateAccount(id, request) }
        }

    override suspend fun deleteAccount(id: Long): NetworkResult<Unit> =
        withContext(Dispatchers.IO) {
            safeApiCall { apiService.deleteAccount(id) }
        }
}
