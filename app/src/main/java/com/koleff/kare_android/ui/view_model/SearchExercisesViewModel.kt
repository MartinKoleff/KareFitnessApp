package com.koleff.kare_android.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.ui.event.OnSearchExerciseEvent
import com.koleff.kare_android.ui.state.ExercisesState
import com.koleff.kare_android.ui.state.SearchState
import com.koleff.kare_android.domain.usecases.ExerciseUseCases
import dagger.assisted.AssistedFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchExercisesViewModel @Inject constructor(
    private val exerciseUseCases: ExerciseUseCases,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _state: MutableStateFlow<ExercisesState> = MutableStateFlow(ExercisesState())
    val state: StateFlow<ExercisesState>
        get() = _state

    private var originalExerciseList: List<ExerciseDto> = mutableListOf()

    private val _searchState: MutableStateFlow<SearchState> = MutableStateFlow(SearchState())
    val searchState: StateFlow<SearchState>
        get() = _searchState

//    val searchState = _searchState
//        .debounce(Constants.fakeSmallDelay)
//        .onEach { exercisesState ->  exercisesState.isSearching = true }
//        .combine(_state) { searchState, exercisesState ->
//            when {
//                searchState.searchText.isNotEmpty() -> exercisesState.exerciseList.filter { exercises ->
//                    exercises.name.contains(searchState.searchText, ignoreCase = true)
//                }
//
//                else -> exercisesState.exerciseList
//            }
//        }.stateIn(
//            scope = viewModelScope,
//            initialValue = _searchState,
//            started = SharingStarted.WhileSubscribed(5000L)
//        )

    init {
        getExercises(MuscleGroup.ALL.muscleGroupId)
    }

    fun onTextChange(searchText: String) {
        _searchState.value = searchState.value.copy(
            searchText = searchText
        )

        val event = OnSearchExerciseEvent.OnSearchTextChange(
            searchText = _searchState.value.searchText,
            exercises = originalExerciseList
        )

        onSearchEvent(event)
    }

    fun onToggleSearch() {
        val isSearching = searchState.value.isSearching
        _searchState.value = searchState.value.copy(
            isSearching = !isSearching
        )

        val event = OnSearchExerciseEvent.OnToggleSearch(
            isSearching = searchState.value.isSearching,
            exercises = originalExerciseList
        )

        onSearchEvent(event)
    }

    private fun onSearchEvent(event: OnSearchExerciseEvent) {
        viewModelScope.launch(dispatcher) {
            exerciseUseCases.onSearchExerciseUseCase(event).collect { exerciseState ->
                _state.value = exerciseState
            }
        }
    }

    private fun getExercises(muscleGroupId: Int) {
        viewModelScope.launch(dispatcher) {
            exerciseUseCases.getExercisesUseCase(muscleGroupId).collect{exerciseState ->
                _state.value = exerciseState

                if(_state.value.isSuccessful){
                    originalExerciseList = _state.value.exerciseList
                }
            }
        }
    }
}
