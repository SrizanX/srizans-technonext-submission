package com.srizan.technonextcodingassessment.domain.repository

interface AuthenticationRepository {
    suspend fun signIn(email: String, password: String): Result<Unit>
    suspend fun signUp(email: String, password: String): Result<Unit>
}