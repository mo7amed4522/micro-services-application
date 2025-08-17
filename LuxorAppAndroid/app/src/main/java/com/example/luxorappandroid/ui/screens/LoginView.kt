package com.example.luxorappandroid.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luxorappandroid.ui.components.*
import com.example.luxorappandroid.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun LoginView(
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    isDarkMode: Boolean = false,
    onNavigateToRegister: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    
    val colors = if (isDarkMode) DarkColors else LightColors
    
    val isFormValid = email.isNotEmpty() && password.isNotEmpty() &&
            isValidEmail(email) && isValidPassword(password)
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = if (isDarkMode) {
                        listOf(colors.Background, colors.SecondaryBackground)
                    } else {
                        listOf(colors.Background, LuxorTeal.copy(alpha = 0.05f))
                    }
                )
            )
    ) {
        // Header section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 60.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            LuxorLogo(size = 100f)
            
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Welcome Back",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.TextPrimary
                )
                
                Text(
                    text = "Sign in to your account",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = colors.TextSecondary
                )
            }
        }
        
        // Form section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            LuxorEmailTextField(
                email = email,
                onEmailChange = { email = it },
                isDarkMode = isDarkMode
            )
            
            LuxorPasswordTextField(
                password = password,
                onPasswordChange = { password = it },
                isDarkMode = isDarkMode
            )
            
            // Forgot password
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = { /* Forgot password */ }
                ) {
                    Text(
                        text = "Forgot Password?",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = LuxorTeal
                    )
                }
            }
            
            // Login button
            Button(
                onClick = {
                    isLoading = true
                    // Simulate login
                    kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch {
                        kotlinx.coroutines.delay(2000)
                        isLoading = false
                        onLoginSuccess()
                    }
                },
                enabled = isFormValid && !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            brush = if (isFormValid) {
                                Brush.linearGradient(listOf(LuxorTeal, LuxorGold))
                            } else {
                                Brush.linearGradient(
                                    listOf(
                                        Color.Gray.copy(alpha = 0.5f),
                                        Color.Gray.copy(alpha = 0.3f)
                                    )
                                )
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Text(
                            text = "Sign In",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }
            
            // Divider
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(
                    modifier = Modifier.weight(1f),
                    color = colors.TextSecondary.copy(alpha = 0.3f)
                )
                
                Text(
                    text = "OR",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = colors.TextSecondary,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                
                Divider(
                    modifier = Modifier.weight(1f),
                    color = colors.TextSecondary.copy(alpha = 0.3f)
                )
            }
            
            // Social login buttons
            SocialLoginButtons(isDarkMode = isDarkMode)
            
            // Create account section
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Divider(
                    color = colors.TextSecondary.copy(alpha = 0.2f),
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                
                OutlinedButton(
                    onClick = { onNavigateToRegister() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent
                    )
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.PersonAdd,
                            contentDescription = "Create account",
                            tint = colors.TextPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Create New Account",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.TextPrimary
                        )
                    }
                }
                
                // Terms and privacy
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "By creating an account, you agree to our",
                        fontSize = 12.sp,
                        color = colors.TextSecondary,
                        textAlign = TextAlign.Center
                    )
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        TextButton(
                            onClick = { /* Show terms */ }
                        ) {
                            Text(
                                text = "Terms of Service",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = LuxorTeal
                            )
                        }
                        
                        Text(
                            text = "and",
                            fontSize = 12.sp,
                            color = colors.TextSecondary
                        )
                        
                        TextButton(
                            onClick = { /* Show privacy policy */ }
                        ) {
                            Text(
                                text = "Privacy Policy",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = LuxorTeal
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

private fun isValidEmail(email: String): Boolean {
    val emailRegex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}".toRegex()
    return email.matches(emailRegex)
}

private fun isValidPassword(password: String): Boolean {
    val hasCapital = password.any { it.isUpperCase() }
    val hasMinLength = password.length >= 8
    val hasSpecial = password.any { !it.isLetterOrDigit() }
    return hasCapital && hasMinLength && hasSpecial
}
