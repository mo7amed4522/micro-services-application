package com.example.luxorappandroid.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class ThemeManager {
    var isDarkMode by mutableStateOf(false)
        private set
    
    fun toggleTheme() {
        isDarkMode = !isDarkMode
    }
    
    fun setDarkMode(darkMode: Boolean) {
        isDarkMode = darkMode
    }
}
