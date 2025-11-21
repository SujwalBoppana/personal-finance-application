package com.example.finance.controller

import com.example.finance.dto.request.BudgetRequest
import com.example.finance.dto.response.BudgetResponse
import com.example.finance.security.UserPrincipal
import com.example.finance.service.BudgetService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/budgets")
class BudgetController(
    private val budgetService: BudgetService
) {
    
    @GetMapping
    fun getAllBudgets(
        @AuthenticationPrincipal user: UserPrincipal,
        @RequestParam(required = false) month: Int?,
        @RequestParam(required = false) year: Int?
    ): ResponseEntity<List<BudgetResponse>> {
        val budgets = budgetService.getAllBudgets(user.id, month, year)
        return ResponseEntity.ok(budgets)
    }
    
    @GetMapping("/{id}")
    fun getBudgetById(
        @AuthenticationPrincipal user: UserPrincipal,
        @PathVariable id: Long
    ): ResponseEntity<BudgetResponse> {
        val budget = budgetService.getBudgetById(user.id, id)
        return ResponseEntity.ok(budget)
    }
    
    @PostMapping
    fun createBudget(
        @AuthenticationPrincipal user: UserPrincipal,
        @Valid @RequestBody request: BudgetRequest
    ): ResponseEntity<BudgetResponse> {
        val budget = budgetService.createBudget(user.id, request)
        return ResponseEntity.status(HttpStatus.CREATED).body(budget)
    }
    
    @PutMapping("/{id}")
    fun updateBudget(
        @AuthenticationPrincipal user: UserPrincipal,
        @PathVariable id: Long,
        @Valid @RequestBody request: BudgetRequest
    ): ResponseEntity<BudgetResponse> {
        val budget = budgetService.updateBudget(user.id, id, request)
        return ResponseEntity.ok(budget)
    }
    
    @DeleteMapping("/{id}")
    fun deleteBudget(
        @AuthenticationPrincipal user: UserPrincipal,
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        budgetService.deleteBudget(user.id, id)
        return ResponseEntity.noContent().build()
    }
}
