package com.example.finance.domain.model

import java.util.Date

data class Transaction(
    val id: Long = 0,
    val amount: Double,
    val category: String,
    val date: Date,
    val accountId: Long,
    val note: String?,
    val type: TransactionType
)

enum class TransactionType {
    INCOME, EXPENSE, TRANSFER
}
