package com.example.luxorappandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.luxorappandroid.ui.screens.LoginView
import com.example.luxorappandroid.ui.screens.MainTabView
import com.example.luxorappandroid.ui.screens.RegisterView
import com.example.luxorappandroid.ui.theme.LuxorAppAndroidTheme
import com.example.luxorappandroid.ui.theme.ThemeManager
import com.example.luxorappandroid.ui.theme.LanguageManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeManager = remember { ThemeManager() }
            val languageManager = remember { LanguageManager() }
            
            LuxorAppAndroidTheme(
                darkTheme = themeManager.isDarkMode
            ) {
                                        var isLoggedIn by remember { mutableStateOf(false) }
                        var showRegister by remember { mutableStateOf(false) }
                        
                        if (isLoggedIn) {
                            MainTabView(
                                isDarkMode = themeManager.isDarkMode,
                                onThemeToggle = { themeManager.toggleTheme() }
                            )
                        } else if (showRegister) {
                            RegisterView(
                                themeManager = themeManager,
                                languageManager = languageManager,
                                onBackPressed = { showRegister = false },
                                onRegisterSuccess = { isLoggedIn = true }
                            )
                        } else {
                            LoginView(
                                themeManager = themeManager,
                                languageManager = languageManager,
                                onLoginSuccess = { isLoggedIn = true },
                                onNavigateToRegister = { showRegister = true }
                            )
                        }
            }
        }
    }
}