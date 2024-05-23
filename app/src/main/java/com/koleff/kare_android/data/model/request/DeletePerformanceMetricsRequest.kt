package com.koleff.kare_android.data.model.request

import com.squareup.moshi.Json

data class DeletePerformanceMetricsRequest(
    @field:Json(name = "performance_metrics_id")
    val id: Int,
)
