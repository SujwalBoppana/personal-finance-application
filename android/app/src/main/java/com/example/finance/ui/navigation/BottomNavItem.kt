package com.example.finance.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Dashboard : BottomNavItem(
        route = Screen.Dashboard.route,
        title = "Dashboard",
        icon = Icons.Default.Home
    )
    
    object Accounts : BottomNavItem(
        route = Screen.Accounts.route,
        title = "Accounts",
        icon = Icons.Default.Person
    )
    
    object Transactions : BottomNavItem(
        route = Screen.Transactions.route,
        title = "Transactions",
        icon = Icons.Default.List
    )
    
    object Budgets : BottomNavItem(
        route = Screen.Budgets.route,
        title = "Budgets",
        icon = Icons.Default.Star
    )
    
    object Analytics : BottomNavItem(
        route = Screen.Analytics.route,
        title = "Analytics",
        icon = Icons.Default.Info
    )
    
    companion object {
        fun getItems() = listOf(
            Dashboard,
            Accounts,
            Transactions,
            Budgets,
            Analytics
        )
    }
}
