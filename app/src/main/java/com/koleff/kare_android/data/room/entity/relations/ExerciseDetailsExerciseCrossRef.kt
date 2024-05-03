package com.koleff.kare_android.data.room.entity.relations

import androidx.room.Entity

@Deprecated("Not used")
@Entity(
    tableName = "exercise_details_exercise_cross_ref",
    primaryKeys = ["exerciseDetailsId", "workoutId", "exerciseId"]
)
data class ExerciseDetailsExerciseCrossRef(
    val exerciseDetailsId: Int,
    val exerciseId: Int,
    val workoutId: Int
)