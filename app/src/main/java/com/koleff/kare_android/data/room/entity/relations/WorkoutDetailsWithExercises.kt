package com.koleff.kare_android.data.room.entity.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.koleff.kare_android.data.room.entity.Exercise
import com.koleff.kare_android.data.room.entity.WorkoutDetails

data class WorkoutDetailsWithExercises(
    @Embedded
    val workoutDetails: WorkoutDetails,

    @Relation(
        parentColumn = "workoutDetailsId",
        entityColumn = "workoutId",
        entity = Exercise::class,
    )
    val exercises: List<Exercise>?
) {
    val safeExercises: List<Exercise>
        get() = exercises ?: emptyList()
}