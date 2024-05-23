package com.koleff.kare_android.data.model.request

import com.koleff.kare_android.data.model.dto.DoWorkoutExerciseSetDto
import com.squareup.moshi.Json

data class SaveDoWorkoutExerciseSetRequest(
    @field:Json(name = "do_workout_exercise_set")
    val doWorkoutExerciseSet: DoWorkoutExerciseSetDto
)