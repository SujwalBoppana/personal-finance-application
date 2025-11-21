package com.example.finance.data.repository

import com.example.finance.data.remote.dto.CategoryDto
import com.example.finance.data.remote.dto.CategoryRequest
import com.example.finance.domain.model.TransactionType
import com.example.finance.network.NetworkResult

interface CategoryDataSource {
    suspend fun getCategories(type: TransactionType?): NetworkResult<List<CategoryDto>>
    suspend fun getCategory(id: Long): NetworkResult<CategoryDto>
    suspend fun createCategory(request: CategoryRequest): NetworkResult<CategoryDto>
    suspend fun updateCategory(id: Long, request: CategoryRequest): NetworkResult<CategoryDto>
    suspend fun deleteCategory(id: Long): NetworkResult<Unit>
}
