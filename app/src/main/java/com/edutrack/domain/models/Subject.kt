package com.edutrack.domain.models

data class Subject(
    val name: String,
    val currentScore: Int,
    val maxScore: Int,
    val components: List<SubjectComponent>
)