package com.example.finance.domain

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "budgets")
data class Budget(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    
    @Column(nullable = false)
    val userId: Long,
    
    @Column(nullable = false)
    val category: String,
    
    @Column(nullable = false)
    val amount: Double,
    
    @Column(nullable = false)
    val month: Int,
    
    @Column(nullable = false)
    val year: Int,
    
    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
    
    @Column(nullable = false)
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
