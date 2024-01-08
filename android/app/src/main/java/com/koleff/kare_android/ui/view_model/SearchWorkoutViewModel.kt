package com.koleff.kare_android.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.ui.event.OnSearchWorkoutEvent
import com.koleff.kare_android.ui.state.SearchState
import com.koleff.kare_android.ui.state.WorkoutDetailsState
import com.koleff.kare_android.ui.state.WorkoutState
import com.koleff.kare_android.domain.usecases.WorkoutUseCases
import com.koleff.kare_android.ui.state.UpdateWorkoutState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
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

    fun onTextChange(searchText: String) {
        _searchState.value = searchState.value.copy(
            searchText = searchText
        )

        val event = OnSearchWorkoutEvent.OnSearchTextChange(
            searchText = _searchState.value.searchText,
            workouts = originalWorkoutList
        )

        onSearchEvent(event)
    }

    fun onToggleSearch() {
        val isSearching = searchState.value.isSearching
        _searchState.value = searchState.value.copy(
            isSearching = !isSearching
        )

        val event = OnSearchWorkoutEvent.OnToggleSearch(
            isSearching = searchState.value.isSearching,
            workouts = originalWorkoutList
        )

        onSearchEvent(event)
    }

    private fun onSearchEvent(event: OnSearchWorkoutEvent) {
        viewModelScope.launch(dispatcher) {
            workoutUseCases.onSearchWorkoutUseCase(event).collect { workoutState ->
                _workoutsState.value = workoutState
            }
        }
    }

    init {
        getWorkouts()
    }

    private fun getWorkouts() {
        viewModelScope.launch(dispatcher) {
            workoutUseCases.getWorkoutsUseCase().collect { workoutState ->
                _workoutsState.value = workoutState

                if (workoutState.isSuccessful) {
                    originalWorkoutList = workoutState.workoutList ?: emptyList()
                }
            }
        }
    }

    fun getWorkoutDetails(workoutId: Int) {
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

    fun resetUpdateWorkoutState() {
        _updateWorkoutState.value = WorkoutDetailsState()
    }
}
