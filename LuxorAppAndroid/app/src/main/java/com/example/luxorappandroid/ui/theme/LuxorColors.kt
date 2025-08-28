package com.example.luxorappandroid.ui.theme

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit


object LuxorColors {
    val pureGold = Color(red = 1.0f, green = 0.84f, blue = 0.0f)
    val richGold = Color(red = 0.85f, green = 0.65f, blue = 0.13f)
    val deepGold = Color(red = 0.72f, green = 0.53f, blue = 0.04f)


    val egyptianBlue = Color(red = 0.06f, green = 0.32f, blue = 0.73f)
    val papyrusBeige = Color(red = 0.96f, green = 0.87f, blue = 0.70f)
    val hieroglyphBrown = Color(red = 0.4f, green = 0.26f, blue = 0.13f)
    

    val luxorTeal = egyptianBlue


    object Light {
        val background = Color(red = 0.98f, green = 0.95f, blue = 0.88f)
        val secondaryBackground = papyrusBeige
        val textPrimary = hieroglyphBrown
        val textSecondary = Color(red = 0.5f, green = 0.4f, blue = 0.3f)
        val cardBackground = Color(red = 0.99f, green = 0.96f, blue = 0.90f)
        val shadow = hieroglyphBrown.copy(alpha = 0.15f)
        val border = richGold.copy(alpha = 0.3f)
    }
    

    object Dark {
        val background = Color(red = 0.08f, green = 0.06f, blue = 0.04f)
        val secondaryBackground = Color(red = 0.12f, green = 0.10f, blue = 0.08f)
        val textPrimary = pureGold
        val textSecondary = Color(red = 0.85f, green = 0.75f, blue = 0.55f)
        val cardBackground = Color(red = 0.15f, green = 0.12f, blue = 0.08f)

        val border = deepGold.copy(alpha = 0.4f)

    }
}


@Composable
fun rememberLuxorThemeManager(): LuxorThemeManager {
    val context = LocalContext.current
    return remember { LuxorThemeManager(context) }
}

class LuxorThemeManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("luxor_theme", Context.MODE_PRIVATE)
    
    private val _isDarkMode = mutableStateOf(prefs.getBoolean("isDarkMode", false))
    val isDarkMode: State<Boolean> = _isDarkMode
    
    fun toggleTheme() {
        val newValue = !_isDarkMode.value
        _isDarkMode.value = newValue
        prefs.edit { putBoolean("isDarkMode", newValue) }
    }

}