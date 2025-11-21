package com.example.finance.dto.request

import com.example.finance.domain.TransactionType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CategoryRequest(
    @field:NotBlank(message = "Name is required")
    val name: String,
    
    @field:NotBlank(message = "Icon is required")
    val icon: String,
    
    @field:NotNull(message = "Type is required")
    val type: TransactionType
)
