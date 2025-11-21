package com.example.finance.util

import java.text.NumberFormat
import java.util.Locale

object CurrencyUtils {
    private val indianLocale = Locale("en", "IN")
    private val currencyFormatter = NumberFormat.getCurrencyInstance(indianLocale)
    
    init {
        currencyFormatter.maximumFractionDigits = 2
        currencyFormatter.minimumFractionDigits = 2
    }
    
    /**
     * Format amount to Indian Rupees with symbol ₹
     * Example: 1500.50 -> ₹1,500.50
     */
    fun formatCurrency(amount: Double): String {
        return currencyFormatter.format(amount)
    }
    
    /**
     * Format amount to Indian Rupees without decimal if whole number
     * Example: 1500.00 -> ₹1,500
     */
    fun formatCurrencyCompact(amount: Double): String {
        return if (amount % 1.0 == 0.0) {
            "₹${String.format(indianLocale, "%,.0f", amount)}"
        } else {
            formatCurrency(amount)
        }
    }
    
    /**
     * Get currency symbol
     */
    fun getCurrencySymbol(): String = "₹"
}
