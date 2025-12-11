package com.edutrack.domain.usecases

import com.edutrack.domain.models.AuthData
import com.edutrack.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<AuthData> {
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("Email and password are required"))
        }
        return repository.login(email, password)
    }
}