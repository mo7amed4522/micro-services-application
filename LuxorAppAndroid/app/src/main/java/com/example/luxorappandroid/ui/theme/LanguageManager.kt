package com.example.luxorappandroid.ui.theme

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.LayoutDirection
import java.util.Locale

/**
 * Supported languages in the Luxor app
 */
enum class SupportedLanguage(val code: String, val displayName: String, val isRTL: Boolean) {
    ENGLISH("en", "English", false),
    ARABIC("ar", "العربية", true),
    FRENCH("fr", "Français", false),
    CHINESE("zh", "中文", false);
    
    companion object {
        fun fromCode(code: String): SupportedLanguage {
            return values().find { it.code == code } ?: ENGLISH
        }
    }
}

/**
 * Language Manager class that handles language switching and RTL support
 * with persistent storage using SharedPreferences
 */
class LanguageManager(private val context: Context? = null) {
    private val prefs: SharedPreferences? = context?.getSharedPreferences("luxor_language_prefs", Context.MODE_PRIVATE)
    
    var currentLanguage by mutableStateOf(loadLanguagePreference())
        private set
    
    val layoutDirection: LayoutDirection
        get() = if (currentLanguage.isRTL) LayoutDirection.Rtl else LayoutDirection.Ltr
    
    private fun loadLanguagePreference(): SupportedLanguage {
        val savedCode = prefs?.getString("currentLanguage", SupportedLanguage.ENGLISH.code)
        return SupportedLanguage.fromCode(savedCode ?: SupportedLanguage.ENGLISH.code)
    }
    
    private fun saveLanguagePreference(language: SupportedLanguage) {
        prefs?.edit()?.putString("currentLanguage", language.code)?.apply()
    }
    
    fun setLanguage(language: SupportedLanguage) {
        currentLanguage = language
        saveLanguagePreference(language)
        
        // Update app locale if context is available
        context?.let { ctx ->
            val locale = Locale(language.code)
            Locale.setDefault(locale)
            val config = ctx.resources.configuration
            config.setLocale(locale)
            ctx.resources.updateConfiguration(config, ctx.resources.displayMetrics)
        }
    }
    
    fun getCurrentLanguageCode(): String = currentLanguage.code
    
    fun getCurrentLanguageDisplayName(): String = currentLanguage.displayName
    
    fun isRTL(): Boolean = currentLanguage.isRTL
    
    fun getAllLanguages(): List<SupportedLanguage> = SupportedLanguage.values().toList()
}