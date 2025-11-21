package com.example.finance.dto.response

import com.example.finance.domain.TransactionType
import java.time.LocalDateTime

data class TransactionResponse(
    val id: Long,
    val userId: Long,
    val accountId: Long,
    val amount: Double,
    val category: String,
    val date: LocalDateTime,
    val note: String?,
    val type: TransactionType,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
