package com.example.finance.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.finance.domain.model.AccountType

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val type: AccountType,
    val balance: Double,
    val color: Long
)
