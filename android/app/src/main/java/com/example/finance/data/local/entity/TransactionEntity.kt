package com.example.finance.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.finance.domain.model.TransactionType
import java.util.Date

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Double,
    val category: String,
    val date: Date,
    val accountId: Long,
    val note: String?,
    val type: TransactionType
)
