package com.koleff.kare_android.data.model.request

import com.koleff.kare_android.data.model.dto.DoWorkoutExerciseSetDto
import com.squareup.moshi.Json

data class SaveDoWorkoutExerciseSetsRequest(
    @field:Json(name = "do_workout_exercise_sets")
    val doWorkoutExerciseSets: List<DoWorkoutExerciseSetDto>
)