package com.koleff.kare_android.data.model.request

import com.squareup.moshi.Json

data class ExerciseRequest(
    @field:Json(name = "workout_id")
    val workoutId: Int,
    @field:Json(name = "exercise_id")
    val exerciseId: Int,
)