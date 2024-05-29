package com.koleff.kare_android.ui.event

sealed class OnWorkoutScreenSwitchEvent{
    object FavoriteWorkouts : OnWorkoutScreenSwitchEvent()
    object AllWorkouts : OnWorkoutScreenSwitchEvent()
}
