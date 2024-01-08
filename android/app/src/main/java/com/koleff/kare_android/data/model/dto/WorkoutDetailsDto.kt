package com.koleff.kare_android.data.model.dto

import com.koleff.kare_android.data.room.entity.WorkoutDetails
import com.squareup.moshi.Json

data class WorkoutDetailsDto(
    @field:Json(name = "id")
    val workoutId: Int = 0, //0 -> DB generates new id
    @field:Json(name = "name")
    val name: String = "Workout $workoutId",
    @field:Json(name = "description")
    val description: String = "",
    @field:Json(name = "muscle_group")
    val muscleGroup: MuscleGroup = MuscleGroup.NONE,
    @field:Json(name = "exercises")
    val exercises: MutableList<ExerciseDto> = mutableListOf(),
    @field:Json(name = "is_selected")
    val isSelected: Boolean = false
){
    fun toWorkoutDetails(): WorkoutDetails{
        return WorkoutDetails(
            workoutDetailsId = workoutId,
            name = name,
            description = description,
            muscleGroup = muscleGroup,
            isSelected = isSelected
        )
    }
}
