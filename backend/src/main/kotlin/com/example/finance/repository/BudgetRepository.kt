package com.example.finance.repository

import com.example.finance.domain.Budget
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BudgetRepository : JpaRepository<Budget, Long> {
    fun findByUserId(userId: Long): List<Budget>
    fun findByUserIdAndId(userId: Long, id: Long): Budget?
    fun findByUserIdAndMonthAndYear(userId: Long, month: Int, year: Int): List<Budget>
    fun findByUserIdAndCategoryAndMonthAndYear(userId: Long, category: String, month: Int, year: Int): Budget?
}
