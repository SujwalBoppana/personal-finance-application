package com.example.finance.service

import com.example.finance.domain.Budget
import com.example.finance.dto.request.BudgetRequest
import com.example.finance.dto.response.BudgetResponse
import com.example.finance.exception.ResourceNotFoundException
import com.example.finance.repository.BudgetRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class BudgetService(
    private val budgetRepository: BudgetRepository
) {
    
    fun getAllBudgets(userId: Long, month: Int?, year: Int?): List<BudgetResponse> {
        val budgets = if (month != null && year != null) {
            budgetRepository.findByUserIdAndMonthAndYear(userId, month, year)
        } else {
            budgetRepository.findByUserId(userId)
        }
        return budgets.map { it.toResponse() }
    }
    
    fun getBudgetById(userId: Long, id: Long): BudgetResponse {
        val budget = budgetRepository.findByUserIdAndId(userId, id)
            ?: throw ResourceNotFoundException("Budget not found")
        return budget.toResponse()
    }
    
    fun createBudget(userId: Long, request: BudgetRequest): BudgetResponse {
        val budget = Budget(
            userId = userId,
            category = request.category,
            amount = request.amount,
            month = request.month,
            year = request.year,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        val savedBudget = budgetRepository.save(budget)
        return savedBudget.toResponse()
    }
    
    fun updateBudget(userId: Long, id: Long, request: BudgetRequest): BudgetResponse {
        val budget = budgetRepository.findByUserIdAndId(userId, id)
            ?: throw ResourceNotFoundException("Budget not found")
        
        val updatedBudget = budget.copy(
            category = request.category,
            amount = request.amount,
            month = request.month,
            year = request.year,
            updatedAt = LocalDateTime.now()
        )
        val savedBudget = budgetRepository.save(updatedBudget)
        return savedBudget.toResponse()
    }
    
    fun deleteBudget(userId: Long, id: Long) {
        val budget = budgetRepository.findByUserIdAndId(userId, id)
            ?: throw ResourceNotFoundException("Budget not found")
        budgetRepository.delete(budget)
    }
    
    private fun Budget.toResponse() = BudgetResponse(
        id = id,
        userId = userId,
        category = category,
        amount = amount,
        month = month,
        year = year,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
