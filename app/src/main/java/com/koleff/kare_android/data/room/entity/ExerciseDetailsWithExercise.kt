package com.koleff.kare_android.data.room.entity

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
        entity = Exercise::class
    )
    val exercise: Exercise
)