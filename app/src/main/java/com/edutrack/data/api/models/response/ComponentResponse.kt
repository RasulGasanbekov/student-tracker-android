package com.edutrack.data.api.models.response

data class ComponentResponse(
    val name: String,
    val score: Int,
    val maxScore: Int,
    val completed: Boolean
)