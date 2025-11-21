package com.example.finance.ui.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finance.data.mapper.toDomain
import com.example.finance.data.remote.dto.CategoryRequest
import com.example.finance.data.repository.CategoryDataSource
import com.example.finance.domain.model.Category
import com.example.finance.domain.model.TransactionType
import com.example.finance.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val repository: CategoryDataSource
) : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories
    
    private val _uiState = MutableStateFlow<CategoriesUiState>(CategoriesUiState.Idle)
    val uiState: StateFlow<CategoriesUiState> = _uiState

    init {
        fetchCategories()
    }

    fun fetchCategories(type: TransactionType? = null) {
        viewModelScope.launch {
            _uiState.value = CategoriesUiState.Loading
            when (val result = repository.getCategories(type)) {
                is NetworkResult.Success -> {
                    _categories.value = result.data?.map { it.toDomain() } ?: emptyList()
                    _uiState.value = CategoriesUiState.Success
                }
                is NetworkResult.Error -> {
                    _uiState.value = CategoriesUiState.Error(result.message ?: "Failed to fetch categories")
                }
                is NetworkResult.Loading -> {
                    _uiState.value = CategoriesUiState.Loading
                }
            }
        }
    }

    fun addCategory(name: String, icon: String, type: TransactionType) {
        viewModelScope.launch {
            _uiState.value = CategoriesUiState.Loading
            val request = CategoryRequest(
                name = name,
                icon = icon,
                type = type
            )
            when (val result = repository.createCategory(request)) {
                is NetworkResult.Success -> {
                    fetchCategories() // Refresh list
                    _uiState.value = CategoriesUiState.Success
                }
                is NetworkResult.Error -> {
                    _uiState.value = CategoriesUiState.Error(result.message ?: "Failed to add category")
                }
                is NetworkResult.Loading -> {
                    _uiState.value = CategoriesUiState.Loading
                }
            }
        }
    }

    fun updateCategory(id: Long, name: String, icon: String, type: TransactionType) {
        viewModelScope.launch {
            _uiState.value = CategoriesUiState.Loading
            val request = CategoryRequest(
                name = name,
                icon = icon,
                type = type
            )
            when (val result = repository.updateCategory(id, request)) {
                is NetworkResult.Success -> {
                    fetchCategories() // Refresh list
                    _uiState.value = CategoriesUiState.Success
                }
                is NetworkResult.Error -> {
                    _uiState.value = CategoriesUiState.Error(result.message ?: "Failed to update category")
                }
                is NetworkResult.Loading -> {
                    _uiState.value = CategoriesUiState.Loading
                }
            }
        }
    }

    suspend fun getCategory(id: Long): Category? {
        return when (val result = repository.getCategory(id)) {
            is NetworkResult.Success -> result.data?.toDomain()
            else -> null
        }
    }

    fun deleteCategory(id: Long) {
        viewModelScope.launch {
            _uiState.value = CategoriesUiState.Loading
            when (val result = repository.deleteCategory(id)) {
                is NetworkResult.Success -> {
                    fetchCategories() // Refresh list
                    _uiState.value = CategoriesUiState.Success
                }
                is NetworkResult.Error -> {
                    _uiState.value = CategoriesUiState.Error(result.message ?: "Failed to delete category")
                }
                is NetworkResult.Loading -> {
                    _uiState.value = CategoriesUiState.Loading
                }
            }
        }
    }
}

sealed class CategoriesUiState {
    object Idle : CategoriesUiState()
    object Loading : CategoriesUiState()
    object Success : CategoriesUiState()
    data class Error(val message: String) : CategoriesUiState()
}
