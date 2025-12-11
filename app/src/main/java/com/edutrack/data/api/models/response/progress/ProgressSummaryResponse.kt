package com.edutrack.data.api.models.response.progress

data class ProgressSummaryResponse(
    val totalScore: Int,
    val totalMaxScore: Int,
    val overallProgress: Int
)