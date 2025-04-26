//package com.koleff.kare_android.ui.view_model
//
//import androidx.lifecycle.viewModelScope
//import com.koleff.kare_android.common.di.IoDispatcher
//import com.koleff.kare_android.common.navigation.NavigationController
//import com.koleff.kare_android.common.preferences.Preferences
//import com.koleff.kare_android.data.model.dto.ExerciseDto
//import com.koleff.kare_android.data.model.dto.MuscleGroup
//import com.koleff.kare_android.data.model.dto.StatisticScreenType
//import com.koleff.kare_android.data.model.dto.WorkoutDto
//import com.koleff.kare_android.domain.usecases.ExerciseUseCases
//import com.koleff.kare_android.domain.usecases.WorkoutUseCases
//import com.koleff.kare_android.domain.usecases.statistics.StatisticsUseCases
//import com.koleff.kare_android.ui.event.OnSearchExerciseEvent
//import com.koleff.kare_android.ui.event.OnSearchWorkoutEvent
//import com.koleff.kare_android.ui.state.ExerciseListState
//import com.koleff.kare_android.ui.state.ExercisePRState
//import com.koleff.kare_android.ui.state.ExerciseState
//import com.koleff.kare_android.ui.state.ExerciseStatisticsState
//import com.koleff.kare_android.ui.state.GeneralStatisticsState
//import com.koleff.kare_android.ui.state.MuscleGroupState
//import com.koleff.kare_android.ui.state.SearchState
//import com.koleff.kare_android.ui.state.StatisticsState
//import com.koleff.kare_android.ui.state.StrongestMuscleGroupState
//import com.koleff.kare_android.ui.state.TotalRepsPerformedState
//import com.koleff.kare_android.ui.state.TotalSetsPerformedState
//import com.koleff.kare_android.ui.state.TotalTimesCompletedState
//import com.koleff.kare_android.ui.state.TotalWeightLiftedState
//import com.koleff.kare_android.ui.state.WorkoutListState
//import com.koleff.kare_android.ui.state.WorkoutState
//import com.koleff.kare_android.ui.state.WorkoutStatisticsState
//import com.koleff.kare_android.ui.state.WorkoutStreakState
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.CoroutineDispatcher
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.combine
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//
//@HiltViewModel
//class StatisticsViewModel @Inject constructor(
//    private val statisticsUseCases: StatisticsUseCases,
//    private val workoutUseCases: WorkoutUseCases,
//    private val exerciseUseCases: ExerciseUseCases,
//    private val preferences: Preferences,
//    private val navigationController: NavigationController,
//    @IoDispatcher private val dispatcher: CoroutineDispatcher
//) : BaseViewModel(navigationController = navigationController) {
//
////    fun onScreenChange(screenType: Int) {
////        _selectedScreenState.value = StatisticScreenType.fromId(screenType)
////    }
////
////    //Stats states
////    private var _statisticsState: MutableStateFlow<StatisticsState> =
////        MutableStateFlow(StatisticsState())
////    val statisticsState: StateFlow<StatisticsState>
////        get() = _statisticsState
//
////    get() = when (_selectedScreenState.value) {
////        StatisticScreenType.GENERAL -> _generalStatisticsState
////        StatisticScreenType.WORKOUT -> _workoutStatisticsState
////        StatisticScreenType.EXERCISE -> _exerciseStatisticsState
////    }
////    val _generalStatisticsState: StateFlow<GeneralStatisticsState>
////    val _workoutStatisticsState: StateFlow<WorkoutStatisticsState>
////    val _exerciseStatisticsState: StateFlow<ExerciseStatisticsState>
//
//    //Selected screen
//    private var _selectedScreenState: MutableStateFlow<StatisticScreenType> =
//        MutableStateFlow(StatisticScreenType.GENERAL)
//    val selectedScreenState: StateFlow<StatisticScreenType>
//        get() = _selectedScreenState
//
//    //Exercise states
//    private var _getExercisePRState: MutableStateFlow<ExercisePRState> =
//        MutableStateFlow(ExercisePRState())
//    val getExercisePRState: StateFlow<ExercisePRState>
//        get() = _getExercisePRState
//
//    private var _getExercise1RepMaxState: MutableStateFlow<ExercisePRState> =
//        MutableStateFlow(ExercisePRState())
//    val getExercise1RepMaxState: StateFlow<ExercisePRState>
//        get() = _getExercise1RepMaxState
//
//    private var _getExerciseTotalRepsState: MutableStateFlow<TotalRepsPerformedState> =
//        MutableStateFlow(TotalRepsPerformedState())
//    val getExerciseTotalRepsState: StateFlow<TotalRepsPerformedState>
//        get() = _getExerciseTotalRepsState
//
//    private var _getExerciseTotalSetsState: MutableStateFlow<TotalSetsPerformedState> =
//        MutableStateFlow(TotalSetsPerformedState())
//    val getExerciseTotalSetsState: StateFlow<TotalSetsPerformedState>
//        get() = _getExerciseTotalSetsState
//
//    private var _getExerciseTotalWeightState: MutableStateFlow<TotalWeightLiftedState> =
//        MutableStateFlow(TotalWeightLiftedState())
//    val getExerciseTotalWeightState: StateFlow<TotalWeightLiftedState>
//        get() = _getExerciseTotalWeightState
//
//    //Workout states
//    private var _getWorkoutTotalTimesCompletedState: MutableStateFlow<TotalTimesCompletedState> =
//        MutableStateFlow(TotalTimesCompletedState())
//    val getWorkoutTotalTimesCompletedState: StateFlow<TotalTimesCompletedState>
//        get() = _getWorkoutTotalTimesCompletedState
//
//    private var _getWorkoutTotalRepsState: MutableStateFlow<TotalRepsPerformedState> =
//        MutableStateFlow(TotalRepsPerformedState())
//    val getWorkoutTotalRepsState: StateFlow<TotalRepsPerformedState>
//        get() = _getWorkoutTotalRepsState
//
//    private var _getWorkoutTotalSetsState: MutableStateFlow<TotalSetsPerformedState> =
//        MutableStateFlow(TotalSetsPerformedState())
//    val getWorkoutTotalSetsState: StateFlow<TotalSetsPerformedState>
//        get() = _getWorkoutTotalSetsState
//
//    private var _getWorkoutTotalWeightState: MutableStateFlow<TotalWeightLiftedState> =
//        MutableStateFlow(TotalWeightLiftedState())
//    val getWorkoutTotalWeightState: StateFlow<TotalWeightLiftedState>
//        get() = _getWorkoutTotalWeightState
//
//    //General states
//    private var _getWorkoutsCompletedState: MutableStateFlow<TotalTimesCompletedState> =
//        MutableStateFlow(TotalTimesCompletedState())
//    val getWorkoutsCompletedState: StateFlow<TotalTimesCompletedState>
//        get() = _getWorkoutsCompletedState
//
//    private var _getDistinctWorkoutsCompletedState: MutableStateFlow<TotalTimesCompletedState> =
//        MutableStateFlow(TotalTimesCompletedState())
//    val getDistinctWorkoutsCompletedState: StateFlow<TotalTimesCompletedState>
//        get() = _getDistinctWorkoutsCompletedState
//
//    private var _getWorkoutStreakState: MutableStateFlow<WorkoutStreakState> =
//        MutableStateFlow(WorkoutStreakState())
//    val getWorkoutStreakState: StateFlow<WorkoutStreakState>
//        get() = _getWorkoutStreakState
//
//    private var _getMostFrequentWorkoutState: MutableStateFlow<WorkoutState> =
//        MutableStateFlow(WorkoutState())
//    val getMostFrequentWorkoutState: StateFlow<WorkoutState>
//        get() = _getMostFrequentWorkoutState
//
//    private var _getMostTrainedMuscleGroupState: MutableStateFlow<MuscleGroupState> =
//        MutableStateFlow(MuscleGroupState())
//    val getMostTrainedMuscleGroupState: StateFlow<MuscleGroupState>
//        get() = _getMostTrainedMuscleGroupState
//
//    private var _getMostTrainedExerciseState: MutableStateFlow<ExerciseState> =
//        MutableStateFlow(ExerciseState())
//    val getMostTrainedExerciseState: StateFlow<ExerciseState>
//        get() = _getMostTrainedExerciseState
//
//    private var _getTotalWeightLiftedState: MutableStateFlow<TotalWeightLiftedState> =
//        MutableStateFlow(TotalWeightLiftedState())
//    val getTotalWeightLiftedState: StateFlow<TotalWeightLiftedState>
//        get() = _getTotalWeightLiftedState
//
//    private var _getStrongestMuscleGroupState: MutableStateFlow<StrongestMuscleGroupState> =
//        MutableStateFlow(StrongestMuscleGroupState())
//    val getStrongestMuscleGroupState: StateFlow<StrongestMuscleGroupState>
//        get() = _getStrongestMuscleGroupState
//
//    private fun observeStatisticsState() {
//        viewModelScope.launch {
//            combine(
//                //General stats
//                _getWorkoutsCompletedState,
//                _getDistinctWorkoutsCompletedState,
//                _getWorkoutStreakState,
//                _getMostFrequentWorkoutState,
//                _getMostTrainedMuscleGroupState,
//                _getMostTrainedExerciseState,
//                _getTotalWeightLiftedState,
//                _getStrongestMuscleGroupState,
//
//                //Workout stats
//                _getWorkoutTotalTimesCompletedState,
//                _getWorkoutTotalRepsState,
//                _getWorkoutTotalSetsState,
//                _getWorkoutTotalWeightState,
//
//                //Exercise stats
//                _getExercisePRState,
//                _getExercise1RepMaxState,
//                _getExerciseTotalRepsState,
//                _getExerciseTotalSetsState,
//                _getExerciseTotalWeightState
//            ) { values ->
//                val generalStats = GeneralStatisticsState(
//                    getWorkoutsCompletedState = values[0] as TotalTimesCompletedState,
//                    getDistinctWorkoutsCompletedState = values[1] as TotalTimesCompletedState,
//                    getWorkoutStreakUseCase = values[2] as WorkoutStreakState,
//                    getMostFrequentWorkoutState = values[3] as WorkoutState,
//                    getMostTrainedMuscleGroupState = values[4] as MuscleGroupState,
//                    getMostTrainedExerciseState = values[5] as ExerciseState,
//                    getTotalWeightLiftedState = values[6] as TotalWeightLiftedState,
//                    getStrongestMuscleGroupState = values[7] as StrongestMuscleGroupState
//                )
//
//                val workoutStats = WorkoutStatisticsState(
//                    getWorkoutTotalTimesCompletedState = values[8] as TotalTimesCompletedState,
//                    getWorkoutTotalRepsPerformedState = values[9] as TotalRepsPerformedState,
//                    getWorkoutTotalSetsPerformedState = values[10] as TotalSetsPerformedState,
//                    getWorkoutTotalWeightLiftedState = values[11] as TotalWeightLiftedState
//                )
//
//                val exerciseStats = ExerciseStatisticsState(
//                    getExercisePRState = values[12] as ExercisePRState,
//                    getExercise1RepMaxState = values[13] as ExercisePRState,
//                    getExerciseTotalRepsPerformedState = values[14] as TotalRepsPerformedState,
//                    getExerciseTotalSetsPerformedState = values[15] as TotalSetsPerformedState,
//                    getExerciseTotalWeightLiftedState = values[16] as TotalWeightLiftedState
//                )
//
//                _statisticsState.value = StatisticsState(
//                    generalStatisticsState = generalStats,
//                    workoutStatisticsState = workoutStats,
//                    exerciseStatisticsState = exerciseStats
//                )
//            }
//        }
//    }
//
//    init {
//        observeStatisticsState()
//        getGeneralStatistics()
//        getWorkouts()
//        getExercises()
//    }
//
//    fun refreshStatisticsState() {
//        observeStatisticsState()
//    }
//
//    private fun getGeneralStatistics() {
//        getWorkoutsCompleted()
//        getDistinctWorkoutsCompleted()
//        getWorkoutStreak()
//        getMostFrequentWorkout()
//        getMostTrainedMuscleGroup()
//        getMostTrainedExercise()
//        getTotalWeightLifted()
//        getStrongestMuscleGroup()
//    }
//
//    private fun getExerciseStatistics(selectedExercise: ExerciseDto) {
//        getExercisePR(exerciseId = selectedExercise.exerciseId)
//        getExercise1RepMax(exerciseId = selectedExercise.exerciseId)
//        getExerciseTotalRepsPerformed(exerciseId = selectedExercise.exerciseId)
//        getExerciseTotalSetsPerformed(exerciseId = selectedExercise.exerciseId)
//        getExerciseTotalWeightLifted(exerciseId = selectedExercise.exerciseId)
//    }
//
//    private fun getWorkoutStatistics(selectedWorkout: WorkoutDto) {
//        getWorkoutTotalTimesCompleted(workoutId = selectedWorkout.workoutId)
////        getWorkoutTotalRepsPerformed(workoutId = selectedWorkout.workoutId)
////        getWorkoutTotalSetsPerformed(workoutId = selectedWorkout.workoutId)
//        getWorkoutTotalWeightLifted(workoutId = selectedWorkout.workoutId)
//    }
//
////    private var _catalogWorkoutsState: MutableStateFlow<ExerciseListState> =
////        MutableStateFlow(ExerciseListState())
////    val catalogWorkoutsState: StateFlow<ExerciseListState>
////        get() = _catalogWorkoutsState
////
////    private fun getCatalogExercises() {
////        viewModelScope.launch(dispatcher) {
////            exerciseUseCases.getCatalogExercisesUseCase(muscleGroupId = MuscleGroup.ALL.muscleGroupId)
////                .collect { exerciseState ->
////                    _catalogWorkoutsState.value = exerciseState
////                }
////        }
////    }
////
//
//    private var _workoutsState: MutableStateFlow<WorkoutListState> =
//        MutableStateFlow(WorkoutListState())
//    val workoutsState: StateFlow<WorkoutListState>
//        get() = _workoutsState
//
//    private fun getWorkouts() {
//        viewModelScope.launch(dispatcher) {
//            workoutUseCases.getAllWorkoutsUseCase().collect { workoutState ->
//                _workoutsState.value = workoutState
//
//                if(workoutState.isSuccessful){
//                    originalWorkoutList = workoutState.workoutList
//                }
//            }
//        }
//    }
//
//    private var _exerciseState: MutableStateFlow<ExerciseListState> = MutableStateFlow(ExerciseListState())
//    val exerciseState: StateFlow<ExerciseListState>
//        get() = _exerciseState
//
//    private fun getExercises() {
//        viewModelScope.launch(dispatcher) {
//            exerciseUseCases.getCatalogExercisesUseCase(muscleGroupId = MuscleGroup.ALL.muscleGroupId)
//                .collect { exerciseState ->
//                    _exerciseState.value = exerciseState
//
//                    if (exerciseState.isSuccessful) {
//                        originalExerciseList = exerciseState.exerciseList
//                    }
//                }
//        }
//    }
//
//    //Search
//    private val _searchState: MutableStateFlow<SearchState> = MutableStateFlow(SearchState())
//    val searchState: StateFlow<SearchState>
//        get() = _searchState
//    private var originalWorkoutList: List<WorkoutDto> = mutableListOf()
//    private var originalExerciseList: List<ExerciseDto> = mutableListOf()
//
//    fun onTextChange(searchText: String, isWorkout: Boolean) {
//        _searchState.value = searchState.value.copy(
//            searchText = searchText
//        )
//
//        if(isWorkout){
//            val event =  OnSearchWorkoutEvent.OnSearchTextChange(
//                searchText = _searchState.value.searchText,
//                workouts = originalWorkoutList
//            )
//
//            onSearchEvent(event)
//        }else{
//            val event =  OnSearchExerciseEvent.OnSearchTextChange(
//                searchText = _searchState.value.searchText,
//                exercises = originalExerciseList
//            )
//
//            onSearchEvent(event)
//        }
//    }
//
//    fun onToggleSearch(isWorkout: Boolean) {
//        val isSearching = searchState.value.isSearching
//        _searchState.value = searchState.value.copy(
//            isSearching = !isSearching
//        )
//
//        if(isWorkout){
//            val event =  OnSearchWorkoutEvent.OnToggleSearch(
//                isSearching = _searchState.value.isSearching,
//                workouts = originalWorkoutList
//            )
//
//            onSearchEvent(event)
//        }else{
//            val event =  OnSearchExerciseEvent.OnToggleSearch(
//                isSearching = _searchState.value.isSearching,
//                exercises = originalExerciseList
//            )
//
//            onSearchEvent(event)
//        }
//    }
//
//    private fun onSearchEvent(event: OnSearchWorkoutEvent) {
//        viewModelScope.launch(dispatcher) {
//            workoutUseCases.onSearchWorkoutUseCase(event).collect { workoutState ->
//                _workoutsState.value = workoutState
//            }
//        }
//    }
//
//    private fun onSearchEvent(event: OnSearchExerciseEvent) {
//        viewModelScope.launch(dispatcher) {
//            exerciseUseCases.onSearchExerciseUseCase(event).collect { exerciseState ->
//                _exerciseState.value = exerciseState
//            }
//        }
//    }
//
//    //Exercise stats
//    fun getExercisePR(exerciseId: Int) {
//        viewModelScope.launch(dispatcher) {
//            statisticsUseCases.exerciseStatisticsUseCases.getExercisePRUseCase(exerciseId)
//                .collect { getExercisePRState ->
//                    _getExercisePRState.value = getExercisePRState
//                }
//        }
//    }
//
//    fun getExercise1RepMax(exerciseId: Int) {
//        viewModelScope.launch(dispatcher) {
//            statisticsUseCases.exerciseStatisticsUseCases.getExercise1RepMaxUseCase(exerciseId)
//                .collect { getExercise1RepMaxState ->
//                    _getExercise1RepMaxState.value = getExercise1RepMaxState
//                }
//        }
//    }
//
//    fun getExerciseTotalRepsPerformed(exerciseId: Int) {
//        viewModelScope.launch(dispatcher) {
//            statisticsUseCases.exerciseStatisticsUseCases.getExerciseTotalRepsPerformedUseCase(
//                exerciseId
//            ).collect { getExerciseTotalRepsState ->
//                _getExerciseTotalRepsState.value = getExerciseTotalRepsState
//            }
//        }
//    }
//
//    fun getExerciseTotalSetsPerformed(exerciseId: Int) {
//        viewModelScope.launch(dispatcher) {
//            statisticsUseCases.exerciseStatisticsUseCases.getExerciseTotalSetsPerformedUseCase(
//                exerciseId
//            ).collect { getExerciseTotalSetsState ->
//                _getExerciseTotalSetsState.value = getExerciseTotalSetsState
//            }
//        }
//    }
//
//    fun getExerciseTotalWeightLifted(exerciseId: Int) {
//        viewModelScope.launch(dispatcher) {
//            statisticsUseCases.exerciseStatisticsUseCases.getExerciseTotalWeightLiftedUseCase(
//                exerciseId
//            ).collect { getExerciseTotalWeightState ->
//                _getExerciseTotalWeightState.value = getExerciseTotalWeightState
//            }
//        }
//    }
//
//    //Workout stats
//    fun getWorkoutTotalTimesCompleted(workoutId: Int) {
//        viewModelScope.launch(dispatcher) {
//            statisticsUseCases.workoutStatisticsUseCases.getWorkoutTotalTimesCompletedUseCase(
//                workoutId
//            ).collect { getWorkoutTotalTimesCompletedState ->
//                _getWorkoutTotalTimesCompletedState.value = getWorkoutTotalTimesCompletedState
//            }
//        }
//    }
//
//    fun getWorkoutTotalRepsPerformed(workoutId: Int, exerciseId: Int) {
//        viewModelScope.launch(dispatcher) {
//            statisticsUseCases.workoutStatisticsUseCases.getWorkoutTotalRepsPerformedUseCase(
//                workoutId,
//                exerciseId
//            ).collect { getWorkoutTotalRepsState ->
//                _getWorkoutTotalRepsState.value = getWorkoutTotalRepsState
//            }
//        }
//    }
//
//    fun getWorkoutTotalSetsPerformed(workoutId: Int, exerciseId: Int) {
//        viewModelScope.launch(dispatcher) {
//            statisticsUseCases.workoutStatisticsUseCases.getWorkoutTotalSetsPerformedUseCase(
//                workoutId,
//                exerciseId
//            ).collect { getWorkoutTotalSetsState ->
//                _getWorkoutTotalSetsState.value = getWorkoutTotalSetsState
//            }
//        }
//    }
//
//    fun getWorkoutTotalWeightLifted(workoutId: Int) {
//        viewModelScope.launch(dispatcher) {
//            statisticsUseCases.workoutStatisticsUseCases.getWorkoutTotalWeightLiftedUseCase(
//                workoutId
//            ).collect { getWorkoutTotalWeightState ->
//                _getWorkoutTotalWeightState.value = getWorkoutTotalWeightState
//            }
//        }
//    }
//
//    //General stats
//    fun getWorkoutsCompleted() {
//        viewModelScope.launch(dispatcher) {
//            statisticsUseCases.generalStatisticsUseCases.getWorkoutsCompletedUseCase().collect {
//                _getWorkoutsCompletedState.value = it
//            }
//        }
//    }
//
//    fun getDistinctWorkoutsCompleted() {
//        viewModelScope.launch(dispatcher) {
//            statisticsUseCases.generalStatisticsUseCases.getDistinctWorkoutsCompletedUseCase()
//                .collect {
//                    _getDistinctWorkoutsCompletedState.value = it
//                }
//        }
//    }
//
//    fun getWorkoutStreak() {
//        viewModelScope.launch(dispatcher) {
//            statisticsUseCases.generalStatisticsUseCases.getWorkoutStreakUseCase().collect {
//                _getWorkoutStreakState.value = it
//            }
//        }
//    }
//
//    fun getMostFrequentWorkout() {
//        viewModelScope.launch(dispatcher) {
//            statisticsUseCases.generalStatisticsUseCases.getMostFrequentWorkoutUseCase()
//                .collect {
//                    _getMostFrequentWorkoutState.value = it
//                }
//        }
//    }
//
//    fun getMostTrainedMuscleGroup() {
//        viewModelScope.launch(dispatcher) {
//            statisticsUseCases.generalStatisticsUseCases.getMostTrainedMuscleGroupUseCase()
//                .collect {
//                    _getMostTrainedMuscleGroupState.value = it
//                }
//        }
//    }
//
//    fun getMostTrainedExercise() {
//        viewModelScope.launch(dispatcher) {
//            statisticsUseCases.generalStatisticsUseCases.getMostTrainedExerciseUseCase()
//                .collect {
//                    _getMostTrainedExerciseState.value = it
//                }
//        }
//    }
//
//    fun getTotalWeightLifted() {
//        viewModelScope.launch(dispatcher) {
//            statisticsUseCases.generalStatisticsUseCases.getTotalWeightLiftedUseCase().collect {
//                _getTotalWeightLiftedState.value = it
//            }
//        }
//    }
//
//    fun getStrongestMuscleGroup() {
//        viewModelScope.launch(dispatcher) {
//            statisticsUseCases.generalStatisticsUseCases.getStrongestMuscleGroupUseCase()
//                .collect {
//                    _getStrongestMuscleGroupState.value = it
//                }
//        }
//    }
//
//    override fun clearError() {
//        if (_getExercisePRState.value.isError) {
//            _getExercisePRState.value = ExercisePRState()
//        }
//        if (_getExercise1RepMaxState.value.isError) {
//            _getExercise1RepMaxState.value = ExercisePRState()
//        }
//        if (_getExerciseTotalRepsState.value.isError) {
//            _getExerciseTotalRepsState.value = TotalRepsPerformedState()
//        }
//        if (_getExerciseTotalSetsState.value.isError) {
//            _getExerciseTotalSetsState.value = TotalSetsPerformedState()
//        }
//        if (_getExerciseTotalWeightState.value.isError) {
//            _getExerciseTotalWeightState.value = TotalWeightLiftedState()
//        }
//        if (_getWorkoutTotalTimesCompletedState.value.isError) {
//            _getWorkoutTotalTimesCompletedState.value = TotalTimesCompletedState()
//        }
//        if (_getWorkoutTotalRepsState.value.isError) {
//            _getWorkoutTotalRepsState.value = TotalRepsPerformedState()
//        }
//        if (_getWorkoutTotalSetsState.value.isError) {
//            _getWorkoutTotalSetsState.value = TotalSetsPerformedState()
//        }
//        if (_getWorkoutTotalWeightState.value.isError) {
//            _getWorkoutTotalWeightState.value = TotalWeightLiftedState()
//        }
//        if (_getWorkoutsCompletedState.value.isError) {
//            _getWorkoutsCompletedState.value = TotalTimesCompletedState()
//        }
//        if (_getDistinctWorkoutsCompletedState.value.isError) {
//            _getDistinctWorkoutsCompletedState.value = TotalTimesCompletedState()
//        }
//        if (_getWorkoutStreakState.value.isError) {
//            _getWorkoutStreakState.value = WorkoutStreakState()
//        }
//        if (_getMostFrequentWorkoutState.value.isError) {
//            _getMostFrequentWorkoutState.value = WorkoutState()
//        }
//        if (_getMostTrainedMuscleGroupState.value.isError) {
//            _getMostTrainedMuscleGroupState.value = MuscleGroupState()
//        }
//        if (_getMostTrainedExerciseState.value.isError) {
//            _getMostTrainedExerciseState.value = ExerciseState()
//        }
//        if (_getTotalWeightLiftedState.value.isError) {
//            _getTotalWeightLiftedState.value = TotalWeightLiftedState()
//        }
//        if (_getStrongestMuscleGroupState.value.isError) {
//            _getStrongestMuscleGroupState.value = StrongestMuscleGroupState()
//        }
//    }
//}