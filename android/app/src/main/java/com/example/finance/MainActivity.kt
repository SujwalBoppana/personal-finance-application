package com.example.finance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.finance.data.local.pref.TokenManager
import com.example.finance.ui.navigation.MainScaffold
import com.example.finance.ui.theme.PersonalFinanceAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    @Inject
    lateinit var tokenManager: TokenManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PersonalFinanceAppTheme {
                MainScreen(tokenManager = tokenManager)
            }
        }
    }
}

@Composable
fun MainScreen(tokenManager: TokenManager) {
    val navController = rememberNavController()
    
    PersonalFinanceAppTheme {
        MainScaffold(
            navController = navController,
            tokenManager = tokenManager
        )
    }
}
