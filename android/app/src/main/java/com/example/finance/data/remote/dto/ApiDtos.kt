package com.example.finance.data.remote.dto

import com.example.finance.domain.model.AccountType
import com.example.finance.domain.model.TransactionType
import java.util.Date

// Auth DTOs
data class RegisterRequest(
    val email: String,
    val password: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class UserDto(
    val id: Long,
    val email: String,
    val createdAt: String,
    val updatedAt: String
)

data class AuthResponse(
    val token: String,
    val user: UserDto
)

// Account DTOs
data class AccountRequest(
    val name: String,
    val type: AccountType,
    val balance: Double,
    val color: Long
)

data class AccountDto(
    val id: Long,
    val userId: Long,
    val name: String,
    val type: AccountType,
    val balance: Double,
    val color: Long,
    val createdAt: String,
    val updatedAt: String
)

// Transaction DTOs
data class TransactionRequest(
    val accountId: Long,
    val amount: Double,
    val category: String,
    val date: String, // ISO 8601 format
    val note: String?,
    val type: TransactionType
)

data class TransactionDto(
    val id: Long,
    val userId: Long,
    val accountId: Long,
    val amount: Double,
    val category: String,
    val date: String,
    val note: String?,
    val type: TransactionType,
    val createdAt: String,
    val updatedAt: String
)

// Budget DTOs
data class BudgetRequest(
    val category: String,
    val amount: Double,
    val month: Int,
    val year: Int
)

data class BudgetDto(
    val id: Long,
    val userId: Long,
    val category: String,
    val amount: Double,
    val month: Int,
    val year: Int,
    val createdAt: String,
    val updatedAt: String
)

// Category DTOs
data class CategoryRequest(
    val name: String,
    val icon: String,
    val type: TransactionType
)

data class CategoryDto(
    val id: Long,
    val userId: Long,
    val name: String,
    val icon: String,
    val type: TransactionType,
    val createdAt: String,
    val updatedAt: String
)
