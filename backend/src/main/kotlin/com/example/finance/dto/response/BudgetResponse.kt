package com.example.finance.dto.response

import java.time.LocalDateTime

data class BudgetResponse(
    val id: Long,
    val userId: Long,
    val category: String,
    val amount: Double,
    val month: Int,
    val year: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
