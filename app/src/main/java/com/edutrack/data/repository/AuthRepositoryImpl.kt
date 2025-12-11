package com.edutrack.data.repository

import com.edutrack.data.api.ApiService
import com.edutrack.data.api.models.*
import com.edutrack.data.api.models.response.LoginResponse
import com.edutrack.data.storage.TokenStorage
import com.edutrack.domain.models.*
import com.edutrack.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val tokenStorage: TokenStorage
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<AuthData> {
        return try {
            val response = apiService.login(LoginRequest(email, password))
            handleAuthResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loginWithGoogle(googleToken: String): Result<AuthData> {
        return try {
            val response = apiService.loginWithGoogle(GoogleLoginRequest(googleToken))
            handleAuthResponse(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUser(token: String): Result<User> {
        return try {
            val response = apiService.getUser("Bearer $token")
            val user = User(
                id = response.id,
                name = response.name,
                email = response.email
            )
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProgress(token: String): Result<List<Subject>> {
        return try {
            val response = apiService.getProgress("Bearer $token")
            val subjects = response.subjects.map { subject ->
                Subject(
                    name = subject.name,
                    currentScore = subject.currentScore,
                    maxScore = subject.maxScore,
                    components = subject.components.map { component ->
                        SubjectComponent(
                            name = component.name,
                            score = component.score,
                            maxScore = component.maxScore,
                            completed = component.completed
                        )
                    }
                )
            }
            Result.success(subjects)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        tokenStorage.clearToken()
    }

    private suspend fun handleAuthResponse(response: LoginResponse): Result<AuthData> {
        return if (response.success && response.token != null && response.user != null) {
            tokenStorage.saveToken(response.token)
            val authData = AuthData(
                token = response.token,
                user = User(
                    id = response.user.id,
                    name = response.user.name,
                    email = response.user.email
                )
            )
            Result.success(authData)
        } else {
            Result.failure(Exception(response.error ?: "Authentication failed"))
        }
    }
}