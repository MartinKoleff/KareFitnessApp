package com.koleff.kare_android.data.model.request

import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.data.model.dto.ExerciseSetDto
import com.squareup.moshi.Json

data class AddNewExerciseSetRequest(
    @field:Json(name = "exercise_id")
    val exerciseId: Int,
    @field:Json(name = "workout_id")
    val workoutId: Int,
    @field:Json(name = "current_sets")
    val currentSets: List<ExerciseSetDto>,
)