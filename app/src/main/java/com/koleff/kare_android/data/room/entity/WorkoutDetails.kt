package com.koleff.kare_android.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto

@Entity(tableName = "workout_details_table")
data class WorkoutDetails(
    @PrimaryKey
    val workoutDetailsId: Int,
    val name: String,
    val description: String,
    val muscleGroup: MuscleGroup,
    var isSelected: Boolean
){
    fun toWorkoutDetailsDto(exercises: MutableList<ExerciseDto>): WorkoutDetailsDto {
        return WorkoutDetailsDto(
            workoutId = workoutDetailsId,
            name = name,
            description = description,
            muscleGroup = muscleGroup,
            exercises = exercises,
            isSelected = isSelected
        )
    }
}
