package com.edutrack.domain.repository

import com.edutrack.domain.models.AuthData
import com.edutrack.domain.models.Subject
import com.edutrack.domain.models.User

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<AuthData>
    suspend fun loginWithGoogle(googleToken: String): Result<AuthData>
    suspend fun getUser(token: String): Result<User>
    suspend fun getProgress(token: String): Result<List<Subject>>
    suspend fun logout()
}