package com.koleff.kare_android.ui.view_model

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.common.di.IoDispatcher
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

    private val _state: MutableStateFlow<DoWorkoutState> = MutableStateFlow(DoWorkoutState(isLoading = true))
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

        val currentExerciseSets =
            workout.exercises[currentExercisePosition].sets
        val isNextExercise = (currentSetNumber + 1 > currentExerciseSets.size && currentExerciseSets.isNotEmpty())
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
    }

    //Used when next exercise is selected and NextExerciseCountdownScreen is still showing
    fun confirmExercise() {
        val updatedData = _state.value.doWorkoutData.copy(isNextExerciseCountdown = false)
        _state.value = _state.value.copy(doWorkoutData = updatedData)
    }
}
