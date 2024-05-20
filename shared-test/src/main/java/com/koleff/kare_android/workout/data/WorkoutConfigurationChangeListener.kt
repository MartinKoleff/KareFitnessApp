package com.koleff.kare_android.workout.data

import com.koleff.kare_android.data.room.entity.WorkoutConfiguration

interface WorkoutConfigurationChangeListener {

    fun onWorkoutConfigurationUpdated(configuration: WorkoutConfiguration)

    fun onWorkoutConfigurationDeleted(workoutId: Int)
}