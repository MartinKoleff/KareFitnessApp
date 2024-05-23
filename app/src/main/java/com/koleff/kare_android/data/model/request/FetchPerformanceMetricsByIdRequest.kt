package com.koleff.kare_android.data.model.request

import com.squareup.moshi.Json
import java.util.Date

data class FetchPerformanceMetricsByIdRequest(
    @field:Json(name = "performance_metrics_id")
    val id: Int,
)