package com.example.finance.data.repository

import com.example.finance.data.local.dao.AccountDao
import com.example.finance.data.local.entity.AccountEntity
import com.example.finance.domain.model.AccountType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AccountRepositoryImplTest {

    private lateinit var repository: AccountRepositoryImpl
    private lateinit var dao: AccountDao

    @Before
    fun setup() {
        dao = mockk()
        repository = AccountRepositoryImpl(dao)
    }

    @Test
    fun `getAccounts maps entities to domain models`() = runTest {
        // Given
        val entity = AccountEntity(1, "Test", "CASH", 100.0, 0)
        coEvery { dao.getAllAccounts() } returns flowOf(listOf(entity))

        // When
        val result = repository.getAccounts().first()

        // Then
        assertEquals(1, result.size)
        assertEquals("Test", result[0].name)
        assertEquals(AccountType.CASH, result[0].type)
    }

    @Test
    fun `insertAccount calls dao insert`() = runTest {
        // Given
        val account = com.example.finance.domain.model.Account(0, "Test", AccountType.CASH, 100.0, 0)
        coEvery { dao.insertAccount(any()) } returns Unit

        // When
        repository.insertAccount(account)

        // Then
        coVerify { dao.insertAccount(any()) }
    }
}
