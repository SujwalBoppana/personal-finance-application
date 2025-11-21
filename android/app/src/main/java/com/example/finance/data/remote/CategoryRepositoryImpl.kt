package com.example.finance.data.remote

import com.example.finance.data.remote.dto.CategoryDto
import com.example.finance.data.remote.dto.CategoryRequest
import com.example.finance.data.repository.CategoryDataSource
import com.example.finance.domain.model.TransactionType
import com.example.finance.network.BaseRepository
import com.example.finance.network.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : BaseRepository(), CategoryDataSource {

    override suspend fun getCategories(type: TransactionType?): NetworkResult<List<CategoryDto>> =
        withContext(Dispatchers.IO) {
            safeApiCall { apiService.getCategories(type) }
        }

    override suspend fun getCategory(id: Long): NetworkResult<CategoryDto> =
        withContext(Dispatchers.IO) {
            safeApiCall { apiService.getCategory(id) }
        }

    override suspend fun createCategory(request: CategoryRequest): NetworkResult<CategoryDto> =
        withContext(Dispatchers.IO) {
            safeApiCall { apiService.createCategory(request) }
        }

    override suspend fun updateCategory(id: Long, request: CategoryRequest): NetworkResult<CategoryDto> =
        withContext(Dispatchers.IO) {
            safeApiCall { apiService.updateCategory(id, request) }
        }

    override suspend fun deleteCategory(id: Long): NetworkResult<Unit> =
        withContext(Dispatchers.IO) {
            safeApiCall { apiService.deleteCategory(id) }
        }
}
