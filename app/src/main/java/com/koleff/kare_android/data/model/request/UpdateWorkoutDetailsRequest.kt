package com.koleff.kare_android.data.model.request

import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.squareup.moshi.Json

data class UpdateWorkoutDetailsRequest(
    @field:Json(name = "workout_details")
    val workoutDetails: WorkoutDetailsDto,
)