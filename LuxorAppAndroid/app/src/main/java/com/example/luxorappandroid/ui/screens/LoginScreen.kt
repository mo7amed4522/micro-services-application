package com.example.luxorappandroid.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luxorappandroid.ui.components.CompactSocialLoginButtons
import com.example.luxorappandroid.ui.components.LuxorEmailTextField
import com.example.luxorappandroid.ui.components.LuxorFullNameTextField
import com.example.luxorappandroid.ui.components.LuxorGradientButton
import com.example.luxorappandroid.ui.components.LuxorLogoWithText
import com.example.luxorappandroid.ui.components.LuxorPasswordTextField
import com.example.luxorappandroid.ui.components.SocialLoginButtons
import com.example.luxorappandroid.ui.components.SocialLoginDivider
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
    
    // Background using theme-aware colors
    val luxorColors = if (isDarkTheme) LuxorColors.Dark else LuxorColors.Light
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
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
        
        // Main content with iOS-style layout
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header section with logo and welcome text
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(luxorColors.background)
                    .padding(horizontal = if (isTablet) 60.dp else 24.dp)
                    .padding(top = 60.dp, bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo
                LuxorLogoWithText(
                    logoSize = if (isTablet) 140.dp else 100.dp,
                    animated = true,
                    showParticles = false
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Welcome text
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Welcome Back",
                        fontSize = if (isTablet) 32.sp else 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = luxorColors.textPrimary,
                        textAlign = TextAlign.Center
                    )
                    
                    Text(
                        text = "Sign in to your account",
                        fontSize = if (isTablet) 18.sp else 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = luxorColors.textSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }
            
            // Scrollable form section
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = if (isTablet) 60.dp else 24.dp)
                    .padding(top = 20.dp)
            ) {
            
                // Login form with iOS-style spacing
                Column(
                    modifier = Modifier
                        .fillMaxWidth(if (isTablet) 0.8f else 1f)
                        .align(Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Email field with error handling
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        LuxorEmailTextField(
                            email = email,
                            onEmailChange = { 
                                email = it
                                emailError = ""
                            },
                            isError = emailError.isNotEmpty(),
                            errorMessage = emailError
                        )
                        
                        if (emailError.isNotEmpty()) {
                            Row {
                                Text(
                                    text = emailError,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = LuxorColors.errorRed
                                )
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                    
                    // Password field with error handling
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        LuxorPasswordTextField(
                            password = password,
                            onPasswordChange = { 
                                password = it
                                passwordError = ""
                            },
                            isError = passwordError.isNotEmpty(),
                            errorMessage = passwordError
                        )
                        
                        if (passwordError.isNotEmpty()) {
                            Row {
                                Text(
                                    text = passwordError,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = LuxorColors.errorRed
                                )
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                    
                    // Forgot password with iOS-style positioning
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = -8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "Forgot Password?",
                            color = LuxorColors.luxorTeal,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.clickable { onForgotPasswordClick() }
                        )
                    }
                    
                    // Login button with iOS-style gradient and validation
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

@OptIn(ExperimentalMaterial3Api::class)
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
                LuxorColors.deepGold,
                LuxorColors.pureGold
            )
        } else {
            listOf(
                LuxorColors.deepGold,
                LuxorColors.pureGold
            )
        }
    )
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(luxorColors.background)
    ) {
        // Top App Bar
        TopAppBar(
            title = { },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = luxorColors.textPrimary
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
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
                                // Simulate login process
                                onLoginSuccess()
                            }
                        },
                        isLoading = isLoading,
                        enabled = !isLoading,
                        modifier = Modifier.height(56.dp)
                    )
                    
                    // Social login divider
                    Row(
                        modifier = Modifier.padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Divider(
                            modifier = Modifier.weight(1f),
                            color = luxorColors.textSecondary.copy(alpha = 0.3f)
                        )
                        Text(
                            text = "OR",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = luxorColors.textSecondary,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Divider(
                            modifier = Modifier.weight(1f),
                            color = luxorColors.textSecondary.copy(alpha = 0.3f)
                        )
                    }
                    
                    // Social login buttons
                     SocialLoginButtons(
                         onGoogleClick = { /* Handle Google sign in */ },
                         onFacebookClick = { /* Handle Facebook sign in */ },
                         onAppleClick = { /* Handle Apple sign in */ }
                     )
                    
                    // Create account section
                    Column(
                        modifier = Modifier.padding(top = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Divider(
                            color = luxorColors.textSecondary.copy(alpha = 0.2f),
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                        
                        // Create account button with iOS-style outline
                        LuxorGradientButton(
                            text = "Create Account",
                            onClick = onCreateAccountClick,
                            modifier = Modifier.height(52.dp),
                            leadingIcon = Icons.Default.PersonAdd
                        )
                        
                        // Terms and privacy text
                        Column(
                            modifier = Modifier.padding(top = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "By creating an account, you agree to our",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                color = luxorColors.textSecondary
                            )
                            
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = "Terms of Service",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = LuxorColors.luxorTeal,
                                    textDecoration = TextDecoration.Underline,
                                    modifier = Modifier.clickable { /* Handle terms click */ }
                                )
                                
                                Text(
                                    text = "and",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = luxorColors.textSecondary
                                )
                                
                                Text(
                                    text = "Privacy Policy",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = LuxorColors.luxorTeal,
                                    textDecoration = TextDecoration.Underline,
                                    modifier = Modifier.clickable { /* Handle privacy click */ }
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
            
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