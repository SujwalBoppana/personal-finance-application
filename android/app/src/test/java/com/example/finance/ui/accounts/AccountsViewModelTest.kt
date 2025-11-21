package com.example.finance.ui.accounts

import com.example.finance.domain.model.Account
import com.example.finance.domain.model.AccountType
import com.example.finance.domain.repository.AccountRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AccountsViewModelTest {

    private lateinit var viewModel: AccountsViewModel
    private lateinit var repository: AccountRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        
        // Mock initial flow
        coEvery { repository.getAccounts() } returns flowOf(emptyList())
        
        viewModel = AccountsViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `addAccount calls repository insert`() = runTest {
        // Given
        val name = "Test Account"
        val type = AccountType.CASH
        val balance = 100.0
        val color = 0xFF000000

        coEvery { repository.insertAccount(any()) } returns Unit

        // When
        viewModel.addAccount(name, type, balance, color)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { 
            repository.insertAccount(match { 
                it.name == name && it.type == type && it.balance == balance 
            }) 
        }
    }

    @Test
    fun `deleteAccount calls repository delete`() = runTest {
        // Given
        val account = Account(1, "Test", AccountType.CASH, 100.0, 0)
        coEvery { repository.deleteAccount(any()) } returns Unit

        // When
        viewModel.deleteAccount(account)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { repository.deleteAccount(account) }
    }
}
