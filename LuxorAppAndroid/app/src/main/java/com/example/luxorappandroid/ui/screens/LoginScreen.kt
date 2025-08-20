package com.example.luxorappandroid.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luxorappandroid.ui.components.*
import com.example.luxorappandroid.ui.theme.LuxorColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onBackClick: () -> Unit = {},
    onLoginSuccess: () -> Unit = {},
    onCreateAccountClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val isDarkTheme = isSystemInDarkTheme()
    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600
    
    // State variables
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    
    // Background gradient
    val backgroundBrush = Brush.verticalGradient(
        colors = if (isDarkTheme) {
            listOf(
                LuxorColors.Dark.background,
                LuxorColors.Dark.surface
            )
        } else {
            listOf(
                LuxorColors.Light.background,
                LuxorColors.Light.surface
            )
        }
    )
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        // Top App Bar
        TopAppBar(
            title = { },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = if (isDarkTheme) LuxorColors.Dark.textPrimary else LuxorColors.Light.textPrimary
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = androidx.compose.ui.graphics.Color.Transparent
            )
        )
        
        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = if (isTablet) 64.dp else 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(if (isTablet) 120.dp else 80.dp))
            
            // Logo
            LuxorLogoWithText(
                logoSize = if (isTablet) 100.dp else 80.dp,
                animated = true,
                showParticles = false
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Welcome text
            Text(
                text = "Welcome Back",
                fontSize = if (isTablet) 32.sp else 28.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDarkTheme) LuxorColors.Dark.textPrimary else LuxorColors.Light.textPrimary,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = "Sign in to continue your journey",
                fontSize = if (isTablet) 18.sp else 16.sp,
                fontWeight = FontWeight.Normal,
                color = if (isDarkTheme) LuxorColors.Dark.textSecondary else LuxorColors.Light.textSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
            
            Spacer(modifier = Modifier.height(40.dp))
            
            // Login form
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDarkTheme) LuxorColors.Dark.cardBackground else LuxorColors.Light.cardBackground
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Email field
                    LuxorEmailTextField(
                        email = email,
                        onEmailChange = { 
                            email = it
                            emailError = ""
                        },
                        isError = emailError.isNotEmpty(),
                        errorMessage = emailError
                    )
                    
                    // Password field
                    LuxorPasswordTextField(
                        password = password,
                        onPasswordChange = { 
                            password = it
                            passwordError = ""
                        },
                        isError = passwordError.isNotEmpty(),
                        errorMessage = passwordError
                    )
                    
                    // Forgot password
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "Forgot Password?",
                            color = LuxorColors.pureGold,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable { onForgotPasswordClick() }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Login button
                    LuxorGradientButton(
                        text = "Sign In",
                        onClick = {
                            // Validate inputs
                            var hasError = false
                            
                            if (email.isBlank()) {
                                emailError = "Email is required"
                                hasError = true
                            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                emailError = "Please enter a valid email"
                                hasError = true
                            }
                            
                            if (password.isBlank()) {
                                passwordError = "Password is required"
                                hasError = true
                            } else if (password.length < 6) {
                                passwordError = "Password must be at least 6 characters"
                                hasError = true
                            }
                            
                            if (!hasError) {
                                isLoading = true
                                // Simulate login process
                                // In a real app, you would call your authentication service here
                                onLoginSuccess()
                            }
                        },
                        isLoading = isLoading,
                        enabled = !isLoading
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Social login divider
            SocialLoginDivider(text = "or continue with")
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Social login buttons
            if (isTablet) {
                CompactSocialLoginButtons(
                    onGoogleClick = { /* Handle Google login */ },
                    onFacebookClick = { /* Handle Facebook login */ },
                    onAppleClick = { /* Handle Apple login */ },
                    isLoading = isLoading
                )
            } else {
                SocialLoginButtons(
                    onGoogleClick = { /* Handle Google login */ },
                    onFacebookClick = { /* Handle Facebook login */ },
                    onAppleClick = { /* Handle Apple login */ },
                    isLoading = isLoading
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Create account link
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account? ",
                    color = if (isDarkTheme) LuxorColors.Dark.textSecondary else LuxorColors.Light.textSecondary,
                    fontSize = 14.sp
                )
                Text(
                    text = "Create Account",
                    color = LuxorColors.pureGold,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { onCreateAccountClick() }
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun CreateAccountScreen(
    onBackClick: () -> Unit = {},
    onCreateSuccess: () -> Unit = {},
    onSignInClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val isDarkTheme = isSystemInDarkTheme()
    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600
    
    // State variables
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var fullNameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }
    
    // Background gradient
    val backgroundBrush = Brush.verticalGradient(
        colors = if (isDarkTheme) {
            listOf(
                LuxorColors.Dark.background,
                LuxorColors.Dark.surface
            )
        } else {
            listOf(
                LuxorColors.Light.background,
                LuxorColors.Light.surface
            )
        }
    )
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        // Top App Bar
        TopAppBar(
            title = { },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = if (isDarkTheme) LuxorColors.Dark.textPrimary else LuxorColors.Light.textPrimary
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = androidx.compose.ui.graphics.Color.Transparent
            )
        )
        
        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = if (isTablet) 64.dp else 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(if (isTablet) 80.dp else 60.dp))
            
            // Logo
            LuxorLogoWithText(
                logoSize = if (isTablet) 80.dp else 60.dp,
                animated = true,
                showParticles = false
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Welcome text
            Text(
                text = "Create Account",
                fontSize = if (isTablet) 32.sp else 28.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDarkTheme) LuxorColors.Dark.textPrimary else LuxorColors.Light.textPrimary,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = "Join us and discover ancient wonders",
                fontSize = if (isTablet) 18.sp else 16.sp,
                fontWeight = FontWeight.Normal,
                color = if (isDarkTheme) LuxorColors.Dark.textSecondary else LuxorColors.Light.textSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Create account form
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDarkTheme) LuxorColors.Dark.cardBackground else LuxorColors.Light.cardBackground
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Full name field
                    LuxorFullNameTextField(
                        fullName = fullName,
                        onFullNameChange = { 
                            fullName = it
                            fullNameError = ""
                        },
                        isError = fullNameError.isNotEmpty(),
                        errorMessage = fullNameError
                    )
                    
                    // Email field
                    LuxorEmailTextField(
                        email = email,
                        onEmailChange = { 
                            email = it
                            emailError = ""
                        },
                        isError = emailError.isNotEmpty(),
                        errorMessage = emailError
                    )
                    
                    // Password field
                    LuxorPasswordTextField(
                        password = password,
                        onPasswordChange = { 
                            password = it
                            passwordError = ""
                        },
                        isError = passwordError.isNotEmpty(),
                        errorMessage = passwordError
                    )
                    
                    // Confirm password field
                    LuxorPasswordTextField(
                        password = confirmPassword,
                        onPasswordChange = { 
                            confirmPassword = it
                            confirmPasswordError = ""
                        },
                        label = "Confirm Password",
                        isError = confirmPasswordError.isNotEmpty(),
                        errorMessage = confirmPasswordError
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Create account button
                    LuxorGradientButton(
                        text = "Create Account",
                        onClick = {
                            // Validate inputs
                            var hasError = false
                            
                            if (fullName.isBlank()) {
                                fullNameError = "Full name is required"
                                hasError = true
                            }
                            
                            if (email.isBlank()) {
                                emailError = "Email is required"
                                hasError = true
                            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                emailError = "Please enter a valid email"
                                hasError = true
                            }
                            
                            if (password.isBlank()) {
                                passwordError = "Password is required"
                                hasError = true
                            } else if (password.length < 6) {
                                passwordError = "Password must be at least 6 characters"
                                hasError = true
                            }
                            
                            if (confirmPassword.isBlank()) {
                                confirmPasswordError = "Please confirm your password"
                                hasError = true
                            } else if (password != confirmPassword) {
                                confirmPasswordError = "Passwords do not match"
                                hasError = true
                            }
                            
                            if (!hasError) {
                                isLoading = true
                                // Simulate account creation process
                                onCreateSuccess()
                            }
                        },
                        isLoading = isLoading,
                        enabled = !isLoading
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Sign in link
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already have an account? ",
                    color = if (isDarkTheme) LuxorColors.Dark.textSecondary else LuxorColors.Light.textSecondary,
                    fontSize = 14.sp
                )
                Text(
                    text = "Sign In",
                    color = LuxorColors.pureGold,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { onSignInClick() }
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}