package com.koleff.kare_android.workout.data

import com.koleff.kare_android.data.room.entity.ExerciseSet
import com.koleff.kare_android.exercise.data.ExerciseSetChangeListener

class CompositeExerciseSetChangeListener : ExerciseSetChangeListener {
    private val listeners = mutableListOf<ExerciseSetChangeListener>()

    fun addListener(listener: ExerciseSetChangeListener) {
        listeners.add(listener)
    }

    override suspend fun onSetAdded(exerciseSet: ExerciseSet) {
        listeners.forEach { it.onSetAdded(exerciseSet) }
    }

    override suspend fun onSetUpdated(exerciseSet: ExerciseSet) {
        listeners.forEach { it.onSetUpdated(exerciseSet) }
    }

    override suspend fun onSetDeleted(exerciseSet: ExerciseSet) {
        listeners.forEach { it.onSetDeleted(exerciseSet) }
    }

    override suspend fun onSetsDeleted(exerciseId: Int, workoutId: Int) {
        listeners.forEach { it.onSetsDeleted(exerciseId, workoutId) }
    }
}