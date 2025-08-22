package com.srizan.technonextcodingassessment.data.repository

import com.srizan.technonextcodingassessment.cache.dao.UserDao
import com.srizan.technonextcodingassessment.cache.entity.UserEntity
import com.srizan.technonextcodingassessment.domain.repository.AuthenticationRepository
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : AuthenticationRepository {
    override suspend fun signUp(email: String, password: String): Result<Unit> {
        if (!isValidEmail(email)) return Result.failure(Exception("Invalid email"))
        //if (!isStrongPassword(password)) return Result.failure(Exception("Weak password"))
        if (userDao.doesUserExist(email)) return Result.failure(Exception("Email already registered"))

        val user = UserEntity(email = email, password = password)
        return try {
            userDao.insertUser(user)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Registration failed: ${e.message}"))
        }
    }

    override suspend fun signIn(email: String, password: String): Result<Unit> {
        val user =
            userDao.getUserByEmail(email) ?: return Result.failure(Exception("Invalid credentials"))
        return if (password == user.password) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Invalid credentials"))
        }
    }

    suspend fun deleteAccount(email: String): Result<Unit> {
        return try {
            val rowsAffected = userDao.deleteUserByEmail(email)
            if (rowsAffected > 0) Result.success(Unit)
            else Result.failure(Exception("User not found"))
        } catch (e: Exception) {
            Result.failure(Exception("Deletion failed: ${e.message}"))
        }
    }

    private fun isValidEmail(email: String): Boolean =
        android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun isStrongPassword(password: String): Boolean =
        password.length >= 8 && password.any { it.isDigit() } && password.any { !it.isLetterOrDigit() }
}