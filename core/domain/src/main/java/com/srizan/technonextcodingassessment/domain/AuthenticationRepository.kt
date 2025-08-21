package com.srizan.technonextcodingassessment.domain

interface AuthenticationRepository {
    suspend fun login(email: String, password: String): Boolean

    suspend fun register(email: String, password: String): Boolean

    suspend fun logout(): Boolean

    suspend fun isAuthenticated(): Boolean
}