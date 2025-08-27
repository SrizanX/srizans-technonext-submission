package com.srizan.technonextcodingassessment.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.srizan.technonextcodingassessment.domain.usecase.SignUpUseCase
import com.srizan.technonextcodingassessment.domain.validation.PasswordStrength
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
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {

    // UI state
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    // UI events for one-time actions (e.g., navigation)
    private val _uiEvent = Channel<UiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    fun register(email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            // Update state to show loading
            _uiState.value = _uiState.value.copy(
                isLoading = true, errorMessage = null
            )

            // Validate input (UI layer validation for user feedback)
            if (!ValidationRules.isValidEmail(email)) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false, errorMessage = "Invalid email format"
                )
                return@launch
            }
            if (!ValidationRules.isStrongPassword(password)) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Password must be at least 8 characters with uppercase, lowercase, number and special character"
                )
                return@launch
            }
            if (!ValidationRules.doPasswordsMatch(password, confirmPassword)) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false, errorMessage = "Passwords do not match"
                )
                return@launch
            }

            // Perform registration
            signUpUseCase(email, password).fold(onSuccess = {
                _uiState.value = _uiState.value.copy(isLoading = false)
                _uiEvent.send(UiEvent.RegisterSuccess)
            }, onFailure = { exception ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false, errorMessage = exception.message ?: "Registration failed"
                )
            })
        }
    }

    // Update input fields with real-time validation
    fun updateEmail(email: String) {
        val emailError = if (email.isNotEmpty() && !ValidationRules.isValidEmail(email)) {
            "Invalid email format"
        } else null

        _uiState.value = _uiState.value.copy(
            email = email, emailError = emailError, errorMessage = null, isFormValid = validateForm(
                email, _uiState.value.password, _uiState.value.confirmPassword
            )
        )
    }

    fun updatePassword(password: String) {
        val passwordError =
            if (password.isNotEmpty() && !ValidationRules.isStrongPassword(password)) {
                "Password must be at least 8 characters with uppercase, lowercase, number and special character"
            } else null

        val passwordStrength = ValidationRules.calculatePasswordStrength(password)

        _uiState.value = _uiState.value.copy(
            password = password,
            passwordError = passwordError,
            passwordStrength = passwordStrength,
            errorMessage = null,
            isFormValid = validateForm(
                _uiState.value.email, password, _uiState.value.confirmPassword
            )
        )
    }

    fun updateConfirmPassword(confirmPassword: String) {
        val confirmPasswordError =
            if (confirmPassword.isNotEmpty() && !ValidationRules.doPasswordsMatch(
                    _uiState.value.password, confirmPassword
                )
            ) {
                "Passwords do not match"
            } else null

        _uiState.value = _uiState.value.copy(
            confirmPassword = confirmPassword,
            confirmPasswordError = confirmPasswordError,
            errorMessage = null,
            isFormValid = validateForm(
                _uiState.value.email, _uiState.value.password, confirmPassword
            )
        )
    }

    private fun validateForm(email: String, password: String, confirmPassword: String): Boolean {
        return ValidationRules.isValidEmail(email) && ValidationRules.isStrongPassword(password) && ValidationRules.doPasswordsMatch(
            password,
            confirmPassword
        ) && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()
    }

    // UI state data class
    data class UiState(
        val email: String = "",
        val password: String = "",
        val confirmPassword: String = "",
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
        val emailError: String? = null,
        val passwordError: String? = null,
        val confirmPasswordError: String? = null,
        val isFormValid: Boolean = false,
        val passwordStrength: PasswordStrength = PasswordStrength.WEAK
    )

    // UI event sealed class
    sealed class UiEvent {
        data object RegisterSuccess : UiEvent()
        data class RegisterError(val message: String) : UiEvent()
    }
}