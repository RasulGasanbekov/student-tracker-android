package com.edutrack.domain.models

data class AuthData(
    val token: String,
    val user: User
)