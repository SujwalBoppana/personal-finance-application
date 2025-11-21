package com.example.finance.dto.request

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class BudgetRequest(
    @field:NotBlank(message = "Category is required")
    val category: String,
    
    @field:NotNull(message = "Amount is required")
    val amount: Double,
    
    @field:NotNull(message = "Month is required")
    @field:Min(value = 1, message = "Month must be between 1 and 12")
    @field:Max(value = 12, message = "Month must be between 1 and 12")
    val month: Int,
    
    @field:NotNull(message = "Year is required")
    val year: Int
)
