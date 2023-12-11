package com.koleff.kare_android.data.model.event

sealed class OnWorkoutScreenSwitchEvent{
    object SelectedWorkout : OnWorkoutScreenSwitchEvent()
    object AllWorkouts : OnWorkoutScreenSwitchEvent()
}
