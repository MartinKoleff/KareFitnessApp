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
class ExerciseStatisticsViewModel @Inject constructor(
    private val statisticsUseCases: StatisticsUseCases,
    private val exerciseUseCases: ExerciseUseCases,
    private val preferences: Preferences,
    private val navigationController: NavigationController,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseViewModel(navigationController = navigationController){

    private var _state: MutableStateFlow<ExerciseStatisticsState> =
        MutableStateFlow(ExerciseStatisticsState())
    val state: StateFlow<ExerciseStatisticsState>
        get() = _state

    //Exercise states
    private var _getExercisePRState: MutableStateFlow<ExercisePRState> =
        MutableStateFlow(ExercisePRState())
    val getExercisePRState: StateFlow<ExercisePRState>
        get() = _getExercisePRState

    private var _getExercise1RepMaxState: MutableStateFlow<ExercisePRState> =
        MutableStateFlow(ExercisePRState())
    val getExercise1RepMaxState: StateFlow<ExercisePRState>
        get() = _getExercise1RepMaxState

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

    private var _exerciseState: MutableStateFlow<ExerciseListState> =
        MutableStateFlow(ExerciseListState())
    val exerciseState: StateFlow<ExerciseListState>
        get() = _exerciseState

    private fun getExercises() {
        viewModelScope.launch(dispatcher) {
            exerciseUseCases.getCatalogExercisesUseCase(muscleGroupId = MuscleGroup.ALL.muscleGroupId)
                .collect { exerciseState ->
                    _exerciseState.value = exerciseState

                    if (exerciseState.isSuccessful) {
                        originalExerciseList = exerciseState.exerciseList
                    }
                }
        }
    }

    //Search
    private val _searchState: MutableStateFlow<SearchState> = MutableStateFlow(SearchState())
    val searchState: StateFlow<SearchState>
        get() = _searchState
    private var originalExerciseList: List<ExerciseDto> = mutableListOf()

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
            isSearching = _searchState.value.isSearching,
            exercises = originalExerciseList
        )

        onSearchEvent(event)
    }

    private fun onSearchEvent(event: OnSearchExerciseEvent) {
        viewModelScope.launch(dispatcher) {
            exerciseUseCases.onSearchExerciseUseCase(event).collect { exerciseState ->
                _exerciseState.value = exerciseState

                Logger.getLogger().i("Search exercise state: ${exerciseState.exerciseList}")
            }
        }
    }

    private fun observeExerciseStatisticsState() {
        viewModelScope.launch {
            combine(
                _getExercisePRState,
                _getExercise1RepMaxState,
                _getExerciseTotalRepsState,
                _getExerciseTotalSetsState,
                _getExerciseTotalWeightState
            ) { values ->
                val exerciseStats = ExerciseStatisticsState(
                    getExercisePRState = values[0] as ExercisePRState,
                    getExercise1RepMaxState = values[1] as ExercisePRState,
                    getExerciseTotalRepsPerformedState = values[2] as TotalRepsPerformedState,
                    getExerciseTotalSetsPerformedState = values[3] as TotalSetsPerformedState,
                    getExerciseTotalWeightLiftedState = values[4] as TotalWeightLiftedState
                )

                _state.value = exerciseStats
            }
        }
    }

    private fun getExerciseStatistics(selectedExercise: ExerciseDto) {
        getExercisePR(exerciseId = selectedExercise.exerciseId)
        getExercise1RepMax(exerciseId = selectedExercise.exerciseId)
        getExerciseTotalRepsPerformed(exerciseId = selectedExercise.exerciseId)
        getExerciseTotalSetsPerformed(exerciseId = selectedExercise.exerciseId)
        getExerciseTotalWeightLifted(exerciseId = selectedExercise.exerciseId)
    }

    init {
        getExercises()
        observeExerciseStatisticsState()
//        getExerciseStatistics()
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

    fun getExercise1RepMax(exerciseId: Int) {
        viewModelScope.launch(dispatcher) {
            statisticsUseCases.exerciseStatisticsUseCases.getExercise1RepMaxUseCase(exerciseId)
                .collect { getExercise1RepMaxState ->
                    _getExercise1RepMaxState.value = getExercise1RepMaxState
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

    override fun clearError() {
        if (_getExercisePRState.value.isError) {
            _getExercisePRState.value = ExercisePRState()
        }
        if (_getExercise1RepMaxState.value.isError) {
            _getExercise1RepMaxState.value = ExercisePRState()
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
    }
}