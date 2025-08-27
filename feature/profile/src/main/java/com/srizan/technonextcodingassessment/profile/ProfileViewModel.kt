package com.srizan.technonextcodingassessment.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.srizan.technonextcodingassessment.domain.repository.PreferenceRepository
import com.srizan.technonextcodingassessment.model.AppThemeConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    val uiState = MutableStateFlow<UiState>(
        UiState(
            appThemeConfig = AppThemeConfig.SYSTEM
        )
    )

    val appThemeConfig = preferenceRepository.getAppThemeConfig().stateIn(
        viewModelScope, started = SharingStarted.Eagerly, initialValue = AppThemeConfig.SYSTEM
    )


    fun changeAppThemeConfig(themeConfig: AppThemeConfig) {
        viewModelScope.launch {
            preferenceRepository.setAppThemeConfig(themeConfig)
        }
    }

    fun signOut() {
        viewModelScope.launch {
            preferenceRepository.setUserLoggedInStatus(false)
            preferenceRepository.clearPreferences()
        }
    }


    data class UiState(
        val email: String = "",
        val appThemeConfig: AppThemeConfig,
        val isSigningOut: Boolean = false
    )
}