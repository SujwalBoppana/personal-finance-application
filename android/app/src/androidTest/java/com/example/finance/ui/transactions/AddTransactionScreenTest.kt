package com.example.finance.ui.transactions

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import org.junit.Rule
import org.junit.Test

class AddTransactionScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun addTransactionScreen_displaysComponents() {
        // Given
        composeTestRule.setContent {
            val navController = rememberNavController()
            // Note: We are not providing HiltViewModel here, so this test checks static UI elements
            // For full integration test, we'd need HiltAndroidRule and proper injection
            AddTransactionScreen(navController = navController)
        }

        // Then
        composeTestRule.onNodeWithText("Add Transaction").assertIsDisplayed()
        composeTestRule.onNodeWithText("Amount").assertIsDisplayed()
        composeTestRule.onNodeWithText("Category").assertIsDisplayed()
        composeTestRule.onNodeWithText("Account").assertIsDisplayed()
        composeTestRule.onNodeWithText("Save Transaction").assertIsDisplayed()
    }
}
