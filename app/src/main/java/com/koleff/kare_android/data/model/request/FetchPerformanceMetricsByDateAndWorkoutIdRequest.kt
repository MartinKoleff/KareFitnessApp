package com.koleff.kare_android.data.model.request

import com.squareup.moshi.Json
import java.util.Date

data class FetchPerformanceMetricsByDateAndWorkoutIdRequest(
    @field:Json(name = "workoutId")
    val workoutId: Int,
    @field:Json(name = "start")
    val start: Date,
    @field:Json(name = "end")
    val end: Date,
)