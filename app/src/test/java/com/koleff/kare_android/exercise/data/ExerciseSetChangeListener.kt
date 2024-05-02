package com.koleff.kare_android.exercise.data

import com.koleff.kare_android.data.room.entity.ExerciseSet

interface ExerciseSetChangeListener {

    fun onSetAdded(exerciseSet: ExerciseSet)

    fun onSetUpdated(exerciseSet: ExerciseSet)

    fun onSetDeleted(exerciseSet: ExerciseSet)

    fun onSetsDeleted(exerciseId: Int, workoutId: Int)
}