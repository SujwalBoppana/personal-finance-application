package com.example.finance.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.finance.data.local.dao.AccountDao
import com.example.finance.data.local.dao.TransactionDao
import com.example.finance.data.local.entity.AccountEntity
import com.example.finance.data.local.entity.TransactionEntity

import com.example.finance.data.local.dao.BudgetDao
import com.example.finance.data.local.dao.CategoryDao
import com.example.finance.data.local.entity.BudgetEntity
import com.example.finance.data.local.entity.CategoryEntity

@Database(entities = [AccountEntity::class, TransactionEntity::class, BudgetEntity::class, CategoryEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao
    abstract fun categoryDao(): CategoryDao
}
