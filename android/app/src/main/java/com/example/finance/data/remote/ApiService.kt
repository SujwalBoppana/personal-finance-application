package com.example.finance.data.remote

import com.example.finance.data.remote.dto.*
import com.example.finance.domain.model.TransactionType
import retrofit2.http.*

interface ApiService {
    
    // Auth endpoints
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse
    
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse
    
    // Account endpoints
    @GET("accounts")
    suspend fun getAccounts(): List<AccountDto>
    
    @GET("accounts/{id}")
    suspend fun getAccount(@Path("id") id: Long): AccountDto
    
    @POST("accounts")
    suspend fun createAccount(@Body request: AccountRequest): AccountDto
    
    @PUT("accounts/{id}")
    suspend fun updateAccount(@Path("id") id: Long, @Body request: AccountRequest): AccountDto
    
    @DELETE("accounts/{id}")
    suspend fun deleteAccount(@Path("id") id: Long)
    
    // Transaction endpoints
    @GET("transactions")
    suspend fun getTransactions(
        @Query("accountId") accountId: Long? = null,
        @Query("type") type: TransactionType? = null
    ): List<TransactionDto>
    
    @GET("transactions/{id}")
    suspend fun getTransaction(@Path("id") id: Long): TransactionDto
    
    @POST("transactions")
    suspend fun createTransaction(@Body request: TransactionRequest): TransactionDto
    
    @PUT("transactions/{id}")
    suspend fun updateTransaction(@Path("id") id: Long, @Body request: TransactionRequest): TransactionDto
    
    @DELETE("transactions/{id}")
    suspend fun deleteTransaction(@Path("id") id: Long)
    
    // Budget endpoints
    @GET("budgets")
    suspend fun getBudgets(
        @Query("month") month: Int? = null,
        @Query("year") year: Int? = null
    ): List<BudgetDto>
    
    @GET("budgets/{id}")
    suspend fun getBudget(@Path("id") id: Long): BudgetDto
    
    @POST("budgets")
    suspend fun createBudget(@Body request: BudgetRequest): BudgetDto
    
    @PUT("budgets/{id}")
    suspend fun updateBudget(@Path("id") id: Long, @Body request: BudgetRequest): BudgetDto
    
    @DELETE("budgets/{id}")
    suspend fun deleteBudget(@Path("id") id: Long)
    
    // Category endpoints
    @GET("categories")
    suspend fun getCategories(@Query("type") type: TransactionType? = null): List<CategoryDto>
    
    @GET("categories/{id}")
    suspend fun getCategory(@Path("id") id: Long): CategoryDto
    
    @POST("categories")
    suspend fun createCategory(@Body request: CategoryRequest): CategoryDto
    
    @PUT("categories/{id}")
    suspend fun updateCategory(@Path("id") id: Long, @Body request: CategoryRequest): CategoryDto
    
    @DELETE("categories/{id}")
    suspend fun deleteCategory(@Path("id") id: Long)
}
