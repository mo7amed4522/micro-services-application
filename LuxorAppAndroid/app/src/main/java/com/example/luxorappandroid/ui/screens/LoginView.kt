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
import com.example.luxorappandroid.data.LanguageManager
import kotlinx.coroutines.launch

@Composable
fun LoginView(
    onLoginSuccess: () -> Unit,
    themeManager: ThemeManager,
    languageManager: LanguageManager,
    modifier: Modifier = Modifier,
    onNavigateToRegister: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    
    val colors = if (themeManager.isDarkMode) LuxorColors.Dark else LuxorColors.Light
    
    val isFormValid = email.isNotEmpty() && password.isNotEmpty() &&
            isValidEmail(email) && isValidPassword(password)
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = if (themeManager.isDarkMode) {
                        listOf(colors.background, colors.secondaryBackground)
                    } else {
                        listOf(colors.background, colors.primary.copy(alpha = 0.05f))
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
            LuxorLogo(size = 100f, themeManager = themeManager)
            
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = LocalizedStrings.welcome(languageManager.currentLanguage),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )
                
                Text(
                    text = LocalizedStrings.signIn(languageManager.currentLanguage),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = colors.textSecondary
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
                themeManager = themeManager,
                languageManager = languageManager
            )
            
            LuxorPasswordTextField(
                password = password,
                onPasswordChange = { password = it },
                themeManager = themeManager,
                languageManager = languageManager
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
                        text = LocalizedStrings.forgotPassword(languageManager.currentLanguage),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = colors.primary
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
                                Brush.linearGradient(listOf(colors.primary, colors.secondary))
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
                            text = LocalizedStrings.signIn(languageManager.currentLanguage),
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
                    color = colors.textSecondary.copy(alpha = 0.3f)
                )
                
                Text(
                    text = "OR",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = colors.textSecondary,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                
                Divider(
                    modifier = Modifier.weight(1f),
                    color = colors.textSecondary.copy(alpha = 0.3f)
                )
            }
            
            // Social login buttons
            SocialLoginButtons(themeManager = themeManager, languageManager = languageManager)
            
            // Create account section
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Divider(
                    color = colors.textSecondary.copy(alpha = 0.2f),
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
                            contentDescription = LocalizedStrings.signUp(languageManager.currentLanguage),
                            tint = colors.textPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = LocalizedStrings.signUp(languageManager.currentLanguage),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.textPrimary
                        )
                    }
                }
                
                // Terms and privacy
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = LocalizedStrings.termsAgreement(languageManager.currentLanguage),
                        fontSize = 12.sp,
                        color = colors.textSecondary,
                        textAlign = TextAlign.Center
                    )
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        TextButton(
                            onClick = { /* Show terms */ }
                        ) {
                            Text(
                                text = LocalizedStrings.termsOfService(languageManager.currentLanguage),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = colors.primary
                            )
                        }
                        
                        Text(
                            text = LocalizedStrings.and(languageManager.currentLanguage),
                            fontSize = 12.sp,
                            color = colors.textSecondary
                        )
                        
                        TextButton(
                            onClick = { /* Show privacy policy */ }
                        ) {
                            Text(
                                text = LocalizedStrings.privacyPolicy(languageManager.currentLanguage),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = colors.primary
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
