package com.example.finance.controller

import com.example.finance.domain.TransactionType
import com.example.finance.dto.request.TransactionRequest
import com.example.finance.dto.response.TransactionResponse
import com.example.finance.security.UserPrincipal
import com.example.finance.service.TransactionService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/transactions")
class TransactionController(
    private val transactionService: TransactionService
) {
    
    @GetMapping
    fun getAllTransactions(
        @AuthenticationPrincipal user: UserPrincipal,
        @RequestParam(required = false) accountId: Long?,
        @RequestParam(required = false) type: TransactionType?
    ): ResponseEntity<List<TransactionResponse>> {
        val transactions = transactionService.getAllTransactions(user.id, accountId, type)
        return ResponseEntity.ok(transactions)
    }
    
    @GetMapping("/{id}")
    fun getTransactionById(
        @AuthenticationPrincipal user: UserPrincipal,
        @PathVariable id: Long
    ): ResponseEntity<TransactionResponse> {
        val transaction = transactionService.getTransactionById(user.id, id)
        return ResponseEntity.ok(transaction)
    }
    
    @PostMapping
    fun createTransaction(
        @AuthenticationPrincipal user: UserPrincipal,
        @Valid @RequestBody request: TransactionRequest
    ): ResponseEntity<TransactionResponse> {
        val transaction = transactionService.createTransaction(user.id, request)
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction)
    }
    
    @PutMapping("/{id}")
    fun updateTransaction(
        @AuthenticationPrincipal user: UserPrincipal,
        @PathVariable id: Long,
        @Valid @RequestBody request: TransactionRequest
    ): ResponseEntity<TransactionResponse> {
        val transaction = transactionService.updateTransaction(user.id, id, request)
        return ResponseEntity.ok(transaction)
    }
    
    @DeleteMapping("/{id}")
    fun deleteTransaction(
        @AuthenticationPrincipal user: UserPrincipal,
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        transactionService.deleteTransaction(user.id, id)
        return ResponseEntity.noContent().build()
    }
}
