package com.koleff.kare_android.data.model.dto

import com.koleff.kare_android.data.KareEntity
import com.koleff.kare_android.data.room.entity.Workout
import com.squareup.moshi.Json

data class WorkoutDto(
    @field:Json(name = "id")
    val workoutId: Int = 0, //0 -> DB generates new id
    @field:Json(name = "name")
    val name: String = "",
    @field:Json(name = "muscle_group")
    val muscleGroup: MuscleGroup = MuscleGroup.NONE,
    @field:Json(name = "snapshot")
    val snapshot: String = "", //TODO: image...
    @field:Json(name = "total_exercises")
    val totalExercises: Int = 0,
    @field:Json(name = "is_favorite")
    val isFavorite: Boolean = false,
): KareEntity<Workout> {
    override fun toEntity(): Workout {
        return Workout(
            workoutId = workoutId,
            name = name,
            muscleGroup = muscleGroup,
            snapshot = snapshot,
            totalExercises = totalExercises,
            isFavorite = isFavorite
        )
    }
}