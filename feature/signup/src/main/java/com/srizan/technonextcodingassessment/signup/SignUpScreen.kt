package com.srizan.technonextcodingassessment.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.srizan.technonextcodingassessment.designsystem.theme.AppTheme

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
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, alignment = Alignment.CenterVertically)
    ) {
        Text(
            "Sign Up", style = MaterialTheme.typography.headlineMedium, modifier = modifier
        )

        OutlinedTextField(
            value = uiState.email,
            onValueChange = onEmailChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Email") })

        OutlinedTextField(
            value = uiState.password,
            onValueChange = onPasswordChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Password") })

        OutlinedTextField(
            value = uiState.confirmPassword,
            onValueChange = onConfirmPasswordChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Confirm Password") })

        PasswordCriteria(password = uiState.password, modifier = Modifier.align(Alignment.Start))

        uiState.errorMessage?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }
        OutlinedButton(
            onClick = onRegisterClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        ) {
            Text(if (uiState.isLoading) "Signing Up..." else "Sign Up")
        }


        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Already have an account? ")
            TextButton(onClick = onSignUpClick) {
                Text("Sign In")
            }
        }
    }
}


@Composable
fun PasswordCriteria(
    password: String, modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        PasswordCriterion(
            text = "At least 6 characters", isValid = password.length >= 6
        )
        PasswordCriterion(
            text = "Contains uppercase letter", isValid = password.any { it.isUpperCase() })
        PasswordCriterion(
            text = "Contains lowercase letter", isValid = password.any { it.isLowerCase() })
        PasswordCriterion(
            text = "Contains number", isValid = password.any { it.isDigit() })
        PasswordCriterion(
            text = "Contains special character", isValid = password.any { !it.isLetterOrDigit() })


        PasswordStrengthLabel(password)
    }
}

@Composable
fun PasswordCriterion(
    text: String, isValid: Boolean, modifier: Modifier = Modifier
) {
    val icon = if (isValid) Icons.Default.CheckCircle else Icons.Default.Clear
    val color = if (isValid) Color(0xFF4CAF50) else Color.Gray

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = color)
        Text(text, color = color)
    }
}

@Composable
fun PasswordStrengthLabel(password: String) {
    val rulesPassed = listOf(
        password.length >= 6,
        password.any { it.isUpperCase() },
        password.any { it.isLowerCase() },
        password.any { it.isDigit() },
        password.any { !it.isLetterOrDigit() },
    ).count { it }

    val strength = when (rulesPassed) {
        in 0..2 -> "Weak"
        3, 4 -> "Moderate"
        5 -> "Strong"
        else -> "Weak"
    }

    val color = when (strength) {
        "Weak" -> Color.Red
        "Moderate" -> Color.Yellow
        "Strong" -> Color(0xFF4CAF50)
        else -> Color.Gray
    }

    if (password.isNotEmpty()) Text("Strength: $strength", color = color)
}

@Preview(showBackground = true)
@Composable
private fun PasswordCriteriaPreview() {
    AppTheme {
        Surface {
            Column(modifier = Modifier.padding(16.dp)) {
                PasswordCriteria(password = "123456")
                Spacer(modifier = Modifier.height(24.dp))
                PasswordCriteria(password = "Strong#2025")
            }
        }
    }
}