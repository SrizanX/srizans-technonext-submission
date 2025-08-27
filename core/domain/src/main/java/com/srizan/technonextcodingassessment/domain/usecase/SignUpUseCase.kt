package com.srizan.technonextcodingassessment.domain.usecase

import com.srizan.technonextcodingassessment.domain.repository.AuthenticationRepository
import com.srizan.technonextcodingassessment.domain.repository.PreferenceRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authRepository: AuthenticationRepository,
    private val preferenceRepository: PreferenceRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        return try {
            // First complete the signup process
            val signUpResult = authRepository.signUp(email, password)
            
            if (signUpResult.isSuccess) {
                // Only set preferences after signup succeeds
                // These operations complete synchronously at suspension points
                preferenceRepository.setUserLoggedInStatus(true)
                preferenceRepository.setUserEmail(email)
                Result.success(Unit)
            } else {
                signUpResult
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}