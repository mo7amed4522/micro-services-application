package com.example.luxorappandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.luxorappandroid.ui.welcome.WelcomeScreen
import com.example.luxorappandroid.ui.auth.LoginScreen
import com.example.luxorappandroid.ui.auth.RegisterScreen
import com.example.luxorappandroid.ui.main.MainScreen
import com.example.luxorappandroid.ui.settings.SettingsScreen
import com.example.luxorappandroid.ui.screens.SplashScreen
import com.example.luxorappandroid.ui.theme.LuxorAppAndroidTheme
import com.example.luxorappandroid.ui.theme.LuxorColors
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LuxorAppAndroidTheme {
                LuxorApp()
            }
        }
    }
}

@Composable
fun LuxorApp() {
    val navController = rememberNavController()
    var showSplash by remember { mutableStateOf(true) }
    var hasSeenWelcome by remember { mutableStateOf(false) }
    var isAuthenticated by remember { mutableStateOf(false) }

    if (showSplash) {
        SplashScreen(
            onSplashFinished = {
                showSplash = false
            }
        )
    } else {
        NavHost(
            navController = navController,
            startDestination = when {
                !hasSeenWelcome -> "welcome"
                isAuthenticated -> "main"
                else -> "login"
            },
            modifier = Modifier.fillMaxSize()
        ) {
            composable("welcome") {
                WelcomeScreen(
                    onGetStartedClick = {
                        hasSeenWelcome = true
                        navController.navigate("login") {
                            popUpTo("welcome") { inclusive = true }
                        }
                    },
                    onSkipClick = {
                        hasSeenWelcome = true
                        navController.navigate("login") {
                            popUpTo("welcome") { inclusive = true }
                        }
                    }
                )
            }
            composable("login") {
                LoginScreen(
                    onLoginSuccess = {
                        isAuthenticated = true
                        navController.navigate("main") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    onNavigateToRegister = {
                        navController.navigate("register")
                    }
                )
            }
            composable("register") {
                RegisterScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onSignInClick = {
                        navController.popBackStack()
                    },
                    onRegisterSuccess = {
                        isAuthenticated = true
                        navController.navigate("main") {
                            popUpTo("register") { inclusive = true }
                        }
                    }
                )
            }
            composable("main") {
                MainScreen(
                    onLogout = {
                        isAuthenticated = false
                        navController.navigate("login") {
                            popUpTo("main") { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}