package com.koleff.kare_android.ui.event

import com.koleff.kare_android.data.model.dto.WorkoutDto

sealed class OnSearchWorkoutEvent(){
    class OnToggleSearch(val isSearching: Boolean = false, val workouts: List<WorkoutDto>) : OnSearchWorkoutEvent(){
        override fun toString(): String {
            return "OnToggleSearch(isSearching=$isSearching, workouts=$workouts)"
        }
    }
    class OnSearchTextChange(val searchText: String, val workouts: List<WorkoutDto>) : OnSearchWorkoutEvent(){
        override fun toString(): String {
            return "OnSearchTextChange(searchText='$searchText', workouts=$workouts)"
        }
    }
}
