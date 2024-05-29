package com.koleff.kare_android.data.model.request

import com.koleff.kare_android.data.model.dto.ExerciseSetDto
import com.squareup.moshi.Json
import java.util.UUID

data class DeleteExerciseSetRequest(
    @field:Json(name = "exercise_id")
    val exerciseId: Int,
    @field:Json(name = "workout_id")
    val workoutId: Int,
    @field:Json(name = "exercise_set_id")
    val setId: UUID,
    @field:Json(name = "current_sets")
    val currentSets: List<ExerciseSetDto>,
)
