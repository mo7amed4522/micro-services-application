package com.example.luxorappandroid.ui.theme

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Theme Manager class that handles dark/light mode state management
 * with persistent storage using SharedPreferences (Android equivalent of UserDefaults)
 */
class ThemeManager(private val context: Context? = null) {
    private val prefs: SharedPreferences? = context?.getSharedPreferences("luxor_theme_prefs", Context.MODE_PRIVATE)
    
    var isDarkMode by mutableStateOf(loadThemePreference())
        private set
    
    private fun loadThemePreference(): Boolean {
        return prefs?.getBoolean("isDarkMode", false) ?: false
    }
    
    private fun saveThemePreference(isDark: Boolean) {
        prefs?.edit()?.putBoolean("isDarkMode", isDark)?.apply()
    }
    
    fun toggleTheme() {
        isDarkMode = !isDarkMode
        saveThemePreference(isDarkMode)
    }
    
    fun setDarkMode(darkMode: Boolean) {
        isDarkMode = darkMode
        saveThemePreference(isDarkMode)
    }
}
