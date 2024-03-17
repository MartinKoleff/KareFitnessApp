package com.koleff.kare_android.ui.view_model

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.common.di.MainDispatcher
import com.koleff.kare_android.common.navigation.NavigationController
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.usecases.WorkoutUseCases
import com.koleff.kare_android.ui.state.DoWorkoutData
import com.koleff.kare_android.ui.state.DoWorkoutState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
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

    private val _state: MutableStateFlow<DoWorkoutState> = MutableStateFlow(DoWorkoutState())
    val state: StateFlow<DoWorkoutState>
        get() = _state

    override fun clearError() {
        if (state.value.isError) {
            _state.value = DoWorkoutState()
        }
    }

    init {
        setup()
    }

    private fun setup() = with(_state.value.doWorkoutData) {
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
                } else if (result.isError) {
                    _state.value = DoWorkoutState(
                        isError = true,
                        error = result.error
                    )
                }

                delay(state.value.doWorkoutData.countdownTime.toSeconds().toLong())
                confirmExercise()
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

        val currentExerciseSets = workout.exercises[currentExercisePosition].sets
        if (currentSetNumber + 1 == currentExerciseSets.size) {

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
                _state.value = DoWorkoutState(
                    isSuccessful = true,
                    doWorkoutData = DoWorkoutData(
                        currentExercise = nextExercise,
                        isNextExerciseCountdown = true
                    )
                )
            }
        } else {
            val nextSetNumber = currentSetNumber + 1
            Log.d("DoWorkoutViewModel", "Next set: $nextSetNumber")

            //Next set
            _state.value = DoWorkoutState(
                isSuccessful = true,
                doWorkoutData = DoWorkoutData(
                    currentSetNumber = nextSetNumber,
                    isNextExerciseCountdown = true
                )
            )
        }
    }

    //Used when next exercise is selected and NextExerciseCountdownScreen is still showing
    fun confirmExercise() {
        _state.value.doWorkoutData.apply {
            isNextExerciseCountdown = false
        }
    }
}
