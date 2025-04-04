package com.koleff.kare_android.ui.view_model

import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.internal.Logger
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.common.navigation.NavigationController
import com.koleff.kare_android.common.preferences.Preferences
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.domain.usecases.ExerciseUseCases
import com.koleff.kare_android.domain.usecases.WorkoutUseCases
import com.koleff.kare_android.domain.usecases.statistics.StatisticsUseCases
import com.koleff.kare_android.ui.event.OnSearchExerciseEvent
import com.koleff.kare_android.ui.event.OnSearchWorkoutEvent
import com.koleff.kare_android.ui.state.ExerciseListState
import com.koleff.kare_android.ui.state.ExercisePRState
import com.koleff.kare_android.ui.state.ExerciseState
import com.koleff.kare_android.ui.state.ExerciseStatisticsState
import com.koleff.kare_android.ui.state.GeneralStatisticsState
import com.koleff.kare_android.ui.state.MuscleGroupState
import com.koleff.kare_android.ui.state.SearchState
import com.koleff.kare_android.ui.state.StatisticsState
import com.koleff.kare_android.ui.state.StrongestMuscleGroupState
import com.koleff.kare_android.ui.state.TotalRepsPerformedState
import com.koleff.kare_android.ui.state.TotalSetsPerformedState
import com.koleff.kare_android.ui.state.TotalTimesCompletedState
import com.koleff.kare_android.ui.state.TotalWeightLiftedState
import com.koleff.kare_android.ui.state.WorkoutListState
import com.koleff.kare_android.ui.state.WorkoutState
import com.koleff.kare_android.ui.state.WorkoutStatisticsState
import com.koleff.kare_android.ui.state.WorkoutStreakState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutStatisticsViewModel @Inject constructor(
    private val statisticsUseCases: StatisticsUseCases,
    private val workoutUseCases: WorkoutUseCases,
    private val navigationController: NavigationController,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseViewModel(navigationController = navigationController){

    private var _state: MutableStateFlow<WorkoutStatisticsState> =
        MutableStateFlow(WorkoutStatisticsState())
    val state: StateFlow<WorkoutStatisticsState>
        get() = _state

    //Workout states
    private var _getWorkoutTotalTimesCompletedState: MutableStateFlow<TotalTimesCompletedState> =
        MutableStateFlow(TotalTimesCompletedState())
    val getWorkoutTotalTimesCompletedState: StateFlow<TotalTimesCompletedState>
        get() = _getWorkoutTotalTimesCompletedState

    private var _getWorkoutTotalRepsState: MutableStateFlow<TotalRepsPerformedState> =
        MutableStateFlow(TotalRepsPerformedState())
    val getWorkoutTotalRepsState: StateFlow<TotalRepsPerformedState>
        get() = _getWorkoutTotalRepsState

    private var _getWorkoutTotalSetsState: MutableStateFlow<TotalSetsPerformedState> =
        MutableStateFlow(TotalSetsPerformedState())
    val getWorkoutTotalSetsState: StateFlow<TotalSetsPerformedState>
        get() = _getWorkoutTotalSetsState

    private var _getWorkoutTotalWeightState: MutableStateFlow<TotalWeightLiftedState> =
        MutableStateFlow(TotalWeightLiftedState())
    val getWorkoutTotalWeightState: StateFlow<TotalWeightLiftedState>
        get() = _getWorkoutTotalWeightState

    private var _workoutsState: MutableStateFlow<WorkoutListState> =
        MutableStateFlow(WorkoutListState())
    val workoutsState: StateFlow<WorkoutListState>
        get() = _workoutsState

    private fun getWorkouts() {
        viewModelScope.launch(dispatcher) {
            workoutUseCases.getAllWorkoutsUseCase().collect { workoutState ->
                _workoutsState.value = workoutState

                if (workoutState.isSuccessful) {
                    originalWorkoutList = workoutState.workoutList
                }
            }
        }
    }

    //Search
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
            isSearching = _searchState.value.isSearching,
            workouts = originalWorkoutList
        )

        onSearchEvent(event)
    }

    private fun onSearchEvent(event: OnSearchWorkoutEvent) {
        viewModelScope.launch(dispatcher) {
            workoutUseCases.onSearchWorkoutUseCase(event).collect { workoutState ->
                _workoutsState.value = workoutState

                Logger.getLogger().i("Search workout state: ${workoutState.workoutList}")
            }
        }
    }

    private fun observeWorkoutStatisticsState() {
        viewModelScope.launch {
            combine(
                _getWorkoutTotalTimesCompletedState,
                _getWorkoutTotalRepsState,
                _getWorkoutTotalSetsState,
                _getWorkoutTotalWeightState,
            ) { values ->
                val workoutStats = WorkoutStatisticsState(
                    getWorkoutTotalTimesCompletedState = values[0] as TotalTimesCompletedState,
                    getWorkoutTotalRepsPerformedState = values[1] as TotalRepsPerformedState,
                    getWorkoutTotalSetsPerformedState = values[2] as TotalSetsPerformedState,
                    getWorkoutTotalWeightLiftedState = values[3] as TotalWeightLiftedState
                )

                _state.value = workoutStats
            }
        }
    }

    private fun getWorkoutStatistics(selectedWorkout: WorkoutDto) {
        getWorkoutTotalTimesCompleted(workoutId = selectedWorkout.workoutId)
//        getWorkoutTotalRepsPerformed(workoutId = selectedWorkout.workoutId)
//        getWorkoutTotalSetsPerformed(workoutId = selectedWorkout.workoutId)
        getWorkoutTotalWeightLifted(workoutId = selectedWorkout.workoutId)
    }

    init {
        getWorkouts()
        observeWorkoutStatisticsState()
//        getWorkoutStatistics()
    }

    //Workout stats
    fun getWorkoutTotalTimesCompleted(workoutId: Int) {
        viewModelScope.launch(dispatcher) {
            statisticsUseCases.workoutStatisticsUseCases.getWorkoutTotalTimesCompletedUseCase(
                workoutId
            ).collect { getWorkoutTotalTimesCompletedState ->
                _getWorkoutTotalTimesCompletedState.value = getWorkoutTotalTimesCompletedState
            }
        }
    }

    fun getWorkoutTotalRepsPerformed(workoutId: Int, exerciseId: Int) {
        viewModelScope.launch(dispatcher) {
            statisticsUseCases.workoutStatisticsUseCases.getWorkoutTotalRepsPerformedUseCase(
                workoutId,
                exerciseId
            ).collect { getWorkoutTotalRepsState ->
                _getWorkoutTotalRepsState.value = getWorkoutTotalRepsState
            }
        }
    }

    fun getWorkoutTotalSetsPerformed(workoutId: Int, exerciseId: Int) {
        viewModelScope.launch(dispatcher) {
            statisticsUseCases.workoutStatisticsUseCases.getWorkoutTotalSetsPerformedUseCase(
                workoutId,
                exerciseId
            ).collect { getWorkoutTotalSetsState ->
                _getWorkoutTotalSetsState.value = getWorkoutTotalSetsState
            }
        }
    }

    fun getWorkoutTotalWeightLifted(workoutId: Int) {
        viewModelScope.launch(dispatcher) {
            statisticsUseCases.workoutStatisticsUseCases.getWorkoutTotalWeightLiftedUseCase(
                workoutId
            ).collect { getWorkoutTotalWeightState ->
                _getWorkoutTotalWeightState.value = getWorkoutTotalWeightState
            }
        }
    }


    override fun clearError() {
        if (_getWorkoutTotalTimesCompletedState.value.isError) {
            _getWorkoutTotalTimesCompletedState.value = TotalTimesCompletedState()
        }
        if (_getWorkoutTotalRepsState.value.isError) {
            _getWorkoutTotalRepsState.value = TotalRepsPerformedState()
        }
        if (_getWorkoutTotalSetsState.value.isError) {
            _getWorkoutTotalSetsState.value = TotalSetsPerformedState()
        }
        if (_getWorkoutTotalWeightState.value.isError) {
            _getWorkoutTotalWeightState.value = TotalWeightLiftedState()
        }
    }
}