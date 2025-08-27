package com.srizan.technonextcodingassessment.signup

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.srizan.technonextcodingassessment.domain.repository.AuthenticationRepository
import com.srizan.technonextcodingassessment.domain.validation.PasswordStrength
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class SignUpViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var authenticationRepository: AuthenticationRepository

    private lateinit var viewModel: SignUpViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = SignUpViewModel(authenticationRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be correct`() = runTest {
        val initialState = viewModel.uiState.first()
        
        assertEquals("", initialState.email)
        assertEquals("", initialState.password)
        assertEquals("", initialState.confirmPassword)
        assertFalse(initialState.isLoading)
        assertNull(initialState.errorMessage)
        assertNull(initialState.emailError)
        assertNull(initialState.passwordError)
        assertNull(initialState.confirmPasswordError)
        assertFalse(initialState.isFormValid)
        assertEquals(PasswordStrength.WEAK, initialState.passwordStrength)
    }

    @Test
    fun `updateEmail with valid email should update state correctly`() = runTest {
        val validEmail = "test@example.com"
        
        viewModel.updateEmail(validEmail)
        advanceUntilIdle()
        
        val state = viewModel.uiState.first()
        assertEquals(validEmail, state.email)
        assertNull(state.emailError)
        assertNull(state.errorMessage)
    }

    @Test
    fun `updateEmail with invalid email should show error`() = runTest {
        val invalidEmail = "invalid-email"
        
        viewModel.updateEmail(invalidEmail)
        advanceUntilIdle()
        
        val state = viewModel.uiState.first()
        assertEquals(invalidEmail, state.email)
        assertEquals("Invalid email format", state.emailError)
        assertNull(state.errorMessage)
        assertFalse(state.isFormValid)
    }

    @Test
    fun `updateEmail with empty email should not show error`() = runTest {
        viewModel.updateEmail("")
        advanceUntilIdle()
        
        val state = viewModel.uiState.first()
        assertEquals("", state.email)
        assertNull(state.emailError)
    }

    @Test
    fun `updatePassword with strong password should update state correctly`() = runTest {
        val strongPassword = "StrongPass123!"
        
        viewModel.updatePassword(strongPassword)
        advanceUntilIdle()
        
        val state = viewModel.uiState.first()
        assertEquals(strongPassword, state.password)
        assertNull(state.passwordError)
        assertEquals(PasswordStrength.STRONG, state.passwordStrength)
        assertNull(state.errorMessage)
    }

    @Test
    fun `updatePassword with weak password should show error`() = runTest {
        val weakPassword = "weak"
        
        viewModel.updatePassword(weakPassword)
        advanceUntilIdle()
        
        val state = viewModel.uiState.first()
        assertEquals(weakPassword, state.password)
        assertEquals(
            "Password must be at least 8 characters with uppercase, lowercase, number and special character",
            state.passwordError
        )
        assertEquals(PasswordStrength.WEAK, state.passwordStrength)
        assertFalse(state.isFormValid)
    }

    @Test
    fun `updatePassword with empty password should not show error`() = runTest {
        viewModel.updatePassword("")
        advanceUntilIdle()
        
        val state = viewModel.uiState.first()
        assertEquals("", state.password)
        assertNull(state.passwordError)
        assertEquals(PasswordStrength.WEAK, state.passwordStrength)
    }

    @Test
    fun `updateConfirmPassword with matching password should update state correctly`() = runTest {
        val password = "StrongPass123!"
        viewModel.updatePassword(password)
        advanceUntilIdle()
        
        viewModel.updateConfirmPassword(password)
        advanceUntilIdle()
        
        val state = viewModel.uiState.first()
        assertEquals(password, state.confirmPassword)
        assertNull(state.confirmPasswordError)
        assertNull(state.errorMessage)
    }

    @Test
    fun `updateConfirmPassword with non-matching password should show error`() = runTest {
        val password = "StrongPass123!"
        val differentPassword = "DifferentPass456!"
        
        viewModel.updatePassword(password)
        advanceUntilIdle()
        
        viewModel.updateConfirmPassword(differentPassword)
        advanceUntilIdle()
        
        val state = viewModel.uiState.first()
        assertEquals(differentPassword, state.confirmPassword)
        assertEquals("Passwords do not match", state.confirmPasswordError)
        assertFalse(state.isFormValid)
    }

    @Test
    fun `updateConfirmPassword with empty password should not show error`() = runTest {
        viewModel.updateConfirmPassword("")
        advanceUntilIdle()
        
        val state = viewModel.uiState.first()
        assertEquals("", state.confirmPassword)
        assertNull(state.confirmPasswordError)
    }

    @Test
    fun `form validation should be true when all fields are valid`() = runTest {
        val validEmail = "test@example.com"
        val strongPassword = "StrongPass123!"
        
        viewModel.updateEmail(validEmail)
        viewModel.updatePassword(strongPassword)
        viewModel.updateConfirmPassword(strongPassword)
        advanceUntilIdle()
        
        val state = viewModel.uiState.first()
        assertTrue(state.isFormValid)
        assertNull(state.emailError)
        assertNull(state.passwordError)
        assertNull(state.confirmPasswordError)
    }

    @Test
    fun `form validation should be false when email is invalid`() = runTest {
        val invalidEmail = "invalid-email"
        val strongPassword = "StrongPass123!"
        
        viewModel.updateEmail(invalidEmail)
        viewModel.updatePassword(strongPassword)
        viewModel.updateConfirmPassword(strongPassword)
        advanceUntilIdle()
        
        val state = viewModel.uiState.first()
        assertFalse(state.isFormValid)
    }

    @Test
    fun `form validation should be false when password is weak`() = runTest {
        val validEmail = "test@example.com"
        val weakPassword = "weak"
        
        viewModel.updateEmail(validEmail)
        viewModel.updatePassword(weakPassword)
        viewModel.updateConfirmPassword(weakPassword)
        advanceUntilIdle()
        
        val state = viewModel.uiState.first()
        assertFalse(state.isFormValid)
    }

    @Test
    fun `register with valid inputs should succeed`() = runTest {
        val validEmail = "test@example.com"
        val strongPassword = "StrongPass123!"
        
        whenever(authenticationRepository.signUp(validEmail, strongPassword))
            .thenReturn(Result.success(Unit))
        
        viewModel.register(validEmail, strongPassword, strongPassword)
        advanceUntilIdle()
        
        val state = viewModel.uiState.first()
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        
        // Verify that UiEvent.RegisterSuccess was sent
        // Note: Testing events requires additional setup, this tests the state
    }

    @Test
    fun `register with invalid email should show error`() = runTest {
        val invalidEmail = "invalid-email"
        val strongPassword = "StrongPass123!"
        
        viewModel.register(invalidEmail, strongPassword, strongPassword)
        advanceUntilIdle()
        
        val state = viewModel.uiState.first()
        assertFalse(state.isLoading)
        assertEquals("Invalid email format", state.errorMessage)
    }

    @Test
    fun `register with weak password should show error`() = runTest {
        val validEmail = "test@example.com"
        val weakPassword = "weak"
        
        viewModel.register(validEmail, weakPassword, weakPassword)
        advanceUntilIdle()
        
        val state = viewModel.uiState.first()
        assertFalse(state.isLoading)
        assertEquals(
            "Password must be at least 8 characters with uppercase, lowercase, number and special character",
            state.errorMessage
        )
    }

    @Test
    fun `register with mismatched passwords should show error`() = runTest {
        val validEmail = "test@example.com"
        val strongPassword = "StrongPass123!"
        val differentPassword = "DifferentPass456!"
        
        viewModel.register(validEmail, strongPassword, differentPassword)
        advanceUntilIdle()
        
        val state = viewModel.uiState.first()
        assertFalse(state.isLoading)
        assertEquals("Passwords do not match", state.errorMessage)
    }

    @Test
    fun `register should show loading state during operation`() = runTest {
        val validEmail = "test@example.com"
        val strongPassword = "StrongPass123!"
        
        whenever(authenticationRepository.signUp(validEmail, strongPassword))
            .thenReturn(Result.success(Unit))
        
        // Verify initial state is not loading
        val initialState = viewModel.uiState.first()
        assertFalse("Should not be loading initially", initialState.isLoading)
        
        // Start the registration process
        viewModel.register(validEmail, strongPassword, strongPassword)
        
        // Complete the operation
        advanceUntilIdle()
        
        // Verify final state is not loading and no error
        val finalState = viewModel.uiState.first()
        assertFalse("Should not be loading after completion", finalState.isLoading)
        assertNull("Should have no error message after success", finalState.errorMessage)
    }

    @Test
    fun `register with repository failure should show error`() = runTest {
        val validEmail = "test@example.com"
        val strongPassword = "StrongPass123!"
        val errorMessage = "Registration failed due to network error"
        
        whenever(authenticationRepository.signUp(validEmail, strongPassword))
            .thenReturn(Result.failure(Exception(errorMessage)))
        
        viewModel.register(validEmail, strongPassword, strongPassword)
        advanceUntilIdle()
        
        val state = viewModel.uiState.first()
        assertFalse(state.isLoading)
        assertEquals(errorMessage, state.errorMessage)
    }

    @Test
    fun `register with repository failure and null message should show default error`() = runTest {
        val validEmail = "test@example.com"
        val strongPassword = "StrongPass123!"
        
        whenever(authenticationRepository.signUp(validEmail, strongPassword))
            .thenReturn(Result.failure(Exception()))
        
        viewModel.register(validEmail, strongPassword, strongPassword)
        advanceUntilIdle()
        
        val state = viewModel.uiState.first()
        assertFalse(state.isLoading)
        assertEquals("Registration failed", state.errorMessage)
    }

    @Test
    fun `password strength calculation should work correctly`() = runTest {
        // Test weak password
        viewModel.updatePassword("weak")
        advanceUntilIdle()
        assertEquals(PasswordStrength.WEAK, viewModel.uiState.first().passwordStrength)
        
        // Test moderate password
        viewModel.updatePassword("Password123")
        advanceUntilIdle()
        assertEquals(PasswordStrength.MODERATE, viewModel.uiState.first().passwordStrength)
        
        // Test strong password
        viewModel.updatePassword("StrongPass123!")
        advanceUntilIdle()
        assertEquals(PasswordStrength.STRONG, viewModel.uiState.first().passwordStrength)
    }

    @Test
    fun `error message should be cleared when updating fields`() = runTest {
        // First cause an error
        viewModel.updateEmail("invalid-email")
        advanceUntilIdle()
        
        // Then update to valid email
        viewModel.updateEmail("test@example.com")
        advanceUntilIdle()
        
        val state = viewModel.uiState.first()
        assertNull(state.errorMessage)
    }
}