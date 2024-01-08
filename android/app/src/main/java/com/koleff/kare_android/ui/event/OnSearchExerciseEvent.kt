package com.koleff.kare_android.ui.event

import com.koleff.kare_android.data.model.dto.ExerciseDto

sealed class OnSearchExerciseEvent(){
    class OnToggleSearch(val isSearching: Boolean = false, val exercises: List<ExerciseDto>) : OnSearchExerciseEvent()
    class OnSearchTextChange(val searchText: String, val exercises: List<ExerciseDto>) : OnSearchExerciseEvent()
}
