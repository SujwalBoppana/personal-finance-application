package com.example.finance.repository

import com.example.finance.domain.Category
import com.example.finance.domain.TransactionType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository : JpaRepository<Category, Long> {
    fun findByUserId(userId: Long): List<Category>
    fun findByUserIdAndId(userId: Long, id: Long): Category?
    fun findByUserIdAndType(userId: Long, type: TransactionType): List<Category>
}
