package com.srizan.technonextcodingassessment.signup

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.srizan.technonextcodingassessment.domain.repository.AuthenticationRepository
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
    private val authenticationRepository: AuthenticationRepository
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
                email = email,
                password = password,
                confirmPassword = confirmPassword,
                isLoading = true,
                errorMessage = null
            )

            // Validate input
            if (!isValidEmail(email)) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false, errorMessage = "Invalid email format"
                )
                return@launch
            }
            if (password.isEmpty()) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false, errorMessage = "Password cannot be empty"
                )
                return@launch
            }
            if (password != confirmPassword) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false, errorMessage = "Passwords do not match"
                )
                return@launch
            }
//            if (!isStrongPassword(password)) {
//                _uiState.value = _uiState.value.copy(
//                    isLoading = false,
//                    errorMessage = "Password must be at least 8 characters, include a number and a special character"
//                )
//                return@launch
//            }

            // Perform registration
            authenticationRepository.signUp(email, password).fold(onSuccess = {
                _uiState.value = _uiState.value.copy(isLoading = false)
                _uiEvent.send(UiEvent.RegisterSuccess)
            }, onFailure = { exception ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false, errorMessage = exception.message ?: "Registration failed"
                )
            })
        }
    }

    // Update input fields
    fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(email = email, errorMessage = null)
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password, errorMessage = null)
    }

    fun updateConfirmPassword(confirmPassword: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = confirmPassword, errorMessage = null)
    }

    // Validation helpers
    private fun isValidEmail(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun isStrongPassword(password: String): Boolean =
        password.length >= 8 && password.any { it.isDigit() } && password.any { !it.isLetterOrDigit() }

    // UI state data class
    data class UiState(
        val email: String = "",
        val password: String = "",
        val confirmPassword: String = "",
        val isLoading: Boolean = false,
        val errorMessage: String? = null
    )

    // UI event sealed class
    sealed class UiEvent {
        data object RegisterSuccess : UiEvent()
        data class RegisterError(val message: String) : UiEvent()
    }
}