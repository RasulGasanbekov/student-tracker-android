package com.edutrack.data.api.models.response.progress

import com.edutrack.data.api.models.response.SubjectResponse

data class ProgressResponse(
    val summary: ProgressSummaryResponse,
    val subjects: List<SubjectResponse>
)