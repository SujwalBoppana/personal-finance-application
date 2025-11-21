package com.example.finance.service

import com.example.finance.domain.Category
import com.example.finance.domain.TransactionType
import com.example.finance.dto.request.CategoryRequest
import com.example.finance.dto.response.CategoryResponse
import com.example.finance.exception.ResourceNotFoundException
import com.example.finance.repository.CategoryRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository
) {
    
    fun getAllCategories(userId: Long, type: TransactionType?): List<CategoryResponse> {
        val categories = if (type != null) {
            categoryRepository.findByUserIdAndType(userId, type)
        } else {
            categoryRepository.findByUserId(userId)
        }
        return categories.map { it.toResponse() }
    }
    
    fun getCategoryById(userId: Long, id: Long): CategoryResponse {
        val category = categoryRepository.findByUserIdAndId(userId, id)
            ?: throw ResourceNotFoundException("Category not found")
        return category.toResponse()
    }
    
    fun createCategory(userId: Long, request: CategoryRequest): CategoryResponse {
        val category = Category(
            userId = userId,
            name = request.name,
            icon = request.icon,
            type = request.type,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        val savedCategory = categoryRepository.save(category)
        return savedCategory.toResponse()
    }
    
    fun updateCategory(userId: Long, id: Long, request: CategoryRequest): CategoryResponse {
        val category = categoryRepository.findByUserIdAndId(userId, id)
            ?: throw ResourceNotFoundException("Category not found")
        
        val updatedCategory = category.copy(
            name = request.name,
            icon = request.icon,
            type = request.type,
            updatedAt = LocalDateTime.now()
        )
        val savedCategory = categoryRepository.save(updatedCategory)
        return savedCategory.toResponse()
    }
    
    fun deleteCategory(userId: Long, id: Long) {
        val category = categoryRepository.findByUserIdAndId(userId, id)
            ?: throw ResourceNotFoundException("Category not found")
        categoryRepository.delete(category)
    }
    
    private fun Category.toResponse() = CategoryResponse(
        id = id,
        userId = userId,
        name = name,
        icon = icon,
        type = type,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
