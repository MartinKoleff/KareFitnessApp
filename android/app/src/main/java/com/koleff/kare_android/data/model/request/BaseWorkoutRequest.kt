package com.koleff.kare_android.data.model.request

import com.squareup.moshi.Json

data class BaseWorkoutRequest(
    @field:Json(name = "workout_id")
    val workoutId: String,
)