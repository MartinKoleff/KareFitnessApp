package com.koleff.kare_android.ui.view_model

import android.util.Log
import com.koleff.kare_android.common.navigation.NavigationController
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.state.DoWorkoutData
import com.koleff.kare_android.ui.state.DoWorkoutState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DoWorkoutViewModel @Inject constructor(private val navigationController: NavigationController) :
    BaseViewModel(navigationController) {

    private val _state: MutableStateFlow<DoWorkoutState> = MutableStateFlow(DoWorkoutState())
    val state: StateFlow<DoWorkoutState>
        get() = _state

    override fun clearError() {
        if (state.value.isError) {
            _state.value = DoWorkoutState()
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
                    doWorkoutData = DoWorkoutData(
                        isWorkoutCompleted = true
                    )
                )
            } else {
                val nextExercise = workout.exercises[currentExercisePosition + 1]
                Log.d("DoWorkoutViewModel", "Next exercise: $nextExercise")

                //Next exercise
                _state.value = DoWorkoutState(
                    isLoading = true,
                    doWorkoutData = DoWorkoutData(
                        currentExercise = nextExercise
                    )
                )
            }
        } else {
            val nextSetNumber = currentSetNumber + 1
            Log.d("DoWorkoutViewModel", "Next set: $nextSetNumber")

            //Next set
            _state.value = DoWorkoutState(
                isLoading = true,
                doWorkoutData = DoWorkoutData(
                    currentSetNumber = nextSetNumber
                )
            )
        }
    }

    //Used when next exercise is selected and NextExerciseCountdownScreen is still showing
    fun confirmNextExercise(){
        _state.value.apply {
            isLoading = false
        }
    }
}
