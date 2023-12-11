package com.koleff.kare_android.data.room.entity.relations

import androidx.room.Entity

@Entity(
    tableName = "workout_exercise_cross_ref",
    primaryKeys = ["workoutDetailsId", "workoutId"]
)
data class WorkoutDetailsWorkoutCrossRef(
    val workoutDetailsId: Int,
    val workoutId: Int
)