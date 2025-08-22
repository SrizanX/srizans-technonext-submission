package com.srizan.technonextcodingassessment.domain.datasource

import kotlinx.coroutines.flow.Flow

interface PreferenceDataSource {
    fun isUserLoggedIn(): Flow<Boolean?>
    suspend fun setUserLoggedInStatus(loggedIn: Boolean)
    fun getUserEmail(): Flow<String?>
    suspend fun setUserEmail(email: String)
    suspend fun clearPreferences()
}