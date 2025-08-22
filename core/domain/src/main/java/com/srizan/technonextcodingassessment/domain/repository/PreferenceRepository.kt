package com.srizan.technonextcodingassessment.domain.repository

import kotlinx.coroutines.flow.Flow

interface PreferenceRepository {
    fun isUserLoggedIn(): Flow<Boolean?>
    suspend fun setUserLoggedInStatus(loggedIn: Boolean)
    fun getUserEmail(): Flow<String>
    suspend fun setUserEmail(email: String)
    suspend fun clearPreferences()
}