package com.example.luxorappandroid.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luxorappandroid.ui.components.LuxorEmailTextField
import com.example.luxorappandroid.ui.components.LuxorPasswordTextField
import com.example.luxorappandroid.ui.theme.*
import androidx.compose.foundation.border
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

@Composable
fun RegisterView(
    isDarkMode: Boolean,
    onBackPressed: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showImagePicker by remember { mutableStateOf(false) }
    var profileImage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 40.dp)
    ) {
        item {
            // Custom App Bar
            CustomAppBar(
                isDarkMode = isDarkMode,
                onBackPressed = onBackPressed
            )
        }
        
        item {
            // Profile Photo Section
            ProfilePhotoSection(
                profileImage = profileImage,
                onImageClick = { showImagePicker = true },
                isDarkMode = isDarkMode
            )
        }
        
        item {
            // Registration Form
            RegistrationForm(
                fullName = fullName,
                onFullNameChange = { fullName = it },
                email = email,
                onEmailChange = { email = it },
                phoneNumber = phoneNumber,
                onPhoneNumberChange = { phoneNumber = it },
                password = password,
                onPasswordChange = { password = it },
                isLoading = isLoading,
                onRegister = {
                    isLoading = true
                    // Simulate registration
                    coroutineScope.launch {
                        delay(2000)
                        isLoading = false
                        onRegisterSuccess()
                    }
                },
                isDarkMode = isDarkMode
            )
        }
    }

    // Image Picker Dialog (simplified)
    if (showImagePicker) {
        ImagePickerDialog(
            onImageSelected = { imageUrl ->
                profileImage = imageUrl
                showImagePicker = false
            },
            onDismiss = { showImagePicker = false },
            isDarkMode = isDarkMode
        )
    }
}

@Composable
fun CustomAppBar(
    isDarkMode: Boolean,
    onBackPressed: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back Button
        IconButton(
            onClick = onBackPressed,
            modifier = Modifier
                .size(44.dp)
                .background(
                    color = if (isDarkMode) DarkColors.CardBackground else Color.White,
                    shape = CircleShape
                )
                .shadow(
                    elevation = 8.dp,
                    shape = CircleShape,
                    spotColor = if (isDarkMode) DarkColors.Shadow else LightColors.Shadow
                )
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = if (isDarkMode) DarkColors.TextPrimary else LightColors.TextPrimary
            )
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Title
        Text(
            text = "Create Account",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDarkMode) DarkColors.TextPrimary else LightColors.TextPrimary
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Logo
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(LuxorTeal, LuxorGold)
                    ),
                    shape = CircleShape
                )
                .shadow(
                    elevation = 8.dp,
                    shape = CircleShape,
                    spotColor = LuxorTeal.copy(alpha = 0.3f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Luxor Logo",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
fun ProfilePhotoSection(
    profileImage: String?,
    onImageClick: () -> Unit,
    isDarkMode: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp)
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            if (isDarkMode) DarkColors.CardBackground else Color.LightGray,
                            if (isDarkMode) DarkColors.InputBackground else Color.White
                        )
                    ),
                    shape = CircleShape
                )
                .border(
                    width = 3.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(LuxorTeal, LuxorGold)
                    ),
                    shape = CircleShape
                )
                .shadow(
                    elevation = 12.dp,
                    shape = CircleShape,
                    spotColor = if (isDarkMode) DarkColors.Shadow else LightColors.Shadow
                )
                .clickable { onImageClick() },
            contentAlignment = Alignment.Center
        ) {
            if (profileImage != null) {
                // Profile image would be displayed here
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = LuxorTeal,
                    modifier = Modifier.size(40.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Add Profile Photo",
                    tint = if (isDarkMode) DarkColors.TextSecondary else LightColors.TextSecondary,
                    modifier = Modifier.size(40.dp)
                )
            }
            
            // Camera Button
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(36.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(LuxorTeal, LuxorGold)
                        ),
                        shape = CircleShape
                    )
                    .shadow(
                        elevation = 8.dp,
                        shape = CircleShape,
                        spotColor = LuxorTeal.copy(alpha = 0.4f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Camera",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Add Profile Photo",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = if (isDarkMode) DarkColors.TextSecondary else LightColors.TextSecondary
        )
    }
}

@Composable
fun RegistrationForm(
    fullName: String,
    onFullNameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    isLoading: Boolean,
    onRegister: () -> Unit,
    isDarkMode: Boolean
) {
    val isFormValid = remember(fullName, email, phoneNumber, password) {
        fullName.isNotEmpty() && 
        email.isNotEmpty() && 
        phoneNumber.isNotEmpty() && 
        password.isNotEmpty() &&
        isValidEmail(email) &&
        isValidFullName(fullName) &&
        isValidPassword(password)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Full Name Field
        FullNameTextField(
            fullName = fullName,
            onFullNameChange = onFullNameChange,
            isDarkMode = isDarkMode
        )
        
        // Email Field
        LuxorEmailTextField(
            email = email,
            onEmailChange = onEmailChange,
            isDarkMode = isDarkMode
        )
        
        // Phone Number Field
        PhoneNumberTextField(
            phoneNumber = phoneNumber,
            onPhoneNumberChange = onPhoneNumberChange,
            isDarkMode = isDarkMode
        )
        
        // Password Field
        LuxorPasswordTextField(
            password = password,
            onPasswordChange = onPasswordChange,
            isDarkMode = isDarkMode
        )
        
        // Register Button
        Button(
            onClick = onRegister,
            enabled = isFormValid && !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFormValid) {
                    Color.Transparent
                } else {
                    Color.Gray.copy(alpha = 0.5f)
                }
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = if (isFormValid) {
                            Brush.linearGradient(
                                colors = listOf(LuxorTeal, LuxorGold)
                            )
                        } else {
                            Brush.linearGradient(
                                colors = listOf(
                                    Color.Gray.copy(alpha = 0.5f),
                                    Color.Gray.copy(alpha = 0.3f)
                                )
                            )
                        },
                        shape = RoundedCornerShape(12.dp)
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
                        text = "Create Account",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
        
        // Terms and Privacy
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "By creating an account, you agree to our",
                fontSize = 12.sp,
                color = if (isDarkMode) DarkColors.TextSecondary else LightColors.TextSecondary,
                textAlign = TextAlign.Center
            )
            
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(
                    onClick = { /* Show Terms */ }
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
                    color = if (isDarkMode) DarkColors.TextSecondary else LightColors.TextSecondary
                )
                
                TextButton(
                    onClick = { /* Show Privacy Policy */ }
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
}

@Composable
fun FullNameTextField(
    fullName: String,
    onFullNameChange: (String) -> Unit,
    isDarkMode: Boolean
) {
    var isEditing by remember { mutableStateOf(false) }
    var isValid by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = if (isDarkMode) DarkColors.InputBackground else Color.White,
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    width = if (isEditing) 2.dp else 1.dp,
                    brush = if (isEditing) {
                        Brush.linearGradient(
                            colors = listOf(LuxorTeal, LuxorGold)
                        )
                    } else if (!isValid) {
                        Brush.linearGradient(
                            colors = listOf(Color.Red.copy(alpha = 0.6f), Color.Red.copy(alpha = 0.3f))
                        )
                    } else {
                        Brush.linearGradient(
                            colors = listOf(
                                if (isDarkMode) DarkColors.Border else Color.Gray.copy(alpha = 0.2f),
                                if (isDarkMode) DarkColors.Border.copy(alpha = 0.5f) else Color.Gray.copy(alpha = 0.1f)
                            )
                        )
                    },
                    shape = RoundedCornerShape(16.dp)
                )
                .shadow(
                    elevation = if (isEditing) 12.dp else 6.dp,
                    shape = RoundedCornerShape(16.dp),
                    spotColor = if (isDarkMode) DarkColors.Shadow else LightColors.Shadow
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            color = if (isEditing) LuxorTeal.copy(alpha = 0.2f) else Color.Transparent,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isEditing) Icons.Default.PersonAdd else Icons.Default.Person,
                        contentDescription = "Full Name",
                        tint = if (isEditing) LuxorTeal else {
                            if (isDarkMode) DarkColors.TextSecondary else Color.Gray
                        },
                        modifier = Modifier.size(18.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // Text Field
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Full Name",
                        fontSize = if (isEditing || fullName.isNotEmpty()) 11.sp else 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isEditing) LuxorTeal else {
                            if (isDarkMode) DarkColors.TextSecondary else Color.Gray
                        }
                    )
                    
                    TextField(
                        value = fullName,
                        onValueChange = { 
                            onFullNameChange(it)
                            isValid = it.isEmpty() || isValidFullName(it)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            cursorColor = LuxorTeal,
                            focusedTextColor = if (isDarkMode) DarkColors.TextPrimary else LightColors.TextPrimary,
                            unfocusedTextColor = if (isDarkMode) DarkColors.TextPrimary else LightColors.TextPrimary
                        ),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        onFocusChanged = { isEditing = it.isFocused }
                    )
                }
                
                // Validation Icon
                if (fullName.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                color = if (isValid) Color.Green.copy(alpha = 0.2f) else Color.Red.copy(alpha = 0.2f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isValid) Icons.Default.CheckCircle else Icons.Default.Cancel,
                            contentDescription = "Validation",
                            tint = if (isValid) Color.Green else Color.Red,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
        
        // Error Message
        if (!isValid && fullName.isNotEmpty()) {
            Row(
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Error",
                    tint = Color.Red,
                    modifier = Modifier.size(12.dp)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = "Please enter your full name (at least 2 words)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Red
                )
            }
        }
    }
}

@Composable
fun PhoneNumberTextField(
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    isDarkMode: Boolean
) {
    var isEditing by remember { mutableStateOf(false) }
    var isValid by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = if (isDarkMode) DarkColors.InputBackground else Color.White,
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    width = if (isEditing) 2.dp else 1.dp,
                    brush = if (isEditing) {
                        Brush.linearGradient(
                            colors = listOf(LuxorTeal, LuxorGold)
                        )
                    } else if (!isValid) {
                        Brush.linearGradient(
                            colors = listOf(Color.Red.copy(alpha = 0.6f), Color.Red.copy(alpha = 0.3f))
                        )
                    } else {
                        Brush.linearGradient(
                            colors = listOf(
                                if (isDarkMode) DarkColors.Border else Color.Gray.copy(alpha = 0.2f),
                                if (isDarkMode) DarkColors.Border.copy(alpha = 0.5f) else Color.Gray.copy(alpha = 0.1f)
                            )
                        )
                    },
                    shape = RoundedCornerShape(16.dp)
                )
                .shadow(
                    elevation = if (isEditing) 12.dp else 6.dp,
                    shape = RoundedCornerShape(16.dp),
                    spotColor = if (isDarkMode) DarkColors.Shadow else LightColors.Shadow
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Country Code Button
                Button(
                    onClick = { /* Show country picker */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDarkMode) DarkColors.CardBackground else Color.LightGray
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🇺🇸", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "+1",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isDarkMode) DarkColors.TextPrimary else LightColors.TextPrimary
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Select Country",
                            tint = if (isDarkMode) DarkColors.TextSecondary else LightColors.TextSecondary,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                // Divider
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(30.dp)
                        .background(
                            color = if (isDarkMode) DarkColors.Border else Color.Gray.copy(alpha = 0.3f)
                        )
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                // Phone Number Field
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Phone Number",
                        fontSize = if (isEditing || phoneNumber.isNotEmpty()) 11.sp else 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isEditing) LuxorTeal else {
                            if (isDarkMode) DarkColors.TextSecondary else Color.Gray
                        }
                    )
                    
                    TextField(
                        value = phoneNumber,
                        onValueChange = { newValue -> 
                            onPhoneNumberChange(newValue)
                            isValid = newValue.isEmpty() || isValidPhoneNumber(newValue)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            cursorColor = LuxorTeal,
                            textColor = if (isDarkMode) DarkColors.TextPrimary else LightColors.TextPrimary
                        ),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        onFocusChanged = { isEditing = it.isFocused }
                    )
                }
                
                // Validation Icon
                if (phoneNumber.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                color = if (isValid) Color.Green.copy(alpha = 0.2f) else Color.Red.copy(alpha = 0.2f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isValid) Icons.Default.CheckCircle else Icons.Default.Cancel,
                            contentDescription = "Validation",
                            tint = if (isValid) Color.Green else Color.Red,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
        
        // Error Message
        if (!isValid && phoneNumber.isNotEmpty()) {
            Row(
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Error",
                    tint = Color.Red,
                    modifier = Modifier.size(12.dp)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = "Please enter a valid phone number",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Red
                )
            }
        }
    }
}

@Composable
fun ImagePickerDialog(
    onImageSelected: (String) -> Unit,
    onDismiss: () -> Unit,
    isDarkMode: Boolean
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Select Profile Photo",
                color = if (isDarkMode) DarkColors.TextPrimary else LightColors.TextPrimary
            )
        },
        text = {
            Text(
                text = "Choose how you want to add your profile photo",
                color = if (isDarkMode) DarkColors.TextSecondary else LightColors.TextSecondary
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    // Simulate image selection
                    onImageSelected("https://picsum.photos/200/200")
                }
            ) {
                Text("Camera", color = LuxorTeal)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    // Simulate image selection
                    onImageSelected("https://picsum.photos/200/200")
                }
            ) {
                Text("Gallery", color = LuxorTeal)
            }
        },
        containerColor = if (isDarkMode) DarkColors.CardBackground else LightColors.CardBackground
    )
}

private fun isValidEmail(email: String): Boolean {
    val emailRegex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}".toRegex()
    return emailRegex.matches(email)
}

private fun isValidFullName(name: String): Boolean {
    val trimmedName = name.trim()
    val nameComponents = trimmedName.split("\\s+".toRegex()).filter { it.isNotEmpty() }
    return nameComponents.size >= 2 && trimmedName.length >= 3
}

private fun isValidPassword(password: String): Boolean {
    return password.length >= 8 &&
           password.any { it.isUpperCase() } &&
           password.any { !it.isLetterOrDigit() }
}

private fun isValidPhoneNumber(number: String): Boolean {
    val cleanedNumber = number.replace(Regex("[^0-9]"), "")
    return cleanedNumber.length >= 7 && cleanedNumber.length <= 15
}
