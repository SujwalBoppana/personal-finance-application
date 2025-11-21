package com.example.finance.data.repository

import com.example.finance.data.remote.dto.AuthResponse
import com.example.finance.network.NetworkResult

interface AuthDataSource {
    suspend fun register(email: String, password: String): NetworkResult<AuthResponse>
    suspend fun login(email: String, password: String): NetworkResult<AuthResponse>
    suspend fun logout()
    fun isLoggedIn(): Boolean
}
