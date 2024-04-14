package com.koleff.kare_android.data.room.entity.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.koleff.kare_android.data.room.entity.WorkoutDetails

data class WorkoutDetailsWithExercisesWithSets(
    @Embedded
    val workoutDetails: WorkoutDetails,

    //Fetches all exercises and all their sets for the given workout
    @Relation(
        parentColumn = "workoutId",
        entityColumn = "workoutId",
        associateBy = Junction(ExerciseSetCrossRef::class)
    )
    val exercisesWithSets: List<ExerciseWithSets>
)
