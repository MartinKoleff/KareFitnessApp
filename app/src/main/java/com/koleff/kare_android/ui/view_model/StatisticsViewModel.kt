package com.koleff.kare_android.ui.view_model

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.common.navigation.NavigationController
import com.koleff.kare_android.common.preferences.Preferences
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.domain.usecases.ExerciseUseCases
import com.koleff.kare_android.domain.usecases.WorkoutUseCases
import com.koleff.kare_android.domain.usecases.statistics.StatisticsUseCases
import com.koleff.kare_android.ui.event.OnSearchWorkoutEvent
import com.koleff.kare_android.ui.state.ExerciseListState
import com.koleff.kare_android.ui.state.ExercisePRState
import com.koleff.kare_android.ui.state.ExerciseState
import com.koleff.kare_android.ui.state.MuscleGroupState
import com.koleff.kare_android.ui.state.SearchState
import com.koleff.kare_android.ui.state.StrongestMuscleGroupState
import com.koleff.kare_android.ui.state.TotalRepsPerformedState
import com.koleff.kare_android.ui.state.TotalSetsPerformedState
import com.koleff.kare_android.ui.state.TotalTimesCompletedState
import com.koleff.kare_android.ui.state.TotalWeightLiftedState
import com.koleff.kare_android.ui.state.WorkoutListState
import com.koleff.kare_android.ui.state.WorkoutState
import com.koleff.kare_android.ui.state.WorkoutStreakState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val statisticsUseCases: StatisticsUseCases,
    private val exerciseUseCases: ExerciseUseCases,
    private val workoutUseCases: WorkoutUseCases,
    private val preferences: Preferences,
    private val navigationController: NavigationController,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseViewModel(navigationController = navigationController) {

    //Exercise states
    private var _getExercisePRState: MutableStateFlow<ExercisePRState> =
        MutableStateFlow(ExercisePRState())
    val getExercisePRState: StateFlow<ExercisePRState>
        get() = _getExercisePRState

    private var _getExerciseTotalRepsState: MutableStateFlow<TotalRepsPerformedState> =
        MutableStateFlow(TotalRepsPerformedState())
    val getExerciseTotalRepsState: StateFlow<TotalRepsPerformedState>
        get() = _getExerciseTotalRepsState

    private var _getExerciseTotalSetsState: MutableStateFlow<TotalSetsPerformedState> =
        MutableStateFlow(TotalSetsPerformedState())
    val getExerciseTotalSetsState: StateFlow<TotalSetsPerformedState>
        get() = _getExerciseTotalSetsState

    private var _getExerciseTotalWeightState: MutableStateFlow<TotalWeightLiftedState> =
        MutableStateFlow(TotalWeightLiftedState())
    val getExerciseTotalWeightState: StateFlow<TotalWeightLiftedState>
        get() = _getExerciseTotalWeightState

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

    //General states
    private var _getWorkoutsCompletedState: MutableStateFlow<TotalTimesCompletedState> =
        MutableStateFlow(TotalTimesCompletedState())
    val getWorkoutsCompletedState: StateFlow<TotalTimesCompletedState>
        get() = _getWorkoutsCompletedState

    private var _getDistinctWorkoutsCompletedState: MutableStateFlow<TotalTimesCompletedState> =
        MutableStateFlow(TotalTimesCompletedState())
    val getDistinctWorkoutsCompletedState: StateFlow<TotalTimesCompletedState>
        get() = _getDistinctWorkoutsCompletedState

    private var _getWorkoutStreakState: MutableStateFlow<WorkoutStreakState> =
        MutableStateFlow(WorkoutStreakState())
    val getWorkoutStreakState: StateFlow<WorkoutStreakState>
        get() = _getWorkoutStreakState

    private var _getMostFrequentWorkoutState: MutableStateFlow<WorkoutState> =
        MutableStateFlow(WorkoutState())
    val getMostFrequentWorkoutState: StateFlow<WorkoutState>
        get() = _getMostFrequentWorkoutState

    private var _getMostTrainedMuscleGroupState: MutableStateFlow<MuscleGroupState> =
        MutableStateFlow(MuscleGroupState())
    val getMostTrainedMuscleGroupState: StateFlow<MuscleGroupState>
        get() = _getMostTrainedMuscleGroupState

    private var _getMostTrainedExerciseState: MutableStateFlow<ExerciseState> =
        MutableStateFlow(ExerciseState())
    val getMostTrainedExerciseState: StateFlow<ExerciseState>
        get() = _getMostTrainedExerciseState

    private var _getTotalWeightLiftedState: MutableStateFlow<TotalWeightLiftedState> =
        MutableStateFlow(TotalWeightLiftedState())
    val getTotalWeightLiftedState: StateFlow<TotalWeightLiftedState>
        get() = _getTotalWeightLiftedState

    private var _getStrongestMuscleGroupState: MutableStateFlow<StrongestMuscleGroupState> =
        MutableStateFlow(StrongestMuscleGroupState())
    val getStrongestMuscleGroupState: StateFlow<StrongestMuscleGroupState>
        get() = _getStrongestMuscleGroupState

    //Init
    init {
        getCatalogExercises()
        getWorkouts()
    }

    private var _catalogWorkoutsState: MutableStateFlow<ExerciseListState> =
        MutableStateFlow(ExerciseListState())
    val catalogWorkoutsState: StateFlow<ExerciseListState>
        get() = _catalogWorkoutsState

    private var _workoutsState: MutableStateFlow<WorkoutListState> =
        MutableStateFlow(WorkoutListState())
    val workoutsState: StateFlow<WorkoutListState>
        get() = _workoutsState

    private fun getCatalogExercises() {
        viewModelScope.launch(dispatcher) {
            exerciseUseCases.getCatalogExercisesUseCase(muscleGroupId = MuscleGroup.ALL.muscleGroupId)
                .collect { exerciseState ->
                    _catalogWorkoutsState.value = exerciseState
                }
        }
    }

    private fun getWorkouts() {
        viewModelScope.launch(dispatcher) {
            workoutUseCases.getAllWorkoutsUseCase().collect { workoutState ->
                _workoutsState.value = workoutState
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

    //Exercise stats
    fun getExercisePR(exerciseId: Int) {
        viewModelScope.launch(dispatcher) {
            statisticsUseCases.exerciseStatisticsUseCases.getExercisePRUseCase(exerciseId)
                .collect { getExercisePRState ->
                    _getExercisePRState.value = getExercisePRState
                }
        }
    }

    fun getExerciseTotalRepsPerformed(exerciseId: Int) {
        viewModelScope.launch(dispatcher) {
            statisticsUseCases.exerciseStatisticsUseCases.getExerciseTotalRepsPerformedUseCase(
                exerciseId
            ).collect { getExerciseTotalRepsState ->
                _getExerciseTotalRepsState.value = getExerciseTotalRepsState
            }
        }
    }

    fun getExerciseTotalSetsPerformed(exerciseId: Int) {
        viewModelScope.launch(dispatcher) {
            statisticsUseCases.exerciseStatisticsUseCases.getExerciseTotalSetsPerformedUseCase(
                exerciseId
            ).collect { getExerciseTotalSetsState ->
                _getExerciseTotalSetsState.value = getExerciseTotalSetsState
            }
        }
    }

    fun getExerciseTotalWeightLifted(exerciseId: Int) {
        viewModelScope.launch(dispatcher) {
            statisticsUseCases.exerciseStatisticsUseCases.getExerciseTotalWeightLiftedUseCase(
                exerciseId
            ).collect { getExerciseTotalWeightState ->
                _getExerciseTotalWeightState.value = getExerciseTotalWeightState
            }
        }
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

    //General stats
    fun getWorkoutsCompleted() {
        viewModelScope.launch(dispatcher) {
            statisticsUseCases.generalStatisticsUseCases.getWorkoutsCompletedUseCase().collect {
                _getWorkoutsCompletedState.value = it
            }
        }
    }

    fun getDistinctWorkoutsCompleted() {
        viewModelScope.launch(dispatcher) {
            statisticsUseCases.generalStatisticsUseCases.getDistinctWorkoutsCompletedUseCase()
                .collect {
                    _getDistinctWorkoutsCompletedState.value = it
                }
        }
    }

    fun getWorkoutStreak() {
        viewModelScope.launch(dispatcher) {
            statisticsUseCases.generalStatisticsUseCases.getWorkoutStreakUseCase().collect {
                _getWorkoutStreakState.value = it
            }
        }
    }

    fun getMostFrequentWorkout() {
        viewModelScope.launch(dispatcher) {
            statisticsUseCases.generalStatisticsUseCases.getMostFrequentWorkoutUseCase().collect {
                _getMostFrequentWorkoutState.value = it
            }
        }
    }

    fun getMostTrainedMuscleGroup() {
        viewModelScope.launch(dispatcher) {
            statisticsUseCases.generalStatisticsUseCases.getMostTrainedMuscleGroupUseCase()
                .collect {
                    _getMostTrainedMuscleGroupState.value = it
                }
        }
    }

    fun getMostTrainedExercise() {
        viewModelScope.launch(dispatcher) {
            statisticsUseCases.generalStatisticsUseCases.getMostTrainedExerciseUseCase().collect {
                _getMostTrainedExerciseState.value = it
            }
        }
    }

    fun getTotalWeightLifted() {
        viewModelScope.launch(dispatcher) {
            statisticsUseCases.generalStatisticsUseCases.getTotalWeightLiftedUseCase().collect {
                _getTotalWeightLiftedState.value = it
            }
        }
    }

    fun getStrongestMuscleGroup() {
        viewModelScope.launch(dispatcher) {
            statisticsUseCases.generalStatisticsUseCases.getStrongestMuscleGroupUseCase().collect {
                _getStrongestMuscleGroupState.value = it
            }
        }
    }

    override fun clearError() {
        if (_getExercisePRState.value.isError) {
            _getExercisePRState.value = ExercisePRState()
        }
        if (_getExerciseTotalRepsState.value.isError) {
            _getExerciseTotalRepsState.value = TotalRepsPerformedState()
        }
        if (_getExerciseTotalSetsState.value.isError) {
            _getExerciseTotalSetsState.value = TotalSetsPerformedState()
        }
        if (_getExerciseTotalWeightState.value.isError) {
            _getExerciseTotalWeightState.value = TotalWeightLiftedState()
        }
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
        if (_getWorkoutsCompletedState.value.isError) {
            _getWorkoutsCompletedState.value = TotalTimesCompletedState()
        }
        if (_getDistinctWorkoutsCompletedState.value.isError) {
            _getDistinctWorkoutsCompletedState.value = TotalTimesCompletedState()
        }
        if (_getWorkoutStreakState.value.isError) {
            _getWorkoutStreakState.value = WorkoutStreakState()
        }
        if (_getMostFrequentWorkoutState.value.isError) {
            _getMostFrequentWorkoutState.value = WorkoutState()
        }
        if (_getMostTrainedMuscleGroupState.value.isError) {
            _getMostTrainedMuscleGroupState.value = MuscleGroupState()
        }
        if (_getMostTrainedExerciseState.value.isError) {
            _getMostTrainedExerciseState.value = ExerciseState()
        }
        if (_getTotalWeightLiftedState.value.isError) {
            _getTotalWeightLiftedState.value = TotalWeightLiftedState()
        }
        if (_getStrongestMuscleGroupState.value.isError) {
            _getStrongestMuscleGroupState.value = StrongestMuscleGroupState()
        }
    }
}