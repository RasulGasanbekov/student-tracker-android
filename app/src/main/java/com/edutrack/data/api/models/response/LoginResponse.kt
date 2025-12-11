package com.edutrack.data.api.models.response

data class LoginResponse(
    val success: Boolean,
    val token: String? = null,
    val user: UserResponse? = null,
    val error: String? = null
)
