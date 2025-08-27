package com.srizan.technonextcodingassessment.data.repository

import com.srizan.technonextcodingassessment.cache.dao.UserDao
import com.srizan.technonextcodingassessment.cache.entity.UserEntity
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner

/**
 * Tests for AuthenticationRepositoryImpl - Real authentication business logic
 * These tests are meaningful because they test actual validation, database operations, and error handling
 */
@RunWith(RobolectricTestRunner::class)
class AuthenticationRepositoryImplTest {

    private val mockUserDao = mock<UserDao>()
    private val repository = AuthenticationRepositoryImpl(mockUserDao)

    @Test
    fun `signUp should succeed with valid email and password`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "#validPassword123"
        whenever(mockUserDao.doesUserExist(email)).thenReturn(false)

        // When
        val result = repository.signUp(email, password)

        // Then
        assertTrue("Sign up should succeed", result.isSuccess)
        verify(mockUserDao).doesUserExist(email)
        verify(mockUserDao).insertUser(UserEntity(email = email, password = password))
    }

    @Test
    fun `signUp should fail with invalid email format`() = runTest {
        // Given
        val invalidEmail = "invalid-email"
        val password = "validPassword123"

        // When
        val result = repository.signUp(invalidEmail, password)

        // Then
        assertTrue("Sign up should fail", result.isFailure)
        assertEquals("Invalid email format", result.exceptionOrNull()?.message)
        verify(mockUserDao, never()).doesUserExist(any())
        verify(mockUserDao, never()).insertUser(any())
    }

    @Test
    fun `signUp should fail when user already exists`() = runTest {
        // Given
        val email = "existing@example.com"
        val password = "#validPassword123"
        whenever(mockUserDao.doesUserExist(email)).thenReturn(true)

        // When
        val result = repository.signUp(email, password)

        // Then
        assertTrue("Sign up should fail", result.isFailure)
        assertEquals("Email already registered", result.exceptionOrNull()?.message)
        verify(mockUserDao).doesUserExist(email)
        verify(mockUserDao, never()).insertUser(any())
    }

    @Test
    fun `signUp should handle database insertion error`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "#validPassword123"
        whenever(mockUserDao.doesUserExist(email)).thenReturn(false)
        whenever(mockUserDao.insertUser(any())).thenThrow(RuntimeException("Database error"))

        // When
        val result = repository.signUp(email, password)

        // Then
        assertTrue("Sign up should fail", result.isFailure)
        assertTrue(
            "Error message should contain database error",
            result.exceptionOrNull()?.message?.contains("Registration failed") == true
        )
    }

    @Test
    fun `signIn should succeed with correct credentials`() = runTest {
        // Given
        val email = "user@example.com"
        val password = "#validPassword123"
        val userEntity = UserEntity(email = email, password = password)
        whenever(mockUserDao.getUserByEmail(email)).thenReturn(userEntity)

        // When
        val result = repository.signIn(email, password)

        // Then
        assertTrue("Sign in should succeed", result.isSuccess)
        verify(mockUserDao).getUserByEmail(email)
    }

    @Test
    fun `signIn should fail with non-existent user`() = runTest {
        // Given
        val email = "nonexistent@example.com"
        val password = "somePassword"
        whenever(mockUserDao.getUserByEmail(email)).thenReturn(null)

        // When
        val result = repository.signIn(email, password)

        // Then
        assertTrue("Sign in should fail", result.isFailure)
        assertEquals("User doesn't exist", result.exceptionOrNull()?.message)
        verify(mockUserDao).getUserByEmail(email)
    }

    @Test
    fun `signIn should fail with incorrect password`() = runTest {
        // Given
        val email = "user@example.com"
        val correctPassword = "correctPassword"
        val wrongPassword = "wrongPassword"
        val userEntity = UserEntity(email = email, password = correctPassword)
        whenever(mockUserDao.getUserByEmail(email)).thenReturn(userEntity)

        // When
        val result = repository.signIn(email, wrongPassword)

        // Then
        assertTrue("Sign in should fail", result.isFailure)
        assertEquals("Invalid credentials", result.exceptionOrNull()?.message)
        verify(mockUserDao).getUserByEmail(email)
    }

    @Test
    fun `deleteAccount should succeed when user exists`() = runTest {
        // Given
        val email = "user@example.com"
        whenever(mockUserDao.deleteUserByEmail(email)).thenReturn(1) // 1 row affected

        // When
        val result = repository.deleteAccount(email)

        // Then
        assertTrue("Delete should succeed", result.isSuccess)
        verify(mockUserDao).deleteUserByEmail(email)
    }

    @Test
    fun `deleteAccount should fail when user does not exist`() = runTest {
        // Given
        val email = "nonexistent@example.com"
        whenever(mockUserDao.deleteUserByEmail(email)).thenReturn(0) // 0 rows affected

        // When
        val result = repository.deleteAccount(email)

        // Then
        assertTrue("Delete should fail", result.isFailure)
        assertEquals("User not found", result.exceptionOrNull()?.message)
        verify(mockUserDao).deleteUserByEmail(email)
    }

    @Test
    fun `deleteAccount should handle database error`() = runTest {
        // Given
        val email = "user@example.com"
        whenever(mockUserDao.deleteUserByEmail(email)).thenThrow(RuntimeException("Database error"))

        // When
        val result = repository.deleteAccount(email)

        // Then
        assertTrue("Delete should fail", result.isFailure)
        assertTrue(
            "Error message should contain deletion failed",
            result.exceptionOrNull()?.message?.contains("Deletion failed") == true
        )
    }

    @Test
    fun `signUp should accept various valid email formats`() = runTest {
        val validEmails = listOf(
            "simple@example.com",
            "user.name@domain.co.uk",
            "user+tag@example.org",
            "123@numbers.com"
        )

        for (email in validEmails) {
            // Given
            whenever(mockUserDao.doesUserExist(email)).thenReturn(false)

            // When
            val result = repository.signUp(email, "#validPassword123")

            // Then
            assertTrue("$email should be valid", result.isSuccess)
        }
    }

    @Test
    fun `signUp should reject clearly invalid email formats`() = runTest {
        val invalidEmails = listOf(
            "invalid-email",      // no @ symbol
            "@domain.com",        // no local part
            "user@",              // no domain
            ""                    // empty
        )

        for (email in invalidEmails) {
            // When
            val result = repository.signUp(email, "#validPassword123")

            // Then
            assertTrue("$email should be invalid", result.isFailure)
            assertEquals("Invalid email format", result.exceptionOrNull()?.message)
        }
    }

    @Test
    fun `signIn should handle empty credentials`() = runTest {
        // Test empty email
        val resultEmptyEmail = repository.signIn("", "password")
        assertTrue("Empty email should fail", resultEmptyEmail.isFailure)

        // Test empty password
        whenever(mockUserDao.getUserByEmail("test@example.com")).thenReturn(
            UserEntity("test@example.com", "actualPassword")
        )
        val resultEmptyPassword = repository.signIn("test@example.com", "")
        assertTrue("Empty password should fail", resultEmptyPassword.isFailure)
    }

}
