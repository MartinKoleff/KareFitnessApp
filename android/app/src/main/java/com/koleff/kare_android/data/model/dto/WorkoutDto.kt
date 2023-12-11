package com.koleff.kare_android.data.model.dto

import com.koleff.kare_android.data.room.dto.Workout
import com.squareup.moshi.Json

data class WorkoutDto(
    @field:Json(name = "id")
    val workoutId: Int,
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
) {
    fun toWorkout(): Workout {
        return Workout(
            id = workoutId,
            name = name,
            muscleGroup = muscleGroup,
            snapshot = snapshot,
            totalExercises = totalExercises,
            isSelected = isSelected
        )
    }
}