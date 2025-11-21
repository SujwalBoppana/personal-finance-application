package com.example.finance.dto.response

import com.example.finance.domain.TransactionType
import java.time.LocalDateTime

data class CategoryResponse(
    val id: Long,
    val userId: Long,
    val name: String,
    val icon: String,
    val type: TransactionType,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
