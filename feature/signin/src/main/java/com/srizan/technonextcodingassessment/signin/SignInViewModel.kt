package com.srizan.technonextcodingassessment.signin

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.srizan.technonextcodingassessment.domain.repository.AuthenticationRepository
import com.srizan.technonextcodingassessment.domain.repository.PreferenceRepository
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

            // Validate input
            if (!isValidEmail(uiState.value.email)) {
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


    // Update email or password in state
    fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(email = email, errorMessage = null)
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password, errorMessage = null)
    }

    // Validation helpers
    private fun isValidEmail(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()

    // UI state data class
    data class UiState(
        val email: String = "",
        val password: String = "",
        val isLoading: Boolean = false,
        val errorMessage: String? = null
    )

    // UI event sealed class
    sealed class SignInUiEvent {
        data object SignInSuccess : SignInUiEvent()
        data class SignInError(val message: String) : SignInUiEvent()
    }
}