package com.koleff.kare_android.data.model.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity
data class WorkoutDto(
    @field:Json(name = "id")
    @PrimaryKey(autoGenerate = true)
    val workoutId: String,
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "muscle_group")
    val muscleGroup: MuscleGroup,
    @field:Json(name = "snapshot")
    val snapshot: String, //TODO: image...
    @field:Json(name = "total_exercises")
    val totalExercises: Int,
    @field:Json(name = "is_selected")
    val isSelected: Boolean,
)