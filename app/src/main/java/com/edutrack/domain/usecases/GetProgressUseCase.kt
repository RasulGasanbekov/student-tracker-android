package com.edutrack.domain.usecases

import com.edutrack.domain.models.Subject
import com.edutrack.domain.repository.AuthRepository
import javax.inject.Inject

class GetProgressUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(token: String): Result<List<Subject>> {
        if (token.isBlank()) {
            return Result.failure(IllegalArgumentException("Token is required"))
        }
        return repository.getProgress(token)
    }
}