package com.koleff.kare_android.data.model.dto

import com.squareup.moshi.Json

data class WorkoutDto(
    @field:Json(name = "id")
    val workoutId: String,
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "muscle_group")
    val muscleGroup: MuscleGroup,
    @field:Json(name = "snapshot")
    val snapshot: String, //TODO: image...
)