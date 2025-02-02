package com.koleff.kare_android.ui.view_model

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.common.navigation.Destination
import com.koleff.kare_android.common.navigation.NavigationController
import com.koleff.kare_android.common.navigation.NavigationEvent
import com.koleff.kare_android.common.timer.TimerUtil
import com.koleff.kare_android.data.model.dto.DoWorkoutExerciseSetDto
import com.koleff.kare_android.data.model.dto.DoWorkoutPerformanceMetricsDto
import com.koleff.kare_android.data.model.dto.ExerciseProgressDto
import com.koleff.kare_android.data.model.dto.ExerciseSetProgressDto
import com.koleff.kare_android.domain.usecases.DoWorkoutPerformanceMetricsUseCases
import com.koleff.kare_android.domain.usecases.DoWorkoutUseCases
import com.koleff.kare_android.domain.usecases.WorkoutUseCases
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.ui.state.BaseState
import com.koleff.kare_android.ui.state.DoWorkoutPerformanceMetricsState
import com.koleff.kare_android.ui.state.DoWorkoutState
import com.koleff.kare_android.ui.state.TimerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class DoWorkoutViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val navigationController: NavigationController,
    private val workoutUseCases: WorkoutUseCases,
    private val doWorkoutUseCases: DoWorkoutUseCases,
    private val doWorkoutPerformanceMetricsUseCases: DoWorkoutPerformanceMetricsUseCases,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) :
    BaseViewModel(navigationController) {
    private val workoutId: Int = savedStateHandle.get<String>("workout_id")?.toIntOrNull() ?: -1

    private val _state: MutableStateFlow<DoWorkoutState> =
        MutableStateFlow(DoWorkoutState(isLoading = true))

    val state: StateFlow<DoWorkoutState>
        get() = _state

    private val workoutTimer: TimerUtil
    private val countdownTimer: TimerUtil

    private val _workoutTimerState: MutableStateFlow<TimerState> =
        MutableStateFlow(TimerState(time = state.value.doWorkoutData.defaultExerciseTime))
    val workoutTimerState: StateFlow<TimerState>
        get() = _workoutTimerState

    private val _countdownTimerState: MutableStateFlow<TimerState> =
        MutableStateFlow(TimerState(time = state.value.doWorkoutData.countdownTime))
    val countdownTimerState: StateFlow<TimerState>
        get() = _countdownTimerState

    private val isLogging: Boolean = false
    private var isCountdownScreen = false

    //List of all do workout exercise sets filled during the workout completion...
    private val doWorkoutExerciseSets: MutableList<DoWorkoutExerciseSetDto> = mutableListOf()

    private val _saveDoWorkoutExerciseSetsState: MutableStateFlow<BaseState> =
        MutableStateFlow(BaseState())
    val saveDoWorkoutExerciseSetsState: StateFlow<BaseState>
        get() = _saveDoWorkoutExerciseSetsState

    private val _saveDoWorkoutPerformanceMetricsState: MutableStateFlow<DoWorkoutPerformanceMetricsState> =
        MutableStateFlow(DoWorkoutPerformanceMetricsState())
    val saveDoWorkoutPerformanceMetricsState: StateFlow<DoWorkoutPerformanceMetricsState>
        get() = _saveDoWorkoutPerformanceMetricsState

    private val _playerState: MutableStateFlow<BaseState> = MutableStateFlow(BaseState())
    val playerState: StateFlow<BaseState>
        get() = _playerState

    override fun clearError() {
        if (state.value.isError) {
            _state.value = DoWorkoutState()
        }
    }

    init {
        with(_state.value.doWorkoutData) {
            workoutTimer = TimerUtil(defaultExerciseTime.toSeconds())
            countdownTimer = TimerUtil(countdownTime.toSeconds())
        }

        setup()

        //Log timer states
        viewModelScope.launch(Dispatchers.Default) {
            while (isLogging) {
                Log.d("DoWorkoutViewModel", "----------------Timers------------------")
                Log.d("DoWorkoutViewModel", "Countdown time: ${countdownTimerState.value.time}")
                Log.d("DoWorkoutViewModel", "Workout time: ${workoutTimerState.value.time}")
                Log.d("DoWorkoutViewModel", "Workout timer is running: ${workoutTimer.isRunning()}")
                Log.d("DoWorkoutViewModel", "Countdown timer is running: ${countdownTimer.isRunning()}")
                delay(1000)
            }
        }
    }

    private fun setup() {
        viewModelScope.launch(dispatcher) {

            //Fetch workout
            workoutUseCases.getWorkoutDetailsUseCase(workoutId).collect { result ->
                if (result.isSuccessful) {
                    val selectedWorkoutDetails = result.workoutDetails
                    Log.d("DoWorkoutViewModel", "Fetched workout: $selectedWorkoutDetails")

                    //Initial setup...
                    doWorkoutUseCases.doWorkoutInitialSetupUseCase.invoke(selectedWorkoutDetails)
                        .collect { setupResult ->
                            _state.value = setupResult

                            //Start workout timer
                            if (setupResult.isSuccessful) {

                                //Create do workout performance metrics
                                createDoWorkoutPerformanceMetrics()

                                startWorkoutTimer(isInitialCall = true)
                            }
                        }
                } else if (result.isError) {
                    _state.value = DoWorkoutState(
                        isError = true,
                        error = result.error
                    )
                }
            }
        }
    }

    private fun createDoWorkoutPerformanceMetrics() = with(state.value.doWorkoutData) {
        Log.d(
            "DoWorkoutViewModel",
            "Creating do workout performance metrics for workout with id ${workout.workoutId}."
        )

        val performanceMetrics = DoWorkoutPerformanceMetricsDto(
            id = 0, //Auto-generate
            workoutId = workout.workoutId,
            date = Date(), //Current date of starting the workout
            doWorkoutExerciseSets = emptyList() //Will be filled as the workout continues...
        )

        viewModelScope.launch(dispatcher) {
            doWorkoutPerformanceMetricsUseCases.saveDoWorkoutPerformanceMetricsUseCase(
                performanceMetrics
            ).collect { result ->
                _saveDoWorkoutPerformanceMetricsState.value = result

                if (result.isSuccessful) {
                    Log.d(
                        "DoWorkoutViewModel",
                        "Do workout performance metrics with id ${result.doWorkoutPerformanceMetrics.id} for workout with id ${workout.workoutId} created!" +
                                "\nDo workout performance metrics: ${result.doWorkoutPerformanceMetrics}"
                    )
                }
            }
        }
    }

    private fun selectNextExercise() {
        Log.d("DoWorkoutViewModel", "Select next exercise requested.")

        //Workout completed
        if (_state.value.doWorkoutData.isWorkoutCompleted || _state.value.doWorkoutData.nextSetNumber == -1) { //No next set...
            val updatedData = _state.value.doWorkoutData.copy(isWorkoutCompleted = true)
            _state.value = _state.value.copy(doWorkoutData = updatedData)
            hideNextExerciseCountdownScreen()

            //Save do workout performance metrics
            saveDoWorkoutExerciseSets()

            //Stop timers...
            workoutTimer.resetTimer()
            countdownTimer.resetTimer()
        } else {
            showNextExerciseCountdownScreen()
            startCountdownTimer()
        }
    }


    //If called from the button update current set and next set directly...
    fun skipNextExercise() {
        viewModelScope.launch(dispatcher) {
            doWorkoutUseCases.updateExerciseSetsAfterTimerUseCase(_state.value.doWorkoutData)
                .collect { result ->
                    _state.value = result

                    if (result.isSuccessful) {
                        selectNextExercise()
                    }
                }
        }
    }

    private fun updateExerciseSetsAfterTimer() {
        viewModelScope.launch(dispatcher) {
            doWorkoutUseCases.updateExerciseSetsAfterTimerUseCase(_state.value.doWorkoutData)
                .collect { result ->
                    _state.value = result
                }
        }
    }

    //Used when next exercise is selected and NextExerciseCountdownScreen is still showing
    private fun hideNextExerciseCountdownScreen() {
        val updatedData = _state.value.doWorkoutData.copy(isBetweenExerciseCountdown = false)
        _state.value = _state.value.copy(doWorkoutData = updatedData)
        isCountdownScreen = false
    }

    private fun showNextExerciseCountdownScreen() {
        val updatedData = _state.value.doWorkoutData.copy(isBetweenExerciseCountdown = true)
        _state.value = _state.value.copy(doWorkoutData = updatedData)
        isCountdownScreen = true
    }

    private fun startCountdownTimer() = with(state.value.doWorkoutData) {

        //Show next exercise countdown screen
        showNextExerciseCountdownScreen()

        viewModelScope.launch(dispatcher) {

            //Reset workout timer
            doWorkoutUseCases.resetTimerUseCase(
                timer = workoutTimer,
                defaultTime = defaultExerciseTime
            ).collect { result ->
                when (result) {
                    is ResultWrapper.Success -> {
                        _workoutTimerState.value =
                            _workoutTimerState.value.copy(time = result.data.time)
                    }

                    else -> {}
                }
            }

            //Start countdown timer
            doWorkoutUseCases.startTimerUseCase(
                timer = countdownTimer,
                time = countdownTime
            ).collect { result ->
                when (result) {
                    is ResultWrapper.Success -> {
                        _countdownTimerState.value =
                            _countdownTimerState.value.copy(time = result.data.time)

                        //Timer has finished
                        if (countdownTimerState.value.time.hasFinished()) {
                            Log.d(
                                "DoWorkoutViewModel",
                                "Countdown finished! Selecting next exercise..."
                            )
                            startWorkoutTimer()
                        }
                    }

                    else -> {}
                }
            }
        }
    }

    private fun startWorkoutTimer(
        isInitialCall: Boolean = false
    ) = with(state.value.doWorkoutData) {

        viewModelScope.launch(dispatcher) {

            //Reset countdown timer
            doWorkoutUseCases.resetTimerUseCase(
                timer = countdownTimer,
                defaultTime = countdownTime
            ).collect { result ->
                when (result) {
                    is ResultWrapper.Success -> {
                        _countdownTimerState.value =
                            _countdownTimerState.value.copy(time = result.data.time)
                    }

                    else -> {}
                }
            }

            //Update current and next exercise sets after countdown timer has finished.
            if (!isInitialCall) {
                updateExerciseSetsAfterTimer()
            }

            //Start workout timer
            doWorkoutUseCases.startTimerUseCase(
                timer = workoutTimer,
                time = defaultExerciseTime
            ).collect { result ->
                when (result) {
                    is ResultWrapper.Success -> {
                        _workoutTimerState.value =
                            _workoutTimerState.value.copy(time = result.data.time)

                        //Hide next exercise countdown screen
                        if (isCountdownScreen || isInitialCall) {
                            hideNextExerciseCountdownScreen()
                        }

                        //Timer has finished
                        if (workoutTimerState.value.time.hasFinished()) {
                            Log.d(
                                "DoWorkoutViewModel",
                                "Exercise timer finished! Starting countdown timer for next exercise."
                            )
                            selectNextExercise()
                        }
                    }

                    else -> {}
                }
            }
        }
    }


     fun resumeWorkoutTimer() = with(state.value.doWorkoutData) {
        viewModelScope.launch(dispatcher) {

            //Start workout timer
            doWorkoutUseCases.resumeTimerUseCase(
                timer = workoutTimer,
                time = defaultExerciseTime
            ).collect { result ->
                when (result) {
                    is ResultWrapper.Success -> {
                        _workoutTimerState.value =
                            _workoutTimerState.value.copy(time = result.data.time)

                        //Hide next exercise countdown screen
                        if (isCountdownScreen) {
                            hideNextExerciseCountdownScreen()
                        }

                        //Timer has finished
                        if (workoutTimerState.value.time.hasFinished()) {
                            Log.d(
                                "DoWorkoutViewModel",
                                "Exercise timer finished! Starting countdown timer for next exercise."
                            )
                            selectNextExercise()
                        }
                    }

                    else -> {}
                }
            }
        }
    }



    fun pauseWorkoutTimer() {
        viewModelScope.launch(dispatcher) {

            //Start workout timer
            doWorkoutUseCases.pauseTimerUseCase(
                timer = workoutTimer
            ).collect { result ->
                when (result) {
                    is ResultWrapper.Success -> {
                        Log.d(
                            "DoWorkoutViewModel",
                            "Workout timer has paused."
                        )
                    }

                    else -> {}
                }
            }
        }
    }

    fun onScreenClick(){
        if(workoutTimer.isRunning()){
            Log.d("DoWorkoutScreen", "Workout timer paused...")
            pauseWorkoutTimer()
        }else{
            Log.d("DoWorkoutScreen", "Workout timer resumed...")
            resumeWorkoutTimer()

        }
    }

    //Every exercise change -> add current do workout performance metrics to list
    //On workout finish -> save
    //On workout exited -> delete

    private fun saveDoWorkoutExerciseSets() {
        Log.d(
            "DoWorkoutViewModel",
            "Saving all DoWorkoutExerciseSets. All sets for do workout performance metrics: $doWorkoutExerciseSets"
        )

        viewModelScope.launch(dispatcher) {
            doWorkoutPerformanceMetricsUseCases.saveAllDoWorkoutExerciseSetUseCase(
                doWorkoutExerciseSets
            ).collect { result ->
                _saveDoWorkoutExerciseSetsState.value = result

                if (result.isSuccessful) {
                    Log.d(
                        "DoWorkoutViewModel",
                        "Saving all DoWorkoutExerciseSets to DB successful!"
                    )
                }
            }
        }
    }

    fun addDoWorkoutExerciseSet(exerciseData: ExerciseProgressDto) {
        Log.d(
            "DoWorkoutViewModel",
            "Adding DoWorkoutExerciseSets to DB for exercise with id: ${exerciseData.exerciseId}"
        )
        Log.d("DoWorkoutViewModel", "DoWorkoutExerciseSets before parsing: ${exerciseData.sets}")

        //Parse to DoWorkoutExerciseSets
        val exerciseSets = parseExerciseSets(exerciseData.sets)
        Log.d("DoWorkoutViewModel", "DoWorkoutExerciseSets after parsing: $exerciseSets")

        //Add to list
        doWorkoutExerciseSets.addAll(exerciseSets)
    }

    private fun parseExerciseSets(setsWithProgress: List<ExerciseSetProgressDto>): List<DoWorkoutExerciseSetDto> {
        val doWorkoutExerciseSets = setsWithProgress.map { setWithProgress ->
            DoWorkoutExerciseSetDto(
                templateSetId = setWithProgress.baseSet.setId
                    ?: throw IllegalArgumentException("Template set id should not be null"),
                workoutPerformanceMetricsId = saveDoWorkoutPerformanceMetricsState.value.doWorkoutPerformanceMetrics.id,
                exerciseId = setWithProgress.baseSet.exerciseId,
                workoutId = setWithProgress.baseSet.workoutId,
                reps = setWithProgress.baseSet.reps,
                weight = setWithProgress.baseSet.weight,
                isDone = setWithProgress.isDone,
                time = null,
                date = Date() //Time of adding... TODO: make it time of starting the set...
            )
        }

        return doWorkoutExerciseSets
    }

    //Used only when canceling workout
    fun exitWorkout() {

        //Delete create workout
        val performanceMetrics = saveDoWorkoutPerformanceMetricsState.value.doWorkoutPerformanceMetrics
        viewModelScope.launch(dispatcher) {
            doWorkoutPerformanceMetricsUseCases.deleteDoWorkoutPerformanceMetricsUseCase(
                performanceMetrics.id
            ).collect{ result ->

                if(result.isSuccessful){
                    Log.d("DoWorkoutViewModel", "DoWorkoutPerformanceMetrics with id ${performanceMetrics.id} was deleted successfully!")

                    //Navigate back
                    navigateToDashboard()
                }else if(result.isError){

                    //Navigate back
                    navigateToDashboard()
                }
            }
        }
    }

    //Used when workout is completed
    fun navigateToDashboard() {
        onNavigationEvent(NavigationEvent.ClearBackstackAndNavigateTo(Destination.Dashboard))
    }

    //Show player icon for a second
    fun showPlayerOverlay() {
        viewModelScope.launch {
            _playerState.value = BaseState(isLoading = true)
            delay(1000)
            _playerState.value = BaseState(isLoading = false, isSuccessful = true)
        }
    }
}
