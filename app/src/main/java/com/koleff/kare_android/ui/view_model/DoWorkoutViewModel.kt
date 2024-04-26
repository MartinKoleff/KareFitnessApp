package com.koleff.kare_android.ui.view_model

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.koleff.kare_android.common.timer.TimerUtil
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.common.navigation.NavigationController
import com.koleff.kare_android.domain.usecases.DoWorkoutUseCases
import com.koleff.kare_android.domain.usecases.WorkoutUseCases
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.ui.state.DoWorkoutState
import com.koleff.kare_android.ui.state.TimerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoWorkoutViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val navigationController: NavigationController,
    private val workoutUseCases: WorkoutUseCases,
    private val doWorkoutUseCases: DoWorkoutUseCases,
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

    private fun selectNextExercise() {
        Log.d("DoWorkoutViewModel", "Select next exercise requested.")
        viewModelScope.launch(dispatcher) {
            doWorkoutUseCases.selectNextExerciseUseCase(_state.value.doWorkoutData)
                .collect { result ->
                    _state.value = result

                    if (result.isSuccessful) {

                        //Workout completed
                        if (result.doWorkoutData.isWorkoutCompleted) {
                            showWorkoutCompletedScreen()

                            //Stop timers...
                            workoutTimer.resetTimer()
                            countdownTimer.resetTimer()
                        } else {
                            showNextExerciseCountdownScreen()
                            startCountdownTimer()
                        }
                    }
                }
        }
    }


    //If called from the button update current set and next set directly...
    fun skipNextExercise() {
        viewModelScope.launch(dispatcher) {
            doWorkoutUseCases.skipNextExerciseUseCase(_state.value.doWorkoutData)
                .collect { result ->
                    _state.value = result

                    if (result.isSuccessful) {
                        startCountdownTimer()
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

    private fun showWorkoutCompletedScreen() {
        TODO("Not yet implemented")
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
}
