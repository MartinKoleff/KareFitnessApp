package com.koleff.kare_android.workout.data

import com.koleff.kare_android.data.room.entity.Exercise

interface ExerciseChangeListener {

    fun onExerciseAdded(exercise: Exercise)

    fun onExerciseUpdated(exercise: Exercise)

    fun onExerciseDeleted(exercise: Exercise)
}