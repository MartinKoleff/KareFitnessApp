package com.koleff.kare_android.data.model.request

import com.koleff.kare_android.common.Constants
import com.squareup.moshi.Json

data class FetchExerciseRequest(
    @field:Json(name = "exercise_id")
    val exerciseId: Int,
    @field:Json(name = "workout_id")
    val workoutId: Int
)