package com.example.finance.controller

import com.example.finance.domain.TransactionType
import com.example.finance.dto.request.CategoryRequest
import com.example.finance.dto.response.CategoryResponse
import com.example.finance.security.UserPrincipal
import com.example.finance.service.CategoryService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/categories")
class CategoryController(
    private val categoryService: CategoryService
) {
    
    @GetMapping
    fun getAllCategories(
        @AuthenticationPrincipal user: UserPrincipal,
        @RequestParam(required = false) type: TransactionType?
    ): ResponseEntity<List<CategoryResponse>> {
        val categories = categoryService.getAllCategories(user.id, type)
        return ResponseEntity.ok(categories)
    }
    
    @GetMapping("/{id}")
    fun getCategoryById(
        @AuthenticationPrincipal user: UserPrincipal,
        @PathVariable id: Long
    ): ResponseEntity<CategoryResponse> {
        val category = categoryService.getCategoryById(user.id, id)
        return ResponseEntity.ok(category)
    }
    
    @PostMapping
    fun createCategory(
        @AuthenticationPrincipal user: UserPrincipal,
        @Valid @RequestBody request: CategoryRequest
    ): ResponseEntity<CategoryResponse> {
        val category = categoryService.createCategory(user.id, request)
        return ResponseEntity.status(HttpStatus.CREATED).body(category)
    }
    
    @PutMapping("/{id}")
    fun updateCategory(
        @AuthenticationPrincipal user: UserPrincipal,
        @PathVariable id: Long,
        @Valid @RequestBody request: CategoryRequest
    ): ResponseEntity<CategoryResponse> {
        val category = categoryService.updateCategory(user.id, id, request)
        return ResponseEntity.ok(category)
    }
    
    @DeleteMapping("/{id}")
    fun deleteCategory(
        @AuthenticationPrincipal user: UserPrincipal,
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        categoryService.deleteCategory(user.id, id)
        return ResponseEntity.noContent().build()
    }
}
