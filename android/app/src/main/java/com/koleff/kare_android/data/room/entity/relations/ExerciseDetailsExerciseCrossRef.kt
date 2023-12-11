package com.koleff.kare_android.data.room.entity.relations

import androidx.room.Entity

@Entity(
    tableName = "exercise_details_exercise_cross_ref",
    primaryKeys = ["exerciseDetailsId", "exerciseId"]
)
data class ExerciseDetailsExerciseCrossRef(
    val exerciseDetailsId: Int,
    val exerciseId: Int
)