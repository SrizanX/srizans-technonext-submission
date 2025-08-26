package com.srizan.technonextcodingassessment.domain.validation

import android.util.Patterns

/**
 * Centralized validation rules to ensure consistency across UI and data layers
 */
object ValidationRules {
    
    /**
     * Email validation using Android's built-in pattern matcher
     */
    fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    
    /**
     * Strong password validation requiring:
     * - At least 8 characters
     * - At least one uppercase letter
     * - At least one lowercase letter  
     * - At least one digit
     * - At least one special character
     */
    fun isStrongPassword(password: String): Boolean {
        return password.length >= 8 && 
               password.any { it.isUpperCase() } &&
               password.any { it.isLowerCase() } &&
               password.any { it.isDigit() } && 
               password.any { !it.isLetterOrDigit() }
    }
    
    /**
     * Calculate password strength based on criteria met
     */
    fun calculatePasswordStrength(password: String): PasswordStrength {
        val criteria = listOf(
            password.length >= 8,
            password.any { it.isUpperCase() },
            password.any { it.isLowerCase() },
            password.any { it.isDigit() },
            password.any { !it.isLetterOrDigit() }
        ).count { it }

        return when (criteria) {
            in 0..2 -> PasswordStrength.WEAK
            3, 4 -> PasswordStrength.MODERATE
            5 -> PasswordStrength.STRONG
            else -> PasswordStrength.WEAK
        }
    }
    
    /**
     * Password confirmation validation
     */
    fun doPasswordsMatch(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword && password.isNotEmpty()
    }
}

enum class PasswordStrength {
    WEAK, MODERATE, STRONG
}
