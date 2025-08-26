package com.srizan.technonextcodingassessment.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.srizan.technonextcodingassessment.domain.repository.AuthenticationRepository
import com.srizan.technonextcodingassessment.domain.repository.PreferenceRepository
import com.srizan.technonextcodingassessment.domain.validation.ValidationRules
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val preferenceRepository: PreferenceRepository,
) : ViewModel() {

    // UI state
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    // UI events for one-time actions (e.g., navigation)
    private val _uiEvent = Channel<SignInUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    fun signIn() {
        viewModelScope.launch {
            // Update state to show loading
            _uiState.value = _uiState.value.copy(
                isLoading = true, errorMessage = null
            )

            // Validate input using centralized validation rules
            if (!ValidationRules.isValidEmail(uiState.value.email)) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false, errorMessage = "Invalid email format"
                )
                return@launch
            }
            if (uiState.value.password.isEmpty()) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false, errorMessage = "Password cannot be empty"
                )
                return@launch
            }

            // Perform sign-in
            authenticationRepository.signIn(uiState.value.email, uiState.value.password)
                .fold(onSuccess = {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    preferenceRepository.setUserLoggedInStatus(true)
                    preferenceRepository.setUserEmail(uiState.value.email)
                    _uiEvent.send(SignInUiEvent.SignInSuccess)
                }, onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false, errorMessage = exception.message ?: "Login failed"
                    )
                    _uiEvent.send(SignInUiEvent.SignInError(exception.message ?: "Login failed"))
                })
        }
    }


    // Update email with real-time validation
    fun updateEmail(email: String) {
        val emailError = if (email.isNotEmpty() && !ValidationRules.isValidEmail(email)) {
            "Invalid email format"
        } else null

        _uiState.value = _uiState.value.copy(
            email = email,
            emailError = emailError,
            errorMessage = null,
            isFormValid = validateForm(email, _uiState.value.password)
        )
    }

    // Update password with minimal validation (only check if empty when submitting)
    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(
            password = password,
            passwordError = null, // No real-time password validation for sign-in
            errorMessage = null,
            isFormValid = validateForm(_uiState.value.email, password)
        )
    }

    // Form validation - only validate email format and non-empty fields
    private fun validateForm(email: String, password: String): Boolean {
        return ValidationRules.isValidEmail(email) && email.isNotEmpty() && password.isNotEmpty() // Only check not empty, no complexity requirements
    }

    // UI state data class
    data class UiState(
        val email: String = "",
        val password: String = "",
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
        val emailError: String? = null,
        val passwordError: String? = null,
        val isFormValid: Boolean = false
    )

    // UI event sealed class
    sealed class SignInUiEvent {
        data object SignInSuccess : SignInUiEvent()
        data class SignInError(val message: String) : SignInUiEvent()
    }
}