package com.example.finance.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.finance.data.local.pref.TokenManager
import com.example.finance.ui.accounts.AccountsScreen
import com.example.finance.ui.accounts.AddAccountScreen
import com.example.finance.ui.analytics.AnalyticsScreen
import com.example.finance.ui.auth.LoginScreen
import com.example.finance.ui.auth.SignupScreen
import com.example.finance.ui.auth.SplashScreen
import com.example.finance.ui.budgets.BudgetsScreen
import com.example.finance.ui.categories.AddCategoryScreen
import com.example.finance.ui.categories.CategoriesScreen
import com.example.finance.ui.dashboard.DashboardScreen
import com.example.finance.ui.security.PinScreen
import com.example.finance.ui.settings.SettingsScreen
import com.example.finance.ui.transactions.AddTransactionScreen
import com.example.finance.ui.transactions.TransactionListScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    tokenManager: TokenManager,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                navController = navController,
                tokenManager = tokenManager
            )
        }
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(Screen.Signup.route) {
            SignupScreen(navController = navController)
        }
        composable(Screen.Dashboard.route) {
            DashboardScreen(navController = navController)
        }
        composable(Screen.Accounts.route) {
            AccountsScreen(navController = navController)
        }
        composable(
            route = Screen.AddAccount.route,
            arguments = listOf(navArgument("accountId") { 
                nullable = true 
                defaultValue = null
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val accountId = backStackEntry.arguments?.getString("accountId")?.toLongOrNull()
            AddAccountScreen(navController = navController, accountId = accountId)
        }
        composable(Screen.Transactions.route) {
            TransactionListScreen(navController = navController)
        }
        composable(Screen.AddTransaction.route) {
            AddTransactionScreen(navController = navController)
        }
        composable(Screen.Budgets.route) {
            BudgetsScreen(navController = navController)
        }
        composable(Screen.Analytics.route) {
            AnalyticsScreen(navController = navController)
        }
        composable(Screen.Categories.route) {
            CategoriesScreen(navController = navController)
        }
        composable(
            route = Screen.AddCategory.route,
            arguments = listOf(navArgument("categoryId") {
                nullable = true
                defaultValue = null
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId")?.toLongOrNull()
            AddCategoryScreen(navController = navController, categoryId = categoryId)
        }
        composable(Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }
        composable("pin_lock") {
            PinScreen(
                navController = navController,
                onPinSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo("pin_lock") { inclusive = true }
                    }
                }
            )
        }
    }
}
