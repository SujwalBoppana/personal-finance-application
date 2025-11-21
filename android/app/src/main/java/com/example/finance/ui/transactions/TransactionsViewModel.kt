package com.example.finance.ui.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finance.data.mapper.toDomain
import com.example.finance.data.remote.dto.TransactionRequest
import com.example.finance.data.repository.AccountDataSource
import com.example.finance.data.repository.CategoryDataSource
import com.example.finance.data.repository.TransactionDataSource
import com.example.finance.domain.model.Account
import com.example.finance.domain.model.Category
import com.example.finance.domain.model.Transaction
import com.example.finance.domain.model.TransactionType
import com.example.finance.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val transactionRepository: TransactionDataSource,
    private val accountRepository: AccountDataSource,
    private val categoryRepository: CategoryDataSource
) : ViewModel() {

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions

    private val _accounts = MutableStateFlow<List<Account>>(emptyList())
    val accounts: StateFlow<List<Account>> = _accounts

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _uiState = MutableStateFlow<TransactionsUiState>(TransactionsUiState.Idle)
    val uiState: StateFlow<TransactionsUiState> = _uiState

    init {
        fetchData()
    }

    fun fetchData() {
        viewModelScope.launch {
            fetchDataSuspend()
        }
    }

    private suspend fun fetchDataSuspend() {
        _uiState.value = TransactionsUiState.Loading
        
        // Fetch transactions
        when (val result = transactionRepository.getTransactions(null, null)) {
            is NetworkResult.Success -> _transactions.value = result.data?.map { it.toDomain() } ?: emptyList()
            is NetworkResult.Error -> _uiState.value = TransactionsUiState.Error(result.message ?: "Failed to fetch transactions")
            else -> {}
        }

        // Fetch accounts
        when (val result = accountRepository.getAccounts()) {
            is NetworkResult.Success -> _accounts.value = result.data?.map { it.toDomain() } ?: emptyList()
            else -> {}
        }

        // Fetch categories
        when (val result = categoryRepository.getCategories(null)) {
            is NetworkResult.Success -> _categories.value = result.data?.map { it.toDomain() } ?: emptyList()
            else -> {}
        }
        
        if (_uiState.value is TransactionsUiState.Loading) {
            _uiState.value = TransactionsUiState.Success
        }
    }

    fun addTransaction(
        amount: Double,
        category: String,
        accountId: Long,
        note: String?,
        type: TransactionType,
        date: java.util.Date
    ) {
        viewModelScope.launch {
            _uiState.value = TransactionsUiState.Loading
            
            // Format date to ISO 8601 string with UTC timezone
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
            dateFormat.timeZone = java.util.TimeZone.getTimeZone("UTC")
            val dateString = dateFormat.format(date)
            
            val request = TransactionRequest(
                amount = amount,
                category = category,
                date = dateString,
                accountId = accountId,
                note = note,
                type = type
            )
            
            when (val result = transactionRepository.createTransaction(request)) {
                is NetworkResult.Success -> {
                    fetchDataSuspend() // Wait for data refresh
                    _uiState.value = TransactionsUiState.Success
                }
                is NetworkResult.Error -> {
                    _uiState.value = TransactionsUiState.Error(result.message ?: "Failed to add transaction")
                }
                is NetworkResult.Loading -> {
                    _uiState.value = TransactionsUiState.Loading
                }
            }
        }
    }

    fun deleteTransaction(id: Long) {
        viewModelScope.launch {
            _uiState.value = TransactionsUiState.Loading
            when (val result = transactionRepository.deleteTransaction(id)) {
                is NetworkResult.Success -> {
                    fetchData() // Refresh data
                    _uiState.value = TransactionsUiState.Success
                }
                is NetworkResult.Error -> {
                    _uiState.value = TransactionsUiState.Error(result.message ?: "Failed to delete transaction")
                }
                is NetworkResult.Loading -> {
                    _uiState.value = TransactionsUiState.Loading
                }
            }
        }
    }
}

sealed class TransactionsUiState {
    object Idle : TransactionsUiState()
    object Loading : TransactionsUiState()
    object Success : TransactionsUiState()
    data class Error(val message: String) : TransactionsUiState()
}
