package com.koleff.kare_android.data.model.request

import com.koleff.kare_android.data.model.dto.DoWorkoutPerformanceMetricsDto
import com.squareup.moshi.Json

data class UpdatePerformanceMetricsRequest(
    @field:Json(name = "performance_metrics")
    val performanceMetrics: DoWorkoutPerformanceMetricsDto
)