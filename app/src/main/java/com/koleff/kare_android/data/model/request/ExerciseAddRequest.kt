package com.koleff.kare_android.data.model.request

import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.squareup.moshi.Json

data class ExerciseAddRequest(
    @field:Json(name = "workout_id")
    val workoutId: Int,
    @field:Json(name = "exercise")
    val exercise: ExerciseDto,
)