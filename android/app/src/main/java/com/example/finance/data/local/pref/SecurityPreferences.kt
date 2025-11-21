package com.example.finance.data.local.pref

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SecurityPreferences @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences("security_prefs", Context.MODE_PRIVATE)

    fun isPinEnabled(): Boolean {
        return prefs.getBoolean("is_pin_enabled", false)
    }

    fun setPinEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("is_pin_enabled", enabled).apply()
    }

    fun getPin(): String? {
        return prefs.getString("pin_code", null)
    }

    fun setPin(pin: String) {
        prefs.edit().putString("pin_code", pin).apply()
    }
}
