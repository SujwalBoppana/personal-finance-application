package com.example.finance.dto.response

import com.example.finance.domain.AccountType
import java.time.LocalDateTime

data class AccountResponse(
    val id: Long,
    val userId: Long,
    val name: String,
    val type: AccountType,
    val balance: Double,
    val color: Long,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
