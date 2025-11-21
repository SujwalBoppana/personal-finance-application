package com.example.finance.ui.accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finance.data.remote.dto.AccountDto
import com.example.finance.data.remote.dto.AccountRequest
import com.example.finance.data.repository.AccountDataSource
import com.example.finance.domain.model.AccountType
import com.example.finance.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.example.finance.data.mapper.toDomain
import com.example.finance.domain.model.Account

@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val repository: AccountDataSource
) : ViewModel() {

    private val _accounts = MutableStateFlow<List<Account>>(emptyList())
    val accounts: StateFlow<List<Account>> = _accounts
    
    private val _uiState = MutableStateFlow<AccountsUiState>(AccountsUiState.Idle)
    val uiState: StateFlow<AccountsUiState> = _uiState

    init {
        fetchAccounts()
    }

    fun fetchAccounts() {
        viewModelScope.launch {
            _uiState.value = AccountsUiState.Loading
            when (val result = repository.getAccounts()) {
                is NetworkResult.Success -> {
                    _accounts.value = result.data?.map { it.toDomain() } ?: emptyList()
                    _uiState.value = AccountsUiState.Success
                }
                is NetworkResult.Error -> {
                    _uiState.value = AccountsUiState.Error(result.message ?: "Failed to fetch accounts")
                }
                is NetworkResult.Loading -> {
                    _uiState.value = AccountsUiState.Loading
                }
            }
        }
    }

    fun addAccount(name: String, type: AccountType, balance: Double, color: Long) {
        viewModelScope.launch {
            _uiState.value = AccountsUiState.Loading
            val request = AccountRequest(
                name = name,
                type = type,
                balance = balance,
                color = color
            )
            when (val result = repository.createAccount(request)) {
                is NetworkResult.Success -> {
                    fetchAccounts() // Refresh list
                    _uiState.value = AccountsUiState.Success
                }
                is NetworkResult.Error -> {
                    _uiState.value = AccountsUiState.Error(result.message ?: "Failed to add account")
                }
                is NetworkResult.Loading -> {
                    _uiState.value = AccountsUiState.Loading
                }
            }
        }
    }

    fun updateAccount(id: Long, name: String, type: AccountType, balance: Double, color: Long) {
        viewModelScope.launch {
            _uiState.value = AccountsUiState.Loading
            val request = AccountRequest(
                name = name,
                type = type,
                balance = balance,
                color = color
            )
            when (val result = repository.updateAccount(id, request)) {
                is NetworkResult.Success -> {
                    fetchAccounts() // Refresh list
                    _uiState.value = AccountsUiState.Success
                }
                is NetworkResult.Error -> {
                    _uiState.value = AccountsUiState.Error(result.message ?: "Failed to update account")
                }
                is NetworkResult.Loading -> {
                    _uiState.value = AccountsUiState.Loading
                }
            }
        }
    }

    suspend fun getAccount(id: Long): Account? {
        return when (val result = repository.getAccount(id)) {
            is NetworkResult.Success -> result.data?.toDomain()
            else -> null
        }
    }

    fun deleteAccount(id: Long) {
        viewModelScope.launch {
            _uiState.value = AccountsUiState.Loading
            when (val result = repository.deleteAccount(id)) {
                is NetworkResult.Success -> {
                    fetchAccounts() // Refresh list
                    _uiState.value = AccountsUiState.Success
                }
                is NetworkResult.Error -> {
                    _uiState.value = AccountsUiState.Error(result.message ?: "Failed to delete account")
                }
                is NetworkResult.Loading -> {
                    _uiState.value = AccountsUiState.Loading
                }
            }
        }
    }
}

sealed class AccountsUiState {
    object Idle : AccountsUiState()
    object Loading : AccountsUiState()
    object Success : AccountsUiState()
    data class Error(val message: String) : AccountsUiState()
}
