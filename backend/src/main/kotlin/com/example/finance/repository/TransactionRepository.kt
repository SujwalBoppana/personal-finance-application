package com.example.finance.repository

import com.example.finance.domain.Transaction
import com.example.finance.domain.TransactionType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface TransactionRepository : JpaRepository<Transaction, Long> {
    fun findByUserId(userId: Long): List<Transaction>
    fun findByUserIdAndId(userId: Long, id: Long): Transaction?
    fun findByUserIdAndAccountId(userId: Long, accountId: Long): List<Transaction>
    fun findByUserIdAndType(userId: Long, type: TransactionType): List<Transaction>
    fun findByUserIdAndDateBetween(userId: Long, startDate: LocalDateTime, endDate: LocalDateTime): List<Transaction>
    
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.userId = :userId AND t.type = :type")
    fun sumByUserIdAndType(userId: Long, type: TransactionType): Double?
}
