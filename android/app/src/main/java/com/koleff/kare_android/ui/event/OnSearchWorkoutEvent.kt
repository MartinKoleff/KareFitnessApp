package com.koleff.kare_android.ui.event

import com.koleff.kare_android.data.model.dto.WorkoutDto

sealed class OnSearchWorkoutEvent(){
    class OnToggleSearch(val isSearching: Boolean = false, val workouts: List<WorkoutDto>) : OnSearchWorkoutEvent()
    class OnSearchTextChange(val searchText: String, val workouts: List<WorkoutDto>) : OnSearchWorkoutEvent()
}
