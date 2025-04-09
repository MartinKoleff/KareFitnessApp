package com.koleff.kare_android.ui.view_model

import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.internal.Logger
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.common.navigation.NavigationController
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.usecases.WorkoutUseCases
import com.koleff.kare_android.domain.usecases.statistics.StatisticsUseCases
import com.koleff.kare_android.ui.event.OnSearchWorkoutEvent
import com.koleff.kare_android.ui.state.GeneralStatisticsState
import com.koleff.kare_android.ui.state.SearchState
import com.koleff.kare_android.ui.state.TotalRepsPerformedState
import com.koleff.kare_android.ui.state.TotalSetsPerformedState
import com.koleff.kare_android.ui.state.TotalTimesCompletedState
import com.koleff.kare_android.ui.state.TotalWeightLiftedState
import com.koleff.kare_android.ui.state.WorkoutListState
import com.koleff.kare_android.ui.state.WorkoutStatisticsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class WorkoutStatisticsViewModel @Inject constructor(
    private val statisticsUseCases: StatisticsUseCases,
    private val workoutUseCases: WorkoutUseCases,
    private val navigationController: NavigationController,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseViewModel(navigationController = navigationController), StatisticsNavigation {

    private var _state: MutableStateFlow<WorkoutStatisticsState> =
        MutableStateFlow(WorkoutStatisticsState())
    val state: StateFlow<WorkoutStatisticsState>
        get() = _state

    private var _selectedWorkout: MutableStateFlow<WorkoutDto> = MutableStateFlow(WorkoutDto())
    val selectedWorkout: StateFlow<WorkoutDto>
        get() = _selectedWorkout

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
    private var searchJob: Job? = null

    fun onToggleSearch() {
        Logger.getLogger().i("onToggleSearch")

        val isSearching = searchState.value.isSearching
        _searchState.value = searchState.value.copy(
            isSearching = false
        )

        searchJob?.cancel()  //Cancel previous search

        val event = OnSearchWorkoutEvent.OnToggleSearch(
            isSearching = _searchState.value.isSearching,
            workouts = originalWorkoutList
        )

        onSearchEvent(event)
    }

    fun onTextChange(searchText: String) {
        Logger.getLogger().i("onTextChange")

        _searchState.value = searchState.value.copy(
            searchText = searchText,
            isSearching = searchText.isNotEmpty()
        )

        val event = OnSearchWorkoutEvent.OnSearchTextChange(
            searchText = _searchState.value.searchText,
            workouts = originalWorkoutList
        )

        onSearchEvent(event)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun onSearchEvent(event: OnSearchWorkoutEvent) {
        Logger.getLogger().i("OnSearchEvent: $event")

        searchJob?.cancel() //Cancel previous search

        searchJob = viewModelScope.launch(dispatcher) {
            workoutUseCases.onSearchWorkoutUseCase(event)
                .debounce(Constants.fakeSmallDelay) //Debounce to avoid rapid search calls
                .distinctUntilChanged() //Ignore repeated searches
                .flatMapLatest { workoutState ->
                    _workoutsState.value = workoutState

                    if (workoutState.workoutList.isNotEmpty()
                        && _searchState.value.isSearching
                        && workoutState.isSuccessful
                    ) {
                        Logger.getLogger().i("Search workout state: Search is happening")
                        _selectedWorkout.value = workoutState.workoutList.first()
                    } else if (!workoutState.isLoading) {
                        Logger.getLogger().i("Search workout state: Reset workout and stats state")
                        _selectedWorkout.value = WorkoutDto() //Reset selected workout
                        resetWorkoutStatistics()
                    }

                    Logger.getLogger().i("Search workout state: $workoutState")
                    _workoutsState
                }
                .collect {} //Collect the latest state
        }
    }


    private fun observeWorkoutStatisticsState() {
        viewModelScope.launch {
            combine(
                _getWorkoutTotalTimesCompletedState,
                _getWorkoutTotalRepsState,
                _getWorkoutTotalSetsState,
                _getWorkoutTotalWeightState
            ) { values ->
                val workoutStats = WorkoutStatisticsState(
                    getWorkoutTotalTimesCompletedState = values[0] as TotalTimesCompletedState,
                    getWorkoutTotalRepsPerformedState = values[1] as TotalRepsPerformedState,
                    getWorkoutTotalSetsPerformedState = values[2] as TotalSetsPerformedState,
                    getWorkoutTotalWeightLiftedState = values[3] as TotalWeightLiftedState,
                    isLoading = values.any { it.isLoading } || workoutsState.value.isLoading,
                    isError = values.any { it.isError },
                    error = values.firstOrNull { it.error != null }?.error ?: KareError.GENERIC
                )

                Logger.getLogger().i("Observer: $workoutStats")
                _state.value = workoutStats
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = GeneralStatisticsState()
            )
        }
    }

    private fun observeSelectedWorkout() {
        viewModelScope.launch {
            selectedWorkout.collect { workout ->
                if (workout != WorkoutDto()) {
                    Logger.getLogger().i("Selected workout: $workout, Get workout statistics called!")
                    getWorkoutStatistics()
                } else {
                    Logger.getLogger().i("Selected workout: $workout, Reset workout statistics called!")
                    resetWorkoutStatistics()
                }
            }
        }
    }

    private fun resetWorkoutStatistics() {
        viewModelScope.launch(dispatcher) {
            _state.value = WorkoutStatisticsState(isLoading = true)
            delay(Constants.fakeDelay)

            _getWorkoutTotalTimesCompletedState.value = TotalTimesCompletedState()
            _getWorkoutTotalRepsState.value = TotalRepsPerformedState()
            _getWorkoutTotalSetsState.value = TotalSetsPerformedState()
            _getWorkoutTotalWeightState.value = TotalWeightLiftedState()

            _state.value = WorkoutStatisticsState(
                getWorkoutTotalTimesCompletedState = _getWorkoutTotalTimesCompletedState.value,
                getWorkoutTotalRepsPerformedState = _getWorkoutTotalRepsState.value,
                getWorkoutTotalSetsPerformedState = _getWorkoutTotalSetsState.value,
                getWorkoutTotalWeightLiftedState = _getWorkoutTotalWeightState.value
            )
        }
    }

    private fun getWorkoutStatistics() {
        if (selectedWorkout.value == WorkoutDto()) return
        getWorkoutTotalTimesCompleted(workoutId = selectedWorkout.value.workoutId)
        getWorkoutTotalWeightLifted(workoutId = selectedWorkout.value.workoutId)

//        getWorkoutTotalRepsPerformed(workoutId = selectedWorkout.workoutId)
//        getWorkoutTotalSetsPerformed(workoutId = selectedWorkout.workoutId)
    }

    init {
        getWorkouts()
        observeWorkoutStatisticsState()
        observeSelectedWorkout()
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

    override fun onScreenChange() {
        _selectedWorkout.value = WorkoutDto() //Goes in observe...
//        resetWorkoutStatistics()
    }
}