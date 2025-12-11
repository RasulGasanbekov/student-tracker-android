package com.edutrack.domain.models

data class SubjectComponent(
    val name: String,
    val score: Int,
    val maxScore: Int,
    val completed: Boolean
)