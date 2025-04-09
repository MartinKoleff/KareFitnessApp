package com.koleff.kare_android.ui.view_model

import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.internal.Logger
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.common.navigation.NavigationController
import com.koleff.kare_android.domain.usecases.statistics.StatisticsUseCases
import com.koleff.kare_android.ui.state.ExerciseState
import com.koleff.kare_android.ui.state.GeneralStatisticsState
import com.koleff.kare_android.ui.state.MuscleGroupState
import com.koleff.kare_android.ui.state.StrongestMuscleGroupState
import com.koleff.kare_android.ui.state.TotalTimesCompletedState
import com.koleff.kare_android.ui.state.TotalWeightLiftedState
import com.koleff.kare_android.ui.state.WorkoutState
import com.koleff.kare_android.ui.state.WorkoutStreakState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GeneralStatisticsViewModel @Inject constructor(
    private val statisticsUseCases: StatisticsUseCases,
    private val navigationController: NavigationController,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseViewModel(navigationController = navigationController){

    private var _state: MutableStateFlow<GeneralStatisticsState> =
        MutableStateFlow(GeneralStatisticsState())
    val state: StateFlow<GeneralStatisticsState>
        get() = _state

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

    init {
        observeGeneralStatisticsState()
        getGeneralStatistics()
    }

    private fun observeGeneralStatisticsState() {
        viewModelScope.launch {
            combine(
                _getWorkoutsCompletedState,
                _getDistinctWorkoutsCompletedState,
                _getWorkoutStreakState,
                _getMostFrequentWorkoutState,
                _getMostTrainedMuscleGroupState,
                _getMostTrainedExerciseState,
                _getTotalWeightLiftedState,
                _getStrongestMuscleGroupState,
            ) { values ->
                val generalStats = GeneralStatisticsState(
                    getWorkoutsCompletedState = values[0] as TotalTimesCompletedState,
                    getDistinctWorkoutsCompletedState = values[1] as TotalTimesCompletedState,
                    getWorkoutStreakUseCase = values[2] as WorkoutStreakState,
                    getMostFrequentWorkoutState = values[3] as WorkoutState,
                    getMostTrainedMuscleGroupState = values[4] as MuscleGroupState,
                    getMostTrainedExerciseState = values[5] as ExerciseState,
                    getTotalWeightLiftedState = values[6] as TotalWeightLiftedState,
                    getStrongestMuscleGroupState = values[7] as StrongestMuscleGroupState
                )

                Logger.getLogger().i("[GeneralStatisticsViewModel] General statistics state changed: $generalStats")

                _state.value = generalStats
            }.stateIn(viewModelScope, SharingStarted.Eagerly, GeneralStatisticsState())
        }
    }

    private fun getGeneralStatistics() {
        getWorkoutsCompleted()
        getDistinctWorkoutsCompleted()
        getWorkoutStreak()
        getMostFrequentWorkout()
        getMostTrainedMuscleGroup()
        getMostTrainedExercise()
        getTotalWeightLifted()
        getStrongestMuscleGroup()
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
            statisticsUseCases.generalStatisticsUseCases.getMostFrequentWorkoutUseCase()
                .collect {
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
            statisticsUseCases.generalStatisticsUseCases.getMostTrainedExerciseUseCase()
                .collect {
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
            statisticsUseCases.generalStatisticsUseCases.getStrongestMuscleGroupUseCase()
                .collect {
                    _getStrongestMuscleGroupState.value = it
                }
        }
    }

    override fun clearError() {
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