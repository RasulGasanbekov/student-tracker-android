package com.edutrack.domain.usecases

import com.edutrack.domain.models.AuthData
import com.edutrack.domain.repository.AuthRepository
import javax.inject.Inject

class GoogleLoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(googleToken: String): Result<AuthData> {
        if (googleToken.isBlank()) {
            return Result.failure(IllegalArgumentException("Google token is required"))
        }
        return repository.loginWithGoogle(googleToken)
    }
}