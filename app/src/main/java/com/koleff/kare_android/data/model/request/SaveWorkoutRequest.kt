package com.koleff.kare_android.data.model.request

import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.squareup.moshi.Json

data class SaveWorkoutRequest(
    @field:Json(name = "workout")
    val workout: WorkoutDetailsDto,
)