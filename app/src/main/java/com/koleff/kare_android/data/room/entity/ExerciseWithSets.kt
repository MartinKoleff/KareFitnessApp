package com.koleff.kare_android.data.room.entity

import com.koleff.kare_android.data.KareDto
import com.koleff.kare_android.data.model.dto.ExerciseDto

data class ExerciseWithSets(
    val exercise: Exercise,
    val sets: List<ExerciseSet>
): KareDto<ExerciseDto> {
    override fun toDto(): ExerciseDto {
        return ExerciseDto(
            exerciseId = this.exercise.exerciseId,
            workoutId = this.exercise.workoutId,
            name = this.exercise.name,
            muscleGroup = this.exercise.muscleGroup,
            machineType = this.exercise.machineType,
            snapshot = this.exercise.snapshot,
            sets = this.sets.map { it.toDto() }.sortedBy { it.number }
        )
    }
}