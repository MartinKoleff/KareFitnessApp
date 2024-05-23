package com.koleff.kare_android.data.model.request

import com.squareup.moshi.Json
import java.util.Date

data class FetchPerformanceMetricsByDateAndIdRequest(
    @field:Json(name = "performance_metrics_id")
    val id: Int,
    @field:Json(name = "start")
    val start: Date,
    @field:Json(name = "end")
    val end: Date,
)