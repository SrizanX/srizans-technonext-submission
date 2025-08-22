package com.srizan.technonextcodingassessment.data.repository

import com.srizan.technonextcodingassessment.domain.AuthenticationRepository
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor() : AuthenticationRepository {
    override suspend fun login(email: String, password: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun register(email: String, password: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun logout(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun isAuthenticated(): Boolean {
        TODO("Not yet implemented")
    }
}