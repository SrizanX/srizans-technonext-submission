package com.srizan.technonextcodingassessment.domain.validation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidationRulesTest {

    @Test
    fun `isValidEmail should return true for valid email addresses`() {
        // Valid email addresses
        assertTrue(ValidationRules.isValidEmail("test@example.com"))
        assertTrue(ValidationRules.isValidEmail("user.name@domain.co.uk"))
        assertTrue(ValidationRules.isValidEmail("user+tag@example.org"))
        assertTrue(ValidationRules.isValidEmail("firstname.lastname@subdomain.example.com"))
        assertTrue(ValidationRules.isValidEmail("user123@test123.co"))
    }

    @Test
    fun `isValidEmail should return false for invalid email addresses`() {
        // Invalid email addresses
        assertFalse(ValidationRules.isValidEmail(""))
        assertFalse(ValidationRules.isValidEmail(" "))
        assertFalse(ValidationRules.isValidEmail("plainaddress"))
        assertFalse(ValidationRules.isValidEmail("@domain.com"))
        assertFalse(ValidationRules.isValidEmail("user@"))
        assertFalse(ValidationRules.isValidEmail("user@domain"))
        assertFalse(ValidationRules.isValidEmail("user.domain.com"))
        assertFalse(ValidationRules.isValidEmail("user@domain."))
        assertFalse(ValidationRules.isValidEmail("user name@domain.com")) // space in local part
    }

    @Test
    fun `isStrongPassword should return true for strong passwords`() {
        assertTrue(ValidationRules.isStrongPassword("Password123!"))
        assertTrue(ValidationRules.isStrongPassword("MyStr0ng@Pass"))
        assertTrue(ValidationRules.isStrongPassword("Complex1#Password"))
    }

    @Test
    fun `isStrongPassword should return false for weak passwords`() {
        assertFalse(ValidationRules.isStrongPassword(""))
        assertFalse(ValidationRules.isStrongPassword("short"))
        assertFalse(ValidationRules.isStrongPassword("password")) // no uppercase, digits, special chars
        assertFalse(ValidationRules.isStrongPassword("PASSWORD")) // no lowercase, digits, special chars
        assertFalse(ValidationRules.isStrongPassword("Password")) // no digits, special chars
        assertFalse(ValidationRules.isStrongPassword("Password123")) // no special chars
    }

    @Test
    fun `calculatePasswordStrength should return correct strength levels`() {
        assertEquals(PasswordStrength.WEAK, ValidationRules.calculatePasswordStrength(""))
        assertEquals(PasswordStrength.WEAK, ValidationRules.calculatePasswordStrength("pass"))
        assertEquals(PasswordStrength.MODERATE, ValidationRules.calculatePasswordStrength("Password"))
        assertEquals(PasswordStrength.MODERATE, ValidationRules.calculatePasswordStrength("Password123"))
        assertEquals(PasswordStrength.STRONG, ValidationRules.calculatePasswordStrength("Password123!"))
    }

    @Test
    fun `doPasswordsMatch should return true when passwords match`() {
        assertTrue(ValidationRules.doPasswordsMatch("password", "password"))
        assertTrue(ValidationRules.doPasswordsMatch("ComplexPass123!", "ComplexPass123!"))
    }

    @Test
    fun `doPasswordsMatch should return false when passwords do not match or are empty`() {
        assertFalse(ValidationRules.doPasswordsMatch("password", "different"))
        assertFalse(ValidationRules.doPasswordsMatch("", ""))
        assertFalse(ValidationRules.doPasswordsMatch("password", ""))
        assertFalse(ValidationRules.doPasswordsMatch("", "password"))
    }
}
