package com.koleff.kare_android.data.model.request

import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.squareup.moshi.Json

data class UpdateWorkoutRequest(
    @field:Json(name = "workout")
    val workout: WorkoutDto,
)