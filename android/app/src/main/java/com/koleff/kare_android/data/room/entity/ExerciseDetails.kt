package com.koleff.kare_android.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.koleff.kare_android.data.model.dto.ExerciseDetailsDto
import com.koleff.kare_android.data.model.dto.MachineType
import com.koleff.kare_android.data.model.dto.MuscleGroup

@Entity(tableName = "exercise_details_table")
data class ExerciseDetails(
    @PrimaryKey
    val exerciseDetailsId: Int,
    val name: String,
    val description: String,
    val muscleGroup: MuscleGroup,
    val machineType: MachineType,
    val videoUrl: String
) {
    fun toExerciseDetailsDto(): ExerciseDetailsDto {
        return ExerciseDetailsDto(
            name = name,
            description = description,
            muscleGroup = muscleGroup,
            machineType = machineType,
            videoUrl = videoUrl
        )
    }
}
