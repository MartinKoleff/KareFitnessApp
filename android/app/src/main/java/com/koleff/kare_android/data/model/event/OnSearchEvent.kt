package com.koleff.kare_android.data.model.event

import com.koleff.kare_android.data.model.dto.WorkoutDto

sealed class OnSearchEvent(){
    class OnToggleSearch(val isSearching: Boolean = false, val workouts: List<WorkoutDto>) : OnSearchEvent()
    class OnSearchTextChange(val searchText: String, val workouts: List<WorkoutDto>) : OnSearchEvent()
}
