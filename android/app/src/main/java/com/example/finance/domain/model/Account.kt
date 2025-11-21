package com.example.finance.domain.model

data class Account(
    val id: Long = 0,
    val name: String,
    val type: AccountType,
    val balance: Double,
    val color: Long // Color as Long (ARGB)
)

enum class AccountType {
    CASH, BANK, CARD, WALLET, OTHER
}
