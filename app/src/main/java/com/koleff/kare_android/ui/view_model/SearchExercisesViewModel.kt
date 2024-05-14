package com.koleff.kare_android.ui.view_model

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.common.navigation.Destination
import com.koleff.kare_android.common.navigation.NavigationController
import com.koleff.kare_android.common.navigation.NavigationEvent
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.ui.event.OnSearchExerciseEvent
import com.koleff.kare_android.ui.state.ExerciseListState
import com.koleff.kare_android.ui.state.SearchState
import com.koleff.kare_android.domain.usecases.ExerciseUseCases
import com.koleff.kare_android.domain.usecases.WorkoutUseCases
import com.koleff.kare_android.ui.event.OnExerciseListUpdateEvent
import com.koleff.kare_android.ui.event.OnExerciseUpdateEvent
import com.koleff.kare_android.ui.event.OnMultipleExercisesUpdateEvent
import com.koleff.kare_android.ui.state.WorkoutDetailsState
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
    private val savedStateHandle: SavedStateHandle,
    private val workoutUseCases: WorkoutUseCases,
    private val navigationController: NavigationController,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseViewModel(navigationController) {
    val workoutId: Int = savedStateHandle.get<String>("workout_id")?.toIntOrNull() ?: -1

    private var _state: MutableStateFlow<ExerciseListState> = MutableStateFlow(ExerciseListState())
    val state: StateFlow<ExerciseListState>
        get() = _state

    private var originalExerciseList: List<ExerciseDto> = mutableListOf()

    private val _searchState: MutableStateFlow<SearchState> = MutableStateFlow(SearchState())
    val searchState: StateFlow<SearchState>
        get() = _searchState

    private var _updateWorkoutState: MutableStateFlow<WorkoutDetailsState> =
        MutableStateFlow(WorkoutDetailsState())
    val updateWorkoutState: StateFlow<WorkoutDetailsState>
        get() = _updateWorkoutState

//    val searchState = _searchState
//        .debounce(Constants.fakeDelay)
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
            exerciseUseCases.getCatalogExercisesUseCase(muscleGroupId).collect { exerciseState ->
                _state.value = exerciseState

                if (_state.value.isSuccessful) {
                    originalExerciseList = _state.value.exerciseList
                }
            }
        }
    }

    /**
     * Unused
     */
    fun navigateToExerciseDetailsConfigurator(exerciseId: Int, workoutId: Int, muscleGroupId: Int) {
        super.onNavigationEvent(
            NavigationEvent.PopUpToAndNavigateTo(
                popUpToRoute = Destination.Workouts.route,
                destinationRoute = Destination.ExerciseDetailsConfigurator(
                    exerciseId,
                    muscleGroupId,
                    workoutId
                ).route,
                inclusive = false
            )
        )
    }

    override fun clearError() {
        if (state.value.isError) {
            _state.value = ExerciseListState()
        }
    }

    /**
     * Unused
     */
    fun onExerciseUpdateEvent(event: OnExerciseUpdateEvent) {
        when (event) {
            is OnExerciseUpdateEvent.OnExerciseDelete -> {
                val exercise = event.exercise

                viewModelScope.launch(dispatcher) {
                    workoutUseCases.deleteExerciseUseCase(
                        workoutId = workoutId,
                        exerciseId = exercise.exerciseId
                    ).collect { updateWorkoutState ->
                        _updateWorkoutState.value = updateWorkoutState

                        //Go to workout details
                        if (updateWorkoutState.isSuccessful) {
                            navigateToWorkoutDetails()
                        }
                    }
                }
            }

            is OnExerciseUpdateEvent.OnExerciseSubmit -> {
                val exercise = event.exercise

                viewModelScope.launch(dispatcher) {
                    workoutUseCases.addExerciseUseCase(
                        workoutId = workoutId,
                        exercise = exercise
                    ).collect { updateWorkoutState ->
                        _updateWorkoutState.value = updateWorkoutState
                    }
                }
            }
        }
    }

    fun onMultipleExercisesUpdateEvent(event: OnMultipleExercisesUpdateEvent) {
        when (event) {
            is OnMultipleExercisesUpdateEvent.OnMultipleExercisesDelete -> {
                val exercises = event.exerciseList
                val exerciseIds = exercises.map { it.exerciseId }

                viewModelScope.launch(dispatcher) {
                    workoutUseCases.deleteMultipleExercisesUseCase(
                        workoutId = workoutId,
                        exerciseIds = exerciseIds
                    ).collect { updateWorkoutState ->
                        _updateWorkoutState.value = updateWorkoutState

                        //Go to workout details
                        if (updateWorkoutState.isSuccessful) {
                            navigateToWorkoutDetails()
                        }
                    }
                }
            }

            is OnMultipleExercisesUpdateEvent.OnMultipleExercisesSubmit -> {
                val exercises = event.exerciseList

                viewModelScope.launch(dispatcher) {
                    workoutUseCases.addMultipleExercisesUseCase(
                        workoutId = workoutId,
                        exerciseList = exercises
                    ).collect { updateWorkoutState ->
                        _updateWorkoutState.value = updateWorkoutState
                    }
                }
            }
        }
    }

    private fun navigateToWorkoutDetails() {
        super.onNavigationEvent(
            NavigationEvent.PopUpToAndNavigateTo(
                destinationRoute = Destination.WorkoutDetails(
                    workoutId
                ).route,
                popUpToRoute = Destination.Workouts.route,
                inclusive = false
            )
        )
    }
}
