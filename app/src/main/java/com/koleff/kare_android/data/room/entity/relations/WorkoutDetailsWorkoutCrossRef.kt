package com.koleff.kare_android.data.room.entity.relations

import androidx.room.Entity

@Deprecated("Not used")
@Entity(
    tableName = "workout_details_workout_cross_ref",
    primaryKeys = ["workoutDetailsId", "workoutId"]
)
data class WorkoutDetailsWorkoutCrossRef(
    val workoutDetailsId: Int,
    val workoutId: Int
)