package com.example.finance.data.remote

import com.example.finance.data.local.pref.TokenManager
import com.example.finance.data.remote.dto.AuthResponse
import com.example.finance.data.remote.dto.LoginRequest
import com.example.finance.data.remote.dto.RegisterRequest
import com.example.finance.data.repository.AuthDataSource
import com.example.finance.network.BaseRepository
import com.example.finance.network.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) : BaseRepository(), AuthDataSource {
    
    override suspend fun register(email: String, password: String): NetworkResult<AuthResponse> =
        withContext(Dispatchers.IO) {
            val result = safeApiCall {
                apiService.register(RegisterRequest(email, password))
            }
            if (result is NetworkResult.Success && result.data != null) {
                tokenManager.saveToken(result.data.token)
            }
            result
        }
    
    override suspend fun login(email: String, password: String): NetworkResult<AuthResponse> =
        withContext(Dispatchers.IO) {
            val result = safeApiCall {
                apiService.login(LoginRequest(email, password))
            }
            if (result is NetworkResult.Success && result.data != null) {
                tokenManager.saveToken(result.data.token)
            }
            result
        }
    
    override suspend fun logout() {
        tokenManager.clearToken()
    }
    
    override fun isLoggedIn(): Boolean = tokenManager.isLoggedIn()
}
