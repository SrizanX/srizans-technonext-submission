package com.srizan.technonextcodingassessment.data.repository

import com.srizan.technonextcodingassessment.cache.dao.UserDao
import com.srizan.technonextcodingassessment.cache.entity.UserEntity
import com.srizan.technonextcodingassessment.domain.repository.AuthenticationRepository
import com.srizan.technonextcodingassessment.domain.validation.ValidationRules
import kotlinx.coroutines.delay
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : AuthenticationRepository {
    override suspend fun signUp(email: String, password: String): Result<Unit> {
        // Server-side validation - Critical security checks that cannot be bypassed
        if (!ValidationRules.isValidEmail(email)) {
            return Result.failure(Exception("Invalid email format"))
        }
        
        if (!ValidationRules.isStrongPassword(password)) {
            return Result.failure(Exception("Password must be at least 8 characters with uppercase, lowercase, number and special character"))
        }
        
        // Business rule validation
        if (userDao.doesUserExist(email)) {
            return Result.failure(Exception("Email already registered"))
        }

        val user = UserEntity(email = email, password = password)
        return try {
            userDao.insertUser(user)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Registration failed: ${e.message}"))
        }
    }

    override suspend fun signIn(email: String, password: String): Result<Unit> {
        delay(3000) // Simulate network delay
        
        // Basic validation
        if (!ValidationRules.isValidEmail(email)) {
            return Result.failure(Exception("Invalid email format"))
        }
        
        val user = userDao.getUserByEmail(email) 
            ?: return Result.failure(Exception("Invalid credentials"))
            
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
}