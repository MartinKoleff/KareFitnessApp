package com.koleff.kare_android.data.room.entity.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.koleff.kare_android.data.room.entity.Exercise
import com.koleff.kare_android.data.room.entity.ExerciseDetails

data class ExerciseDetailsWithExercise(
    @Embedded
    val exerciseDetails: ExerciseDetails,

    @Relation(
        parentColumn = "exerciseDetailsId",
        entityColumn = "exerciseId",
        associateBy = Junction(ExerciseDetailsExerciseCrossRef::class)
    )
    val exercises: Exercise
)