package com.srizan.technonextcodingassessment.data.repository

import com.srizan.technonextcodingassessment.domain.datasource.PreferenceDataSource
import com.srizan.technonextcodingassessment.domain.repository.PreferenceRepository
import com.srizan.technonextcodingassessment.model.AppThemeConfig
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferenceRepositoryImpl @Inject constructor(
    private val preferenceDataSource: PreferenceDataSource
) : PreferenceRepository {
    override fun isUserLoggedIn() = preferenceDataSource.isUserLoggedIn()


    override suspend fun setUserLoggedInStatus(loggedIn: Boolean) {
        preferenceDataSource.setUserLoggedInStatus(loggedIn)
    }

    override fun getUserEmail() = preferenceDataSource.getUserEmail().map { it ?: "" }

    override suspend fun setUserEmail(email: String) {
        preferenceDataSource.setUserEmail(email)
    }

    override fun getAppThemeConfig() = preferenceDataSource.getAppThemeConfig()

    override suspend fun setAppThemeConfig(themeConfig: AppThemeConfig) {
        preferenceDataSource.setAppThemeConfig(themeConfig)
    }

    override suspend fun clearPreferences() {
        preferenceDataSource.clearPreferences()
    }
}