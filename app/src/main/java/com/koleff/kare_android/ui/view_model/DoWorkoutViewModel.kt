package com.koleff.kare_android.ui.view_model

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.common.TimerUtil
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.common.navigation.NavigationController
import com.koleff.kare_android.data.model.dto.ExerciseTime
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.usecases.WorkoutUseCases
import com.koleff.kare_android.ui.state.DoWorkoutData
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
            while (true) {
                Log.d("DoWorkoutViewModel", "----------------Timers------------------")
                Log.d("DoWorkoutViewModel", "Countdown time: ${countdownTimerState.value.time}")
                Log.d("DoWorkoutViewModel", "Workout time: ${workoutTimerState.value.time}")
                delay(1000)
            }
        }
    }

    private fun setup() {
        viewModelScope.launch(dispatcher) {
            Log.d("DoWorkoutViewModel", "Initialization...")

            _state.value = DoWorkoutState(
                isLoading = true
            )
            delay(Constants.fakeDelay)

            workoutUseCases.getWorkoutDetailsUseCase(workoutId).collect { result ->
                if (result.isSuccessful) {
                    val selectedWorkoutDetails = result.workoutDetails
                    Log.d("DoWorkoutViewModel", "Fetched workout: $selectedWorkoutDetails")

                    val firstExercise = selectedWorkoutDetails.exercises.firstOrNull() ?: run {
                        _state.value = DoWorkoutState(
                            isError = true,
                            error = KareError.WORKOUT_HAS_NO_EXERCISES
                        )
                        return@collect
                    }
                    Log.d("DoWorkoutViewModel", "First exercise: $firstExercise")

                    val firstSet = 1
                    _state.value = DoWorkoutState(
                        isSuccessful = true,
                        doWorkoutData = DoWorkoutData(
                            currentExercise = firstExercise,
                            currentSetNumber = firstSet,
                            workout = selectedWorkoutDetails,
                            isNextExerciseCountdown = true
                        )
                    )

                    confirmExercise()
                    startWorkoutTimer()
                } else if (result.isError) {
                    _state.value = DoWorkoutState(
                        isError = true,
                        error = result.error
                    )
                }
            }
        }
    }

    fun selectNextExercise() = with(_state.value.doWorkoutData) {
        Log.d("DoWorkoutViewModel", "Select next exercise requested.")

        //Find exercise position in list
        val currentExercisePosition = workout.exercises.indexOf(currentExercise)
        if (currentExercisePosition == -1) {
            _state.value = DoWorkoutState(
                isError = true,
                error = KareError.INVALID_EXERCISE
            )

            return@with
        }

        val currentExerciseSets =
            workout.exercises[currentExercisePosition].sets
        val isNextExercise =
            (currentSetNumber + 1 > currentExerciseSets.size && currentExerciseSets.isNotEmpty())
                    || (currentExerciseSets.isEmpty() && currentSetNumber + 1 > defaultTotalSets)
        if (isNextExercise) {

            //Latest exercise (and set)
            if (currentExercisePosition + 1 == workout.exercises.size) {
                Log.d("DoWorkoutViewModel", "Workout ${workout.name} finished!")

                //Workout finished...
                _state.value = DoWorkoutState(
                    isSuccessful = true,
                    doWorkoutData = DoWorkoutData(
                        isWorkoutCompleted = true
                    )
                )
            } else {
                val nextExercise = workout.exercises[currentExercisePosition + 1]
                Log.d("DoWorkoutViewModel", "Next exercise: $nextExercise")

                //Next exercise
                val updatedData = _state.value.doWorkoutData.copy(
                    isNextExerciseCountdown = true,
                    currentExercise = nextExercise,
                    currentSetNumber = 1
                )
                _state.value = DoWorkoutState(
                    isSuccessful = true,
                    doWorkoutData = updatedData
                )
            }
        } else {
            val nextSetNumber = currentSetNumber + 1
            Log.d("DoWorkoutViewModel", "Next set: $nextSetNumber")

            //Next set
            val updatedData = _state.value.doWorkoutData.copy(
                isNextExerciseCountdown = true,
                currentSetNumber = nextSetNumber
            )
            _state.value = DoWorkoutState(
                isSuccessful = true,
                doWorkoutData = updatedData
            )
        }

        startWorkoutTimer()
    }

    //Used when next exercise is selected and NextExerciseCountdownScreen is still showing
    private fun confirmExercise() {
        val updatedData = _state.value.doWorkoutData.copy(isNextExerciseCountdown = false)
        _state.value = _state.value.copy(doWorkoutData = updatedData)
    }

    private fun startCountdownTimer(countdownTime: ExerciseTime = state.value.doWorkoutData.countdownTime) {

        //Reset workout timer and state
        workoutTimer.resetTimer().also {
            val defaultWorkoutTime = state.value.doWorkoutData.defaultExerciseTime
            _workoutTimerState.value = _countdownTimerState.value.copy(time = defaultWorkoutTime)
        }

        countdownTimer.startTimer(totalSeconds = countdownTime.toSeconds()) { timeLeft ->
            _countdownTimerState.value = _countdownTimerState.value.copy(time = timeLeft)

            //Countdown has finished
            if(countdownTimerState.value.time == ExerciseTime(0, 0, 0)){
                Log.d("DoWorkoutViewModel", "Countdown finished! Selecting next exercise...")
                selectNextExercise()
            }
        }
    }

    private fun startWorkoutTimer(exerciseTime: ExerciseTime = state.value.doWorkoutData.defaultExerciseTime) {

        //Reset countdown timer and state
        countdownTimer.resetTimer().also {
            val defaultCountdownTime = state.value.doWorkoutData.countdownTime
            _countdownTimerState.value =
                _countdownTimerState.value.copy(time = defaultCountdownTime)
        }

        workoutTimer.startTimer(totalSeconds = exerciseTime.toSeconds()) { timeLeft ->
            _workoutTimerState.value = _workoutTimerState.value.copy(time = timeLeft)

            //Workout timer has finished
            if(workoutTimerState.value.time == ExerciseTime(0, 0, 0)){
                Log.d("DoWorkoutViewModel", "Exercise timer finished! Starting countdown timer for next exercise.")
                startCountdownTimer()
            }
        }
    }
}
