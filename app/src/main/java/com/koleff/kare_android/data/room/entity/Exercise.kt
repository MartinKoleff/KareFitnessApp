package com.koleff.kare_android.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.MachineType
import com.koleff.kare_android.data.model.dto.MuscleGroup

@Entity(
    tableName = "exercise_table"
)
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    val exerciseId: Int,
    val name: String,
    val muscleGroup: MuscleGroup,
    val machineType: MachineType,
    val snapshot: String,
) {
    fun toExerciseDto(sets: List<ExerciseSet>): ExerciseDto {
        return ExerciseDto(
            exerciseId = this.exerciseId,
            name = this.name,
            muscleGroup = this.muscleGroup,
            machineType = this.machineType,
            snapshot = this.snapshot,
            sets = sets
                .map { it.toExerciseSetDto() }
                .toList()
        )
    }
}
