package com.koleff.kare_android.data.room.entity.relations

import androidx.room.Entity

//One exercise can be in many workout details and one workout details can have many exercises
@Entity(
    tableName = "workout_details_exercise_cross_ref",
    primaryKeys = ["workoutDetailsId", "exerciseId"]
)
data class WorkoutDetailsExerciseCrossRef(
    val workoutDetailsId: Int,
    val exerciseId: Int
)