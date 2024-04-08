package com.koleff.kare_android.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.koleff.kare_android.data.model.dto.ExerciseDetailsDto
import com.koleff.kare_android.data.model.dto.MachineType
import com.koleff.kare_android.data.model.dto.MuscleGroup

@Entity(
    tableName = "exercise_details_table",
    primaryKeys = ["exerciseDetailsId", "workoutId"] //Composite key
)
data class ExerciseDetails(
    val exerciseDetailsId: Int,
    val workoutId: Int,
    val name: String,
    val description: String,
    val muscleGroup: MuscleGroup,
    val machineType: MachineType,
    val videoUrl: String
) {
    fun toExerciseDetailsDto(): ExerciseDetailsDto {
        return ExerciseDetailsDto(
            id = exerciseDetailsId,
            workoutId = workoutId,
            name = name,
            description = description,
            muscleGroup = muscleGroup,
            machineType = machineType,
            videoUrl = videoUrl
        )
    }
}
