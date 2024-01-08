package com.koleff.kare_android.ui.event

sealed class OnWorkoutScreenSwitchEvent{
    object SelectedWorkout : OnWorkoutScreenSwitchEvent()
    object AllWorkouts : OnWorkoutScreenSwitchEvent()
}
