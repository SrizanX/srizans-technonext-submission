package com.srizan.technonextcodingassessment.signup.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.srizan.technonextcodingassessment.designsystem.R
import com.srizan.technonextcodingassessment.designsystem.theme.AppTheme
import com.srizan.technonextcodingassessment.domain.validation.PasswordStrength

@Composable
fun PasswordCriteria(
    password: String, passwordStrength: PasswordStrength, modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        PasswordCriterion(
            text = stringResource(R.string.signup_password_requirement_length),
            isValid = password.length >= 8
        )
        PasswordCriterion(
            text = stringResource(R.string.signup_password_requirement_uppercase),
            isValid = password.any { it.isUpperCase() })
        PasswordCriterion(
            text = "Contains lowercase letter", isValid = password.any { it.isLowerCase() })
        PasswordCriterion(
            text = "Contains number", isValid = password.any { it.isDigit() })
        PasswordCriterion(
            text = "Contains special character", isValid = password.any { !it.isLetterOrDigit() })

        Spacer(modifier = Modifier.height(8.dp))

        PasswordStrengthLabel(passwordStrength)
    }
}

@Composable
fun PasswordCriterion(
    text: String, isValid: Boolean, modifier: Modifier = Modifier
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
            text, color = color, style = MaterialTheme.typography.bodySmall
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
                    password = "123456", passwordStrength = PasswordStrength.WEAK
                )
                Spacer(modifier = Modifier.height(24.dp))
                PasswordCriteria(
                    password = "Strong#2025", passwordStrength = PasswordStrength.STRONG
                )
            }
        }
    }
}