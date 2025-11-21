package com.example.finance.ui.security

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.finance.data.local.pref.SecurityPreferences
import com.example.finance.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PinViewModel @Inject constructor(
    private val securityPreferences: SecurityPreferences
) : ViewModel() {
    
    fun checkPin(inputPin: String): Boolean {
        val storedPin = securityPreferences.getPin()
        return storedPin == inputPin
    }

    fun isPinSet(): Boolean {
        return securityPreferences.isPinEnabled() && securityPreferences.getPin() != null
    }
}

@Composable
fun PinScreen(
    navController: NavController,
    viewModel: PinViewModel = hiltViewModel(),
    onPinSuccess: () -> Unit
) {
    var pin by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Enter PIN",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // PIN Dots
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(bottom = 32.dp)
        ) {
            repeat(4) { index ->
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(
                            if (index < pin.length) MaterialTheme.colorScheme.primary else Color.LightGray
                        )
                )
            }
        }

        if (error) {
            Text(
                text = "Incorrect PIN",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Number Pad
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val rows = listOf(
                listOf("1", "2", "3"),
                listOf("4", "5", "6"),
                listOf("7", "8", "9"),
                listOf("", "0", "DEL")
            )

            rows.forEach { row ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    row.forEach { key ->
                        NumberButton(
                            text = key,
                            onClick = {
                                if (key == "DEL") {
                                    if (pin.isNotEmpty()) {
                                        pin = pin.dropLast(1)
                                        error = false
                                    }
                                } else if (key.isNotEmpty()) {
                                    if (pin.length < 4) {
                                        pin += key
                                        error = false
                                        if (pin.length == 4) {
                                            if (viewModel.checkPin(pin)) {
                                                onPinSuccess()
                                            } else {
                                                error = true
                                                pin = ""
                                            }
                                        }
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NumberButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(80.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (text.isEmpty()) Color.Transparent else MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        enabled = text.isNotEmpty()
    ) {
        if (text.isNotEmpty()) {
            Text(text = text, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
    }
}
