package com.example.finance.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.finance.domain.model.TransactionType

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val icon: String,
    val type: TransactionType
)
