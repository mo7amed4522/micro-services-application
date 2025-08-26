package com.example.luxorappandroid.ui.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import java.util.*
import androidx.core.content.edit

class SettingsManager(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("luxor_settings", Context.MODE_PRIVATE)
    

    private val _isDarkMode = mutableStateOf(prefs.getBoolean("isDarkMode", false))
    val isDarkMode: State<Boolean> = _isDarkMode
    

    private val _selectedLanguage = mutableStateOf(prefs.getString("selectedLanguage", "English") ?: "English")
    val selectedLanguage: State<String> = _selectedLanguage
    

    private val _selectedCurrency = mutableStateOf(prefs.getString("selectedCurrency", "USD") ?: "USD")
    val selectedCurrency: State<String> = _selectedCurrency
    

    private val _notificationsEnabled = mutableStateOf(prefs.getBoolean("notificationsEnabled", true))
    val notificationsEnabled: State<Boolean> = _notificationsEnabled
    

    private val _locationEnabled = mutableStateOf(prefs.getBoolean("locationEnabled", true))
    val locationEnabled: State<Boolean> = _locationEnabled
    
    fun toggleTheme() {
        val newValue = !_isDarkMode.value
        _isDarkMode.value = newValue
        prefs.edit { putBoolean("isDarkMode", newValue) }
    }
    
    fun setDarkMode(enabled: Boolean) {
        _isDarkMode.value = enabled
        prefs.edit { putBoolean("isDarkMode", enabled) }
    }
    
    fun setLanguage(language: String) {
        _selectedLanguage.value = language
        prefs.edit { putString("selectedLanguage", language) }
        

        updateAppLocale(language)
    }
    
    fun setCurrency(currency: String) {
        _selectedCurrency.value = currency
        prefs.edit { putString("selectedCurrency", currency) }
    }
    
    fun setNotificationsEnabled(enabled: Boolean) {
        _notificationsEnabled.value = enabled
        prefs.edit { putBoolean("notificationsEnabled", enabled) }
    }
    
    fun setLocationEnabled(enabled: Boolean) {
        _locationEnabled.value = enabled
        prefs.edit { putBoolean("locationEnabled", enabled) }
    }
    
    private fun updateAppLocale(language: String) {
        val locale = when (language) {
            "Arabic" -> Locale("ar")
            "French" -> Locale("fr")
            "Chinese" -> Locale("zh")
            else -> Locale("en")
        }
        
        val config = context.resources.configuration
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }
    
    companion object {
        val supportedLanguages = listOf("English", "Arabic", "French", "Chinese")
        val supportedCurrencies = listOf("USD", "EUR", "EGP", "GBP")
        
        fun getLanguageCode(language: String): String {
            return when (language) {
                "Arabic" -> "ar"
                "French" -> "fr"
                "Chinese" -> "zh"
                else -> "en"
            }
        }
    }
}

@Composable
fun rememberSettingsManager(): SettingsManager {
    val context = LocalContext.current
    return remember { SettingsManager(context) }
}