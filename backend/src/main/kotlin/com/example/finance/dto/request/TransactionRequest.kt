package com.example.finance.dto.request

import com.example.finance.domain.TransactionType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

data class TransactionRequest(
    @field:NotNull(message = "Account ID is required")
    val accountId: Long,
    
    @field:NotNull(message = "Amount is required")
    val amount: Double,
    
    @field:NotBlank(message = "Category is required")
    val category: String,
    
    @field:NotNull(message = "Date is required")
    val date: LocalDateTime,
    
    val note: String? = null,
    
    @field:NotNull(message = "Type is required")
    val type: TransactionType
)
