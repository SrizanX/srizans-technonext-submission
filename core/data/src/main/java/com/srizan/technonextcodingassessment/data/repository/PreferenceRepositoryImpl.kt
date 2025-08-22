package com.srizan.technonextcodingassessment.data.repository

import com.srizan.technonextcodingassessment.domain.datasource.PreferenceDataSource
import com.srizan.technonextcodingassessment.domain.repository.PreferenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferenceRepositoryImpl @Inject constructor(
    private val preferenceDataSource: PreferenceDataSource
) : PreferenceRepository {
    override fun isUserLoggedIn() = preferenceDataSource.isUserLoggedIn()


    override suspend fun setUserLoggedInStatus(loggedIn: Boolean) {
        preferenceDataSource.setUserLoggedInStatus(loggedIn)
    }

    override fun getUserEmail(): Flow<String> {
        return preferenceDataSource.getUserEmail().map { it ?: "" }
    }

    override suspend fun setUserEmail(email: String) {
        preferenceDataSource.setUserEmail(email)
    }

    override suspend fun clearPreferences() {
        preferenceDataSource.clearPreferences()
    }
}