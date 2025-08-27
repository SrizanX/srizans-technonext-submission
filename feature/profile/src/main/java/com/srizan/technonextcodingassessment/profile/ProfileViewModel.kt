package com.srizan.technonextcodingassessment.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.srizan.technonextcodingassessment.domain.repository.PreferenceRepository
import com.srizan.technonextcodingassessment.model.AppThemeConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())

    val uiState = combine(
        preferenceRepository.getUserEmail(),
        preferenceRepository.getAppThemeConfig(),
        _uiState
    ) { email, themeConfig, currentState ->
        currentState.copy(
            email = email,
            appThemeConfig = themeConfig
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState()
    )



    fun changeAppThemeConfig(themeConfig: AppThemeConfig) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                preferenceRepository.setAppThemeConfig(themeConfig)
                _uiState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to update theme: ${e.message}"
                    )
                }
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isSigningOut = true, errorMessage = null) }
                preferenceRepository.setUserLoggedInStatus(false)
                preferenceRepository.clearPreferences()
                // Note: Navigation should be handled at the app level
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSigningOut = false,
                        errorMessage = "Failed to sign out: ${e.message}"
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }


    data class UiState(
        val email: String = "",
        val appThemeConfig: AppThemeConfig = AppThemeConfig.SYSTEM,
        val isSigningOut: Boolean = false,
        val isLoading: Boolean = false,
        val errorMessage: String? = null
    )
}