package com.koleff.kare_android.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.data.model.event.OnSearchEvent
import com.koleff.kare_android.data.model.state.SearchState
import com.koleff.kare_android.data.model.state.WorkoutDetailsState
import com.koleff.kare_android.data.model.state.WorkoutState
import com.koleff.kare_android.domain.usecases.WorkoutUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchWorkoutViewModel @Inject constructor(
    private val workoutUseCases: WorkoutUseCases,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _selectedWorkoutState: MutableStateFlow<WorkoutDetailsState> =
        MutableStateFlow(WorkoutDetailsState())

    val selectedWorkoutState: StateFlow<WorkoutDetailsState>
        get() = _selectedWorkoutState

    private val _workoutsState: MutableStateFlow<WorkoutState> =
        MutableStateFlow(WorkoutState())

    val workoutsState: StateFlow<WorkoutState>
        get() = _workoutsState

    private val _updateWorkoutState: MutableStateFlow<WorkoutDetailsState> =
        MutableStateFlow(WorkoutDetailsState())

    val updateWorkoutState: StateFlow<WorkoutDetailsState>
        get() = _updateWorkoutState

    private val _searchState: MutableStateFlow<SearchState> = MutableStateFlow(SearchState())

    val searchState: StateFlow<SearchState>
        get() = _searchState

    private var originalWorkoutList: List<WorkoutDto> = mutableListOf()

    fun onSearchEvent(event: OnSearchEvent) {
        when (event) {
            is OnSearchEvent.OnToggleSearch -> {
                val isSearching = _searchState.value.isSearching
                _searchState.value = searchState.value.copy(
                    isSearching = !isSearching
                )

                if (!isSearching) {
                    onSearchEvent(OnSearchEvent.OnSearchTextChange(""))
                }
            }

            is OnSearchEvent.OnSearchTextChange -> {
                _searchState.value = searchState.value.copy(
                    searchText = event.searchText
                )

                //Search filter
                _workoutsState.value = workoutsState.value.copy(
                    workoutList = originalWorkoutList.filter {

                        //Custom search filter...
                        it.name.contains(event.searchText, ignoreCase = true)
                    }
                )
            }
        }
    }

    private fun getWorkouts() {
        viewModelScope.launch(dispatcher) {
            workoutUseCases.getWorkoutsUseCase().collect { workoutState ->
                _workoutsState.value = workoutState
            }
        }
    }

    private fun getWorkoutDetails(workoutId: Int) {
        viewModelScope.launch(dispatcher) {
            workoutUseCases.getWorkoutDetailsUseCase(workoutId).collect { workoutDetailsState ->
                _selectedWorkoutState.value = workoutDetailsState
            }
        }
    }

    fun updateWorkout(workout: WorkoutDetailsDto) {
        viewModelScope.launch(dispatcher) {
            workoutUseCases.updateWorkoutUseCase.invoke(workout).collect { updateWorkoutState ->
                _updateWorkoutState.value = updateWorkoutState
            }
        }
    }

//    @AssistedFactory
//    interface Factory {
//        fun create(workoutId: Int): SearchWorkoutViewModel
//    }
//
//    companion object {
//        fun provideSearchWorkoutViewModelFactory(
//            factory: Factory,
//            workoutId: Int
//        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
//            override fun <T : ViewModel> create(modelClass: Class<T>): T {
//                return factory.create(workoutId) as T
//            }
//        }
//    }
}
