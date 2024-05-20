package com.koleff.kare_android.workout.data

import com.koleff.kare_android.data.room.entity.Exercise

interface ExerciseChangeListener {

    fun onExerciseAdded(exercise: Exercise)

    fun onExerciseUpdated(exercise: Exercise)

    suspend fun onExerciseDeleted(exercise: Exercise)

    suspend fun onExercisesDeleted(workoutId: Int)
}