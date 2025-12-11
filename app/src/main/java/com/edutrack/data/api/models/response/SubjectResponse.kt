package com.edutrack.data.api.models.response

data class SubjectResponse(
    val name: String,
    val currentScore: Int,
    val maxScore: Int,
    val components: List<ComponentResponse>
)