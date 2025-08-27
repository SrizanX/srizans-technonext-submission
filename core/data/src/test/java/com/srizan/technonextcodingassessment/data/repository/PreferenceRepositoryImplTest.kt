package com.srizan.technonextcodingassessment.data.repository

import com.srizan.technonextcodingassessment.domain.datasource.PreferenceDataSource
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.fail
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

/**
 * Tests for PreferenceRepositoryImpl - Real preference coordination logic
 * These tests are meaningful because they test data transformation and coordination between layers
 */
class PreferenceRepositoryImplTest {

    private val mockPreferenceDataSource = mock<PreferenceDataSource>()
    private val repository = PreferenceRepositoryImpl(mockPreferenceDataSource)

    @Test
    fun `isUserLoggedIn should delegate to datasource`() = runTest {
        // Given
        whenever(mockPreferenceDataSource.isUserLoggedIn()).thenReturn(flowOf(true))

        // When
        val result = repository.isUserLoggedIn().toList()

        // Then
        assertEquals(1, result.size)
        assertEquals(true, result[0])
        verify(mockPreferenceDataSource).isUserLoggedIn()
    }

    @Test
    fun `isUserLoggedIn should handle null values from datasource`() = runTest {
        // Given
        whenever(mockPreferenceDataSource.isUserLoggedIn()).thenReturn(flowOf(null))

        // When
        val result = repository.isUserLoggedIn().toList()

        // Then
        assertEquals(1, result.size)
        assertNull(result[0])
        verify(mockPreferenceDataSource).isUserLoggedIn()
    }

    @Test
    fun `setUserLoggedInStatus should delegate to datasource`() = runTest {
        // Given
        val loggedIn = true

        // When
        repository.setUserLoggedInStatus(loggedIn)

        // Then
        verify(mockPreferenceDataSource).setUserLoggedInStatus(true)
    }

    @Test
    fun `getUserEmail should transform null to empty string`() = runTest {
        // Given - DataSource returns null
        whenever(mockPreferenceDataSource.getUserEmail()).thenReturn(flowOf(null))

        // When
        val result = repository.getUserEmail().toList()

        // Then
        assertEquals(1, result.size)
        assertEquals("", result[0]) // null transformed to empty string
        verify(mockPreferenceDataSource).getUserEmail()
    }

    @Test
    fun `getUserEmail should pass through non-null values`() = runTest {
        // Given - DataSource returns actual email
        val email = "user@example.com"
        whenever(mockPreferenceDataSource.getUserEmail()).thenReturn(flowOf(email))

        // When
        val result = repository.getUserEmail().toList()

        // Then
        assertEquals(1, result.size)
        assertEquals(email, result[0])
        verify(mockPreferenceDataSource).getUserEmail()
    }

    @Test
    fun `getUserEmail should handle multiple emissions correctly`() = runTest {
        // Given - DataSource emits multiple values including null
        whenever(mockPreferenceDataSource.getUserEmail()).thenReturn(
            flowOf(null, "first@example.com", null, "second@example.com")
        )

        // When
        val result = repository.getUserEmail().toList()

        // Then
        assertEquals(4, result.size)
        assertEquals("", result[0])              // null -> ""
        assertEquals("first@example.com", result[1])   // pass through
        assertEquals("", result[2])              // null -> ""  
        assertEquals("second@example.com", result[3])  // pass through
    }

    @Test
    fun `setUserEmail should delegate to datasource`() = runTest {
        // Given
        val email = "test@example.com"

        // When
        repository.setUserEmail(email)

        // Then
        verify(mockPreferenceDataSource).setUserEmail(email)
    }

    @Test
    fun `setUserEmail should handle empty string`() = runTest {
        // Given
        val emptyEmail = ""

        // When
        repository.setUserEmail(emptyEmail)

        // Then
        verify(mockPreferenceDataSource).setUserEmail("")
    }

    @Test
    fun `clearPreferences should delegate to datasource`() = runTest {
        // When
        repository.clearPreferences()

        // Then
        verify(mockPreferenceDataSource).clearPreferences()
    }

    @Test
    fun `multiple operations should work independently`() = runTest {
        // Given
        whenever(mockPreferenceDataSource.isUserLoggedIn()).thenReturn(flowOf(false))
        whenever(mockPreferenceDataSource.getUserEmail()).thenReturn(flowOf("test@example.com"))

        // When
        val loginStatus = repository.isUserLoggedIn().toList()
        repository.setUserLoggedInStatus(true)
        val email = repository.getUserEmail().toList()
        repository.setUserEmail("new@example.com")
        repository.clearPreferences()

        // Then
        assertEquals(false, loginStatus[0])
        assertEquals("test@example.com", email[0])
        
        verify(mockPreferenceDataSource).isUserLoggedIn()
        verify(mockPreferenceDataSource).setUserLoggedInStatus(true)
        verify(mockPreferenceDataSource).getUserEmail()
        verify(mockPreferenceDataSource).setUserEmail("new@example.com")
        verify(mockPreferenceDataSource).clearPreferences()
    }

    @Test
    fun `repository should handle datasource errors gracefully`() = runTest {
        // Given - DataSource throws exception
        whenever(mockPreferenceDataSource.setUserEmail(any())).thenThrow(RuntimeException("DataSource error"))

        // When & Then - Exception should propagate (this is expected behavior)
        try {
            repository.setUserEmail("test@example.com")
            fail("Exception should have been thrown")
        } catch (e: RuntimeException) {
            assertEquals("DataSource error", e.message)
        }
    }

    @Test
    fun `getUserEmail null-to-empty transformation should be consistent`() = runTest {
        // Test the specific business logic: null email from datasource becomes empty string
        
        // Given - Various null scenarios
        whenever(mockPreferenceDataSource.getUserEmail()).thenReturn(flowOf(null))
        
        // When
        val result1 = repository.getUserEmail().toList()
        
        // Then
        assertEquals("", result1[0])
        
        // Given - Mixed null and non-null
        whenever(mockPreferenceDataSource.getUserEmail()).thenReturn(
            flowOf(null, "email@test.com", null)
        )
        
        // When  
        val result2 = repository.getUserEmail().toList()
        
        // Then
        assertEquals(3, result2.size)
        assertEquals("", result2[0])
        assertEquals("email@test.com", result2[1])
        assertEquals("", result2[2])
    }

    @Test
    fun `setUserLoggedInStatus should handle both true and false`() = runTest {
        // When
        repository.setUserLoggedInStatus(true)
        repository.setUserLoggedInStatus(false)

        // Then
        verify(mockPreferenceDataSource).setUserLoggedInStatus(true)
        verify(mockPreferenceDataSource).setUserLoggedInStatus(false)
    }
}
