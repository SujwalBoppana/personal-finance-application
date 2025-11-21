package com.example.finance.repository

import com.example.finance.domain.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository : JpaRepository<Account, Long> {
    fun findByUserId(userId: Long): List<Account>
    fun findByUserIdAndId(userId: Long, id: Long): Account?
}
