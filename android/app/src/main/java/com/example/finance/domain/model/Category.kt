package com.example.finance.domain.model

data class Category(
    val id: Long = 0,
    val name: String,
    val icon: String, // Icon name or resource ID identifier
    val type: TransactionType
)
