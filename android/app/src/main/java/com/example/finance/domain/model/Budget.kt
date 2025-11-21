package com.example.finance.domain.model

data class Budget(
    val id: Long = 0,
    val category: String,
    val amount: Double,
    val month: Int,
    val year: Int
)
