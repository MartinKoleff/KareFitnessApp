package com.koleff.kare_android.data.model.dto

import androidx.room.PrimaryKey
import com.squareup.moshi.Json

data class SaveWorkoutDto(
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "muscle_group")
    val muscleGroup: MuscleGroup,
    @field:Json(name = "exercises")
    val exercises: List<ExerciseDto>,
    @field:Json(name = "snapshot")
    val snapshot: String, //TODO: image...
    @field:Json(name = "is_selected")
    val isSelected: Boolean,
)