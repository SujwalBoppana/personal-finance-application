package com.example.finance.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finance.data.remote.dto.UserDto
import com.example.finance.data.repository.AuthDataSource
import com.example.finance.network.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authDataSource: AuthDataSource
) : ViewModel() {
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState
    
    fun register(email: String, password: String) {
        _uiState.value = AuthUiState.Loading
        viewModelScope.launch {
            when (val result = authDataSource.register(email, password)) {
                is NetworkResult.Success -> {
                    _uiState.value = AuthUiState.Success(result.data!!.user)
                }
                is NetworkResult.Error -> {
                    _uiState.value = AuthUiState.Error(result.message ?: "Registration failed")
                }
                is NetworkResult.Loading -> {
                    _uiState.value = AuthUiState.Loading
                }
            }
        }
    }
    
    fun login(email: String, password: String) {
        _uiState.value = AuthUiState.Loading
        viewModelScope.launch {
            when (val result = authDataSource.login(email, password)) {
                is NetworkResult.Success -> {
                    _uiState.value = AuthUiState.Success(result.data!!.user)
                }
                is NetworkResult.Error -> {
                    _uiState.value = AuthUiState.Error(result.message ?: "Login failed")
                }
                is NetworkResult.Loading -> {
                    _uiState.value = AuthUiState.Loading
                }
            }
        }
    }
    
    fun logout() {
        viewModelScope.launch {
            authDataSource.logout()
            _uiState.value = AuthUiState.Idle
        }
    }
}

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val user: UserDto) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}
