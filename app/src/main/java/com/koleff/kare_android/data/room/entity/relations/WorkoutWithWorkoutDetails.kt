package com.koleff.kare_android.data.room.entity.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.koleff.kare_android.data.room.entity.Workout
import com.koleff.kare_android.data.room.entity.WorkoutDetails

data class WorkoutWithWorkoutDetails(
    @Embedded
    val workout: Workout,

    @Relation(
        parentColumn = "workoutId",
        entityColumn = "workoutDetailsId",
        entity = WorkoutDetails::class
    )
    val workoutDetails: WorkoutDetails
)