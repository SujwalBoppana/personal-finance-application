package com.example.finance.controller

import com.example.finance.dto.request.AccountRequest
import com.example.finance.dto.response.AccountResponse
import com.example.finance.security.UserPrincipal
import com.example.finance.service.AccountService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/accounts")
class AccountController(
    private val accountService: AccountService
) {
    
    @GetMapping
    fun getAllAccounts(@AuthenticationPrincipal user: UserPrincipal): ResponseEntity<List<AccountResponse>> {
        val accounts = accountService.getAllAccounts(user.id)
        return ResponseEntity.ok(accounts)
    }
    
    @GetMapping("/{id}")
    fun getAccountById(
        @AuthenticationPrincipal user: UserPrincipal,
        @PathVariable id: Long
    ): ResponseEntity<AccountResponse> {
        val account = accountService.getAccountById(user.id, id)
        return ResponseEntity.ok(account)
    }
    
    @PostMapping
    fun createAccount(
        @AuthenticationPrincipal user: UserPrincipal,
        @Valid @RequestBody request: AccountRequest
    ): ResponseEntity<AccountResponse> {
        val account = accountService.createAccount(user.id, request)
        return ResponseEntity.status(HttpStatus.CREATED).body(account)
    }
    
    @PutMapping("/{id}")
    fun updateAccount(
        @AuthenticationPrincipal user: UserPrincipal,
        @PathVariable id: Long,
        @Valid @RequestBody request: AccountRequest
    ): ResponseEntity<AccountResponse> {
        val account = accountService.updateAccount(user.id, id, request)
        return ResponseEntity.ok(account)
    }
    
    @DeleteMapping("/{id}")
    fun deleteAccount(
        @AuthenticationPrincipal user: UserPrincipal,
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        accountService.deleteAccount(user.id, id)
        return ResponseEntity.noContent().build()
    }
}
