package com.koleff.kare_android.data.room.entity.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.room.entity.Exercise
import com.koleff.kare_android.data.room.entity.ExerciseDetails
import com.koleff.kare_android.data.room.entity.SetEntity

data class ExerciseWithSet(
    @Embedded
    val exercise: Exercise,

    @Relation(
        parentColumn = "exerciseId",
        entityColumn = "setId",
        associateBy = Junction(ExerciseSetCrossRef::class)
    )
    val sets: List<SetEntity>
) {
    fun toExerciseDto(): ExerciseDto {
        return ExerciseDto(
            exerciseId = this.exercise.exerciseId,
            name = this.exercise.name,
            muscleGroup = this.exercise.muscleGroup,
            machineType = this.exercise.machineType,
            snapshot = this.exercise.snapshot,
            sets = this.sets.map { it.toExerciseSet() }.sortedBy { it.number }
        )
    }
}