package com.koleff.kare_android.data.model.request

import com.koleff.kare_android.data.model.dto.WorkoutConfigurationDto
import com.squareup.moshi.Json

data class FetchWorkoutConfigurationRequest(
    @field:Json(name = "workout_configuration")
    val workoutConfiguration: WorkoutConfigurationDto
)