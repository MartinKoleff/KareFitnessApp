package com.koleff.kare_android.data.model.dto

import com.squareup.moshi.Json

data class WorkoutDetailsDto(
    @field:Json(name = "id")
    val workoutId: String,
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "description")
    val description: String,
    @field:Json(name = "muscle_group")
    val muscleGroup: MuscleGroup,
    @field:Json(name = "exercises")
    val exercises: List<ExerciseDto>
)