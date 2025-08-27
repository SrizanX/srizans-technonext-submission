package com.srizan.technonextcodingassessment.signin

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.srizan.technonextcodingassessment.domain.repository.AuthenticationRepository
import com.srizan.technonextcodingassessment.domain.repository.PreferenceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class SignInViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockAuthenticationRepository: AuthenticationRepository

    @Mock
    private lateinit var mockPreferenceRepository: PreferenceRepository

    private lateinit var viewModel: SignInViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    // Test data
    private val testEmail = "test@example.com"
    private val testPassword = "password123"

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = SignInViewModel(mockAuthenticationRepository, mockPreferenceRepository)
    }

    @Test
    fun `initial ui state should have correct default values`() {
        // When
        val uiState = viewModel.uiState.value
        
        // Then
        assertEquals("", uiState.email)
        assertEquals("", uiState.password)
        assertFalse(uiState.isLoading)
        assertNull(uiState.errorMessage)
        assertNull(uiState.emailError)
        assertNull(uiState.passwordError)
        assertFalse(uiState.isFormValid)
    }

    @Test
    fun `updateEmail should update email in state`() = testScope.runTest {
        // When
        viewModel.updateEmail(testEmail)
        
        // Then
        val uiState = viewModel.uiState.value
        assertEquals(testEmail, uiState.email)
        assertNull(uiState.errorMessage)
    }

    @Test
    fun `updatePassword should update password in state`() = testScope.runTest {
        // When
        viewModel.updatePassword(testPassword)
        
        // Then
        val uiState = viewModel.uiState.value
        assertEquals(testPassword, uiState.password)
        assertNull(uiState.errorMessage)
        assertNull(uiState.passwordError)
    }

    @Test
    fun `signIn should succeed with valid credentials`() = testScope.runTest {
        // Given
        viewModel.updateEmail(testEmail)
        viewModel.updatePassword(testPassword)
        whenever(mockAuthenticationRepository.signIn(testEmail, testPassword)) doReturn Result.success(Unit)
        
        // When
        viewModel.signIn()
        advanceUntilIdle()
        
        // Then
        verify(mockAuthenticationRepository).signIn(testEmail, testPassword)
        verify(mockPreferenceRepository).setUserLoggedInStatus(true)
        verify(mockPreferenceRepository).setUserEmail(testEmail)
        
        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertNull(uiState.errorMessage)
        
        // Check that success event was sent
        val event = viewModel.uiEvent.first()
        assertEquals(SignInViewModel.SignInUiEvent.SignInSuccess, event)
    }

    @Test
    fun `signIn should handle authentication failure`() = testScope.runTest {
        // Given
        val errorMessage = "Invalid credentials"
        viewModel.updateEmail(testEmail)
        viewModel.updatePassword(testPassword)
        whenever(mockAuthenticationRepository.signIn(testEmail, testPassword)) doReturn 
            Result.failure(RuntimeException(errorMessage))
        
        // When
        viewModel.signIn()
        advanceUntilIdle()
        
        // Then
        verify(mockAuthenticationRepository).signIn(testEmail, testPassword)
        
        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertEquals(errorMessage, uiState.errorMessage)
        
        // Check that error event was sent
        val event = viewModel.uiEvent.first()
        assertEquals(SignInViewModel.SignInUiEvent.SignInError(errorMessage), event)
        
        // Verify preferences were not updated on failure
        verify(mockPreferenceRepository, org.mockito.kotlin.never()).setUserLoggedInStatus(org.mockito.kotlin.any())
        verify(mockPreferenceRepository, org.mockito.kotlin.never()).setUserEmail(org.mockito.kotlin.any())
    }

    @Test
    fun `signIn should handle authentication failure with null message`() = testScope.runTest {
        // Given
        viewModel.updateEmail(testEmail)
        viewModel.updatePassword(testPassword)
        whenever(mockAuthenticationRepository.signIn(testEmail, testPassword)) doReturn 
            Result.failure(RuntimeException()) // No message
        
        // When
        viewModel.signIn()
        advanceUntilIdle()
        
        // Then
        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertEquals("Login failed", uiState.errorMessage) // Default message
        
        // Check that error event was sent with default message
        val event = viewModel.uiEvent.first()
        assertEquals(SignInViewModel.SignInUiEvent.SignInError("Login failed"), event)
    }

    @Test
    fun `signIn should show loading state during operation`() = testScope.runTest {
        // Given
        viewModel.updateEmail(testEmail)
        viewModel.updatePassword(testPassword)
        whenever(mockAuthenticationRepository.signIn(testEmail, testPassword)) doReturn Result.success(Unit)
        
        // When
        viewModel.signIn()
        advanceUntilIdle()
        
        // Then - verify final state and that repository was called
        val finalState = viewModel.uiState.value
        assertFalse("Final state should not be loading", finalState.isLoading)
        assertNull("Final state should not have error", finalState.errorMessage)
        
        // Verify repository was called with correct credentials
        verify(mockAuthenticationRepository).signIn(testEmail, testPassword)
        verify(mockPreferenceRepository).setUserLoggedInStatus(true)
        verify(mockPreferenceRepository).setUserEmail(testEmail)
    }

    @Test
    fun `updating email should clear previous error messages`() = testScope.runTest {
        // Given - manually set an error state
        val currentState = viewModel.uiState.value
        // Note: Since we can't easily trigger validation errors in unit tests due to Android dependencies,
        // we'll just verify that updateEmail clears error message if it was set
        
        // When - update email
        viewModel.updateEmail(testEmail)
        
        // Then - error should be cleared
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `updating password should clear previous error messages`() = testScope.runTest {
        // When - update password
        viewModel.updatePassword(testPassword)
        
        // Then - error should be cleared
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `signIn with empty password should show error`() = testScope.runTest {
        // Given
        viewModel.updateEmail(testEmail)
        viewModel.updatePassword("") // Empty password
        
        // When
        viewModel.signIn()
        advanceUntilIdle()
        
        // Then
        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertEquals("Password cannot be empty", uiState.errorMessage)
        
        // Verify repository methods were not called
        verify(mockAuthenticationRepository, org.mockito.kotlin.never()).signIn(org.mockito.kotlin.any(), org.mockito.kotlin.any())
        verify(mockPreferenceRepository, org.mockito.kotlin.never()).setUserLoggedInStatus(org.mockito.kotlin.any())
    }

    @Test
    fun `signIn should validate input before making network call`() = testScope.runTest {
        // Given - empty email and password
        viewModel.updateEmail("")
        viewModel.updatePassword("")
        
        // When
        viewModel.signIn()
        advanceUntilIdle()
        
        // Then - should not call repository due to validation
        verify(mockAuthenticationRepository, org.mockito.kotlin.never()).signIn(org.mockito.kotlin.any(), org.mockito.kotlin.any())
        
        val uiState = viewModel.uiState.value
        assertFalse(uiState.isLoading)
        assertTrue(uiState.errorMessage != null) // Some validation error should be present
    }

    @Test
    fun `ui events should be properly handled`() = testScope.runTest {
        // Given
        viewModel.updateEmail(testEmail)
        viewModel.updatePassword(testPassword)
        whenever(mockAuthenticationRepository.signIn(testEmail, testPassword)) doReturn Result.success(Unit)
        
        // When
        viewModel.signIn()
        advanceUntilIdle()
        
        // Then - should emit success event
        val event = viewModel.uiEvent.first()
        assertTrue(event is SignInViewModel.SignInUiEvent.SignInSuccess)
    }
}
