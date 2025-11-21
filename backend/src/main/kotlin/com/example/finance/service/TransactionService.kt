package com.example.finance.service

import com.example.finance.domain.Transaction
import com.example.finance.domain.TransactionType
import com.example.finance.dto.request.TransactionRequest
import com.example.finance.dto.response.TransactionResponse
import com.example.finance.exception.ResourceNotFoundException
import com.example.finance.repository.TransactionRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TransactionService(
    private val transactionRepository: TransactionRepository
) {
    
    fun getAllTransactions(userId: Long, accountId: Long?, type: TransactionType?): List<TransactionResponse> {
        val transactions = when {
            accountId != null -> transactionRepository.findByUserIdAndAccountId(userId, accountId)
            type != null -> transactionRepository.findByUserIdAndType(userId, type)
            else -> transactionRepository.findByUserId(userId)
        }
        return transactions.map { it.toResponse() }
    }
    
    fun getTransactionById(userId: Long, id: Long): TransactionResponse {
        val transaction = transactionRepository.findByUserIdAndId(userId, id)
            ?: throw ResourceNotFoundException("Transaction not found")
        return transaction.toResponse()
    }
    
    fun createTransaction(userId: Long, request: TransactionRequest): TransactionResponse {
        val transaction = Transaction(
            userId = userId,
            accountId = request.accountId,
            amount = request.amount,
            category = request.category,
            date = request.date,
            note = request.note,
            type = request.type,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        val savedTransaction = transactionRepository.save(transaction)
        return savedTransaction.toResponse()
    }
    
    fun updateTransaction(userId: Long, id: Long, request: TransactionRequest): TransactionResponse {
        val transaction = transactionRepository.findByUserIdAndId(userId, id)
            ?: throw ResourceNotFoundException("Transaction not found")
        
        val updatedTransaction = transaction.copy(
            accountId = request.accountId,
            amount = request.amount,
            category = request.category,
            date = request.date,
            note = request.note,
            type = request.type,
            updatedAt = LocalDateTime.now()
        )
        val savedTransaction = transactionRepository.save(updatedTransaction)
        return savedTransaction.toResponse()
    }
    
    fun deleteTransaction(userId: Long, id: Long) {
        val transaction = transactionRepository.findByUserIdAndId(userId, id)
            ?: throw ResourceNotFoundException("Transaction not found")
        transactionRepository.delete(transaction)
    }
    
    private fun Transaction.toResponse() = TransactionResponse(
        id = id,
        userId = userId,
        accountId = accountId,
        amount = amount,
        category = category,
        date = date,
        note = note,
        type = type,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
