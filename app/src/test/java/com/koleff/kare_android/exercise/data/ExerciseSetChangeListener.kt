package com.koleff.kare_android.exercise.data

import com.koleff.kare_android.data.room.entity.ExerciseSet

interface ExerciseSetChangeListener {

    suspend fun onSetAdded(exerciseSet: ExerciseSet)

    suspend fun onSetUpdated(exerciseSet: ExerciseSet)

    suspend fun onSetDeleted(exerciseSet: ExerciseSet)

    suspend fun onSetsDeleted(exerciseId: Int, workoutId: Int)
}