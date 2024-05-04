package com.koleff.kare_android.data.room.entity

import androidx.room.Embedded
import androidx.room.Relation

data class WorkoutWithConfig(
    @Embedded val workoutDetails: WorkoutDetails,
    @Relation(
        parentColumn = "workoutDetailsId",
        entityColumn = "workoutId"
    )
    val configuration: WorkoutConfiguration?
)