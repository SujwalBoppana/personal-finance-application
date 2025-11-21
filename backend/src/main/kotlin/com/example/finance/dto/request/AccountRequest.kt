package com.example.finance.dto.request

import com.example.finance.domain.AccountType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class AccountRequest(
    @field:NotBlank(message = "Name is required")
    val name: String,
    
    @field:NotNull(message = "Type is required")
    val type: AccountType,
    
    @field:NotNull(message = "Balance is required")
    val balance: Double,
    
    @field:NotNull(message = "Color is required")
    val color: Long
)
