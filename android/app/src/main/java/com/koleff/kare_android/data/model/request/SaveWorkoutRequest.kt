package com.koleff.kare_android.data.model.request

import com.koleff.kare_android.data.model.dto.SaveWorkoutDto
import com.squareup.moshi.Json

data class SaveWorkoutRequest(
    @field:Json(name = "workout")
    val workout: SaveWorkoutDto, //Like WorkoutDto but no id...
)