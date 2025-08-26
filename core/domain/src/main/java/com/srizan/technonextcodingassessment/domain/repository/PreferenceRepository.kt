package com.srizan.technonextcodingassessment.domain.repository

import com.srizan.technonextcodingassessment.model.AppThemeConfig
import kotlinx.coroutines.flow.Flow

interface PreferenceRepository {
    fun isUserLoggedIn(): Flow<Boolean?>
    suspend fun setUserLoggedInStatus(loggedIn: Boolean)
    fun getUserEmail(): Flow<String>
    suspend fun setUserEmail(email: String)
    fun getAppThemeConfig(): Flow<AppThemeConfig>
    suspend fun setAppThemeConfig(themeConfig: AppThemeConfig)
    suspend fun clearPreferences()
}