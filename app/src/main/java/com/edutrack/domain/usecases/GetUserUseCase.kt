package com.edutrack.domain.usecases

import com.edutrack.domain.models.User
import com.edutrack.domain.repository.AuthRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(token: String): Result<User> {
        if (token.isBlank()) {
            return Result.failure(IllegalArgumentException("Token is required"))
        }
        return repository.getUser(token)
    }
}