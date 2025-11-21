package com.example.finance.di

import com.example.finance.data.remote.*
import com.example.finance.data.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthDataSource(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthDataSource

    @Binds
    @Singleton
    abstract fun bindAccountDataSource(
        accountRepositoryImpl: AccountRepositoryImpl
    ): AccountDataSource

    @Binds
    @Singleton
    abstract fun bindTransactionDataSource(
        transactionRepositoryImpl: TransactionRepositoryImpl
    ): TransactionDataSource

    @Binds
    @Singleton
    abstract fun bindBudgetDataSource(
        budgetRepositoryImpl: BudgetRepositoryImpl
    ): BudgetDataSource

    @Binds
    @Singleton
    abstract fun bindCategoryDataSource(
        categoryRepositoryImpl: CategoryRepositoryImpl
    ): CategoryDataSource
}
