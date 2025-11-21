package com.example.finance.service

import com.example.finance.domain.Account
import com.example.finance.dto.request.AccountRequest
import com.example.finance.dto.response.AccountResponse
import com.example.finance.exception.ResourceNotFoundException
import com.example.finance.repository.AccountRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AccountService(
    private val accountRepository: AccountRepository
) {
    
    fun getAllAccounts(userId: Long): List<AccountResponse> {
        return accountRepository.findByUserId(userId).map { it.toResponse() }
    }
    
    fun getAccountById(userId: Long, id: Long): AccountResponse {
        val account = accountRepository.findByUserIdAndId(userId, id)
            ?: throw ResourceNotFoundException("Account not found")
        return account.toResponse()
    }
    
    fun createAccount(userId: Long, request: AccountRequest): AccountResponse {
        val account = Account(
            userId = userId,
            name = request.name,
            type = request.type,
            balance = request.balance,
            color = request.color,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        val savedAccount = accountRepository.save(account)
        return savedAccount.toResponse()
    }
    
    fun updateAccount(userId: Long, id: Long, request: AccountRequest): AccountResponse {
        val account = accountRepository.findByUserIdAndId(userId, id)
            ?: throw ResourceNotFoundException("Account not found")
        
        val updatedAccount = account.copy(
            name = request.name,
            type = request.type,
            balance = request.balance,
            color = request.color,
            updatedAt = LocalDateTime.now()
        )
        val savedAccount = accountRepository.save(updatedAccount)
        return savedAccount.toResponse()
    }
    
    fun deleteAccount(userId: Long, id: Long) {
        val account = accountRepository.findByUserIdAndId(userId, id)
            ?: throw ResourceNotFoundException("Account not found")
        accountRepository.delete(account)
    }
    
    private fun Account.toResponse() = AccountResponse(
        id = id,
        userId = userId,
        name = name,
        type = type,
        balance = balance,
        color = color,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
