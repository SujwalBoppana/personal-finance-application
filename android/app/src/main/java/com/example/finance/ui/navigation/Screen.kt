package com.example.finance.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Signup : Screen("signup")
    object Dashboard : Screen("dashboard")
    object Accounts : Screen("accounts")
    object AddAccount : Screen("add_account?accountId={accountId}") {
        fun createRoute(accountId: Long? = null) = "add_account?accountId=${accountId ?: ""}"
    }
    object Transactions : Screen("transactions")
    object AddTransaction : Screen("add_transaction")
    object Budgets : Screen("budgets")
    object Analytics : Screen("analytics")
    object Categories : Screen("categories")
    object AddCategory : Screen("add_category?categoryId={categoryId}") {
        fun createRoute(categoryId: Long? = null) = "add_category?categoryId=${categoryId ?: ""}"
    }
    object Settings : Screen("settings")
}
