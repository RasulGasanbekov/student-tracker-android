package com.edutrack.data.api

import com.edutrack.data.api.models.*
import com.edutrack.data.api.models.response.LoginResponse
import com.edutrack.data.api.models.response.UserResponse
import com.edutrack.data.api.models.response.progress.ProgressResponse
import retrofit2.http.*

interface ApiService {
    @POST("/api/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("/api/auth/google")
    suspend fun loginWithGoogle(@Body request: GoogleLoginRequest): LoginResponse

    @GET("/api/user")
    suspend fun getUser(@Header("Authorization") token: String): UserResponse

    @GET("/api/progress")
    suspend fun getProgress(@Header("Authorization") token: String): ProgressResponse
}