package com.example.finance.domain

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "transactions")
data class Transaction(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    
    @Column(nullable = false)
    val userId: Long,
    
    @Column(nullable = false)
    val accountId: Long,
    
    @Column(nullable = false)
    val amount: Double,
    
    @Column(nullable = false)
    val category: String,
    
    @Column(nullable = false)
    val date: LocalDateTime,
    
    @Column
    val note: String? = null,
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val type: TransactionType,
    
    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
    
    @Column(nullable = false)
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

enum class TransactionType {
    INCOME, EXPENSE, TRANSFER
}
