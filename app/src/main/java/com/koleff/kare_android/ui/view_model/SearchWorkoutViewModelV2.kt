package com.koleff.kare_android.ui.view_model

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.common.navigation.Destination
import com.koleff.kare_android.common.navigation.NavigationController
import com.koleff.kare_android.common.navigation.NavigationEvent
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.ui.event.OnSearchWorkoutEvent
import com.koleff.kare_android.ui.state.SearchState
import com.koleff.kare_android.ui.state.WorkoutDetailsState
import com.koleff.kare_android.ui.state.WorkoutListState
import com.koleff.kare_android.domain.usecases.WorkoutUseCases
import com.koleff.kare_android.ui.state.WorkoutState
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
class SearchWorkoutViewModelV2 @Inject constructor(
    private val workoutUseCases: WorkoutUseCases,
    private val navigationController: NavigationController,
    private val savedStateHandle: SavedStateHandle,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseViewModel(navigationController) {
    val exerciseId = savedStateHandle.get<String>("exercise_id")?.toIntOrNull() ?: -1

    private var _workoutsState: MutableStateFlow<WorkoutListState> =
        MutableStateFlow(WorkoutListState())

    val workoutsState: StateFlow<WorkoutListState>
        get() = _workoutsState

    private var _updateWorkoutState: MutableStateFlow<WorkoutDetailsState> =
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
            searchText = _searchState.value.searchText, workouts = originalWorkoutList
        )

        onSearchEvent(event)
    }

    fun onToggleSearch() {
        val isSearching = searchState.value.isSearching
        _searchState.value = searchState.value.copy(
            isSearching = !isSearching
        )

        val event = OnSearchWorkoutEvent.OnToggleSearch(
            isSearching = searchState.value.isSearching, workouts = originalWorkoutList
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
            workoutUseCases.getAllWorkoutsUseCase().collect { workoutState ->
                _workoutsState.value = workoutState

                if (workoutState.isSuccessful) {
                    originalWorkoutList = workoutState.workoutList ?: emptyList()
                }
            }
        }
    }

    fun updateWorkoutDetails(workout: WorkoutDto) {
        viewModelScope.launch(dispatcher) {
            workoutUseCases.addExerciseUseCase.invoke(workoutId = workout.workoutId, exerciseId = exerciseId)
                .collect { updateWorkoutState ->
                    _updateWorkoutState.value = updateWorkoutState

//                    if (updateWorkoutState.isSuccessful) {
//                        navigateToWorkouts()
//                    }
                }
        }
    }

     fun navigateToWorkouts() {
        super.onNavigationEvent(
            NavigationEvent.PopUpToAndNavigateTo(
                popUpToRoute = Destination.Dashboard.route,
                destinationRoute = Destination.Workouts.route,
                inclusive = false
            )
        )
    }

    override fun clearError() {
        if (workoutsState.value.isError) {
            _workoutsState.value = WorkoutListState()
        }
        if (updateWorkoutState.value.isError) {
            _updateWorkoutState.value = WorkoutDetailsState()
        }
    }
}
