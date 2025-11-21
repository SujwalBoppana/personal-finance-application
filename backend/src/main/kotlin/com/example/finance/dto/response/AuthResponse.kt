package com.example.finance.dto.response

import java.time.LocalDateTime

data class UserResponse(
    val id: Long,
    val email: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

data class AuthResponse(
    val token: String,
    val user: UserResponse
)
