package com.example.finance.ui.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AuthScreen(
    viewModel: AuthViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoginMode by remember { mutableStateOf(true) }
    
    val uiState = viewModel.uiState.collectAsState().value
    
    Column {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (isLoginMode) {
                viewModel.login(email, password)
            } else {
                viewModel.register(email, password)
            }
        }) {
            Text(if (isLoginMode) "Login" else "Register")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { isLoginMode = !isLoginMode }) {
            Text(if (isLoginMode) "Switch to Register" else "Switch to Login")
        }
        
        when (uiState) {
            is AuthUiState.Loading -> Text("Loading...")
            is AuthUiState.Success -> Text("Welcome, ${uiState.user.email}")
            is AuthUiState.Error -> Text("Error: ${uiState.message}")
            else -> {}
        }
    }
}
