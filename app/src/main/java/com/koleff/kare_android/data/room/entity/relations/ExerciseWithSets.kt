package com.koleff.kare_android.data.room.entity.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.room.entity.Exercise
import com.koleff.kare_android.data.room.entity.ExerciseSet

data class ExerciseWithSets(
    val exercise: Exercise,
    val sets: List<ExerciseSet>

) {
    fun toExerciseDto(): ExerciseDto {
        return ExerciseDto(
            exerciseId = this.exercise.exerciseId,
            workoutId = this.exercise.workoutId,
            name = this.exercise.name,
            muscleGroup = this.exercise.muscleGroup,
            machineType = this.exercise.machineType,
            snapshot = this.exercise.snapshot,
            sets = this.sets.map { it.toExerciseSetDto() }.sortedBy { it.number }
        )
    }
}