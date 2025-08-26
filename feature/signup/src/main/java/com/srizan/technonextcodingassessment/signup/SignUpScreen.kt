package com.srizan.technonextcodingassessment.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.srizan.technonextcodingassessment.designsystem.theme.AppTheme
import com.srizan.technonextcodingassessment.domain.validation.PasswordStrength

@Composable
fun SignUpScreenContent(
    uiState: SignUpViewModel.UiState,
    onEmailChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onConfirmPasswordChange: (String) -> Unit = {},
    onRegisterClick: () -> Unit = {},
    onSignUpClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Header Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                Text(
                    text = "Create Account",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Sign up to get started",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Email Field
            OutlinedTextField(
                value = uiState.email,
                onValueChange = onEmailChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "Email input field" },
                label = { Text("Email") },
                leadingIcon = {
                    Icon(
                        Icons.Default.Email,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                singleLine = true,
                isError = uiState.emailError != null,
                supportingText = uiState.emailError?.let { { Text(it) } }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Field
            OutlinedTextField(
                value = uiState.password,
                onValueChange = onPasswordChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "Password input field" },
                label = { Text("Password") },
                leadingIcon = {
                    Icon(
                        Icons.Default.Key,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (isPasswordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                singleLine = true,
                isError = uiState.passwordError != null,
                supportingText = uiState.passwordError?.let { { Text(it) } }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Confirm Password Field
            OutlinedTextField(
                value = uiState.confirmPassword,
                onValueChange = onConfirmPasswordChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "Confirm password input field" },
                label = { Text("Confirm Password") },
                leadingIcon = {
                    Icon(
                        Icons.Default.Key,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                        Icon(
                            if (isConfirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (isConfirmPasswordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { 
                        focusManager.clearFocus()
                        if (uiState.isFormValid) onRegisterClick()
                    }
                ),
                singleLine = true,
                isError = uiState.confirmPasswordError != null,
                supportingText = uiState.confirmPasswordError?.let { { Text(it) } }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Password Criteria
            if (uiState.password.isNotEmpty()) {
                PasswordCriteria(
                    password = uiState.password,
                    passwordStrength = uiState.passwordStrength,
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Error Message
            uiState.errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.semantics { contentDescription = "Error message: $error" }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Sign Up Button
            Button(
                onClick = {
                    focusManager.clearFocus()
                    onRegisterClick()
                },
                enabled = uiState.isFormValid && !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .semantics { contentDescription = "Sign up button" }
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Creating Account...")
                } else {
                    Text(
                        "Sign Up",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sign In Section
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.semantics { contentDescription = "Sign in section" }
            ) {
                Text(
                    "Already have an account? ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                TextButton(
                    onClick = onSignUpClick,
                    enabled = !uiState.isLoading
                ) {
                    Text(
                        "Sign In",
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}


@Composable
fun PasswordCriteria(
    password: String,
    passwordStrength: PasswordStrength,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        PasswordCriterion(
            text = "At least 8 characters", 
            isValid = password.length >= 8
        )
        PasswordCriterion(
            text = "Contains uppercase letter", 
            isValid = password.any { it.isUpperCase() }
        )
        PasswordCriterion(
            text = "Contains lowercase letter", 
            isValid = password.any { it.isLowerCase() }
        )
        PasswordCriterion(
            text = "Contains number", 
            isValid = password.any { it.isDigit() }
        )
        PasswordCriterion(
            text = "Contains special character", 
            isValid = password.any { !it.isLetterOrDigit() }
        )

        Spacer(modifier = Modifier.height(8.dp))
        
        PasswordStrengthLabel(passwordStrength)
    }
}

@Composable
fun PasswordCriterion(
    text: String, 
    isValid: Boolean, 
    modifier: Modifier = Modifier
) {
    val icon = if (isValid) Icons.Default.CheckCircle else Icons.Default.Clear
    val color = if (isValid) {
        Color(0xFF4CAF50) // Success green
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Row(
        modifier = modifier.padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon, 
            contentDescription = if (isValid) "Criteria met" else "Criteria not met",
            tint = color,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text,
            color = color,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun PasswordStrengthLabel(passwordStrength: PasswordStrength) {
    val (strengthText, color) = when (passwordStrength) {
        PasswordStrength.WEAK -> "Weak" to Color.Red
        PasswordStrength.MODERATE -> "Moderate" to Color(0xFFFF9800) // Orange
        PasswordStrength.STRONG -> "Strong" to Color(0xFF4CAF50) // Green
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            "Strength: ",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            strengthText,
            color = color,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PasswordCriteriaPreview() {
    AppTheme {
        Surface {
            Column(modifier = Modifier.padding(16.dp)) {
                PasswordCriteria(
                    password = "123456", 
                    passwordStrength = PasswordStrength.WEAK
                )
                Spacer(modifier = Modifier.height(24.dp))
                PasswordCriteria(
                    password = "Strong#2025", 
                    passwordStrength = PasswordStrength.STRONG
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Sign Up Screen - Empty")
@Composable
private fun SignUpScreenEmptyPreview() {
    AppTheme {
        Surface {
            SignUpScreenContent(
                uiState = SignUpViewModel.UiState()
            )
        }
    }
}

@Preview(showBackground = true, name = "Sign Up Screen - With Data")
@Composable
private fun SignUpScreenWithDataPreview() {
    AppTheme {
        Surface {
            SignUpScreenContent(
                uiState = SignUpViewModel.UiState(
                    email = "user@example.com",
                    password = "StrongPass123!",
                    confirmPassword = "StrongPass123!",
                    isFormValid = true,
                    passwordStrength = PasswordStrength.STRONG
                )
            )
        }
    }
}

@Preview(showBackground = true, name = "Sign Up Screen - Loading")
@Composable
private fun SignUpScreenLoadingPreview() {
    AppTheme {
        Surface {
            SignUpScreenContent(
                uiState = SignUpViewModel.UiState(
                    email = "user@example.com",
                    password = "StrongPass123!",
                    confirmPassword = "StrongPass123!",
                    isLoading = true,
                    isFormValid = true,
                    passwordStrength = PasswordStrength.STRONG
                )
            )
        }
    }
}

@Preview(showBackground = true, name = "Sign Up Screen - Error")
@Composable
private fun SignUpScreenErrorPreview() {
    AppTheme {
        Surface {
            SignUpScreenContent(
                uiState = SignUpViewModel.UiState(
                    email = "invalid-email",
                    password = "weak",
                    confirmPassword = "different",
                    emailError = "Invalid email format",
                    passwordError = "Password too weak",
                    confirmPasswordError = "Passwords do not match",
                    errorMessage = "Please fix the errors above",
                    passwordStrength = PasswordStrength.WEAK
                )
            )
        }
    }
}