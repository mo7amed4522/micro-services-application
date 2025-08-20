package com.example.luxorappandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.luxorappandroid.ui.screens.*
import com.example.luxorappandroid.ui.theme.LuxorAppAndroidTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Install splash screen
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
    
    // Handle splash screen timing
    LaunchedEffect(Unit) {
        delay(100) // Small delay to ensure smooth transition
        showSplash = false
    }
    
    if (showSplash) {
        SplashScreen(
            onSplashComplete = {
                showSplash = false
            }
        )
    } else {
        NavHost(
            navController = navController,
            startDestination = "welcome",
            modifier = Modifier.fillMaxSize()
        ) {
            composable("welcome") {
                WelcomeScreen(
                    onGetStartedClick = {
                        navController.navigate("login") {
                            popUpTo("welcome") { inclusive = true }
                        }
                    },
                    onSignInClick = {
                        navController.navigate("login") {
                            popUpTo("welcome") { inclusive = true }
                        }
                    }
                )
            }
            
            composable("login") {
                LoginScreen(
                    onBackClick = {
                        navController.navigate("welcome") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    onLoginSuccess = {
                        navController.navigate("main") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    onCreateAccountClick = {
                        navController.navigate("create_account")
                    },
                    onForgotPasswordClick = {
                        // Handle forgot password
                    }
                )
            }
            
            composable("create_account") {
                CreateAccountScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onCreateSuccess = {
                        navController.navigate("main") {
                            popUpTo("create_account") { inclusive = true }
                        }
                    },
                    onSignInClick = {
                        navController.popBackStack()
                    }
                )
            }
            
            composable("main") {
                MainScreen()
            }
        }
    }
}