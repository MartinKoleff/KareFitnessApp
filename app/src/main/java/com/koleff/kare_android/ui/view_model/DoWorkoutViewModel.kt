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

    //Countdown time between exercises and sets (time to get ready)
    private val countdownTime: Int = 10
    override fun clearError() {
        if (state.value.isError) {
            _state.value = DoWorkoutState()
        }
    }

    fun selectNextExercise() = with(_state.value.doWorkoutData) {

        //TODO: loading logic to show next exercise countdown screen for 10 seconds for example...
        Log.d("DoWorkoutViewModel", "Next exercise requested.")

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

                //Workout finished...
                _state.value = DoWorkoutState(
                    doWorkoutData = DoWorkoutData(
                        isWorkoutCompleted = true
                    )
                )
            } else {

                //Next exercise
                _state.value = DoWorkoutState(
                    isSuccessful = true,
                    doWorkoutData = DoWorkoutData(
                        currentExercise = workout.exercises[currentExercisePosition + 1],
                        isNextExercise = true
                    )
                )
            }
        } else {

            //Next set
            _state.value = DoWorkoutState(
                isSuccessful = true,
                doWorkoutData = DoWorkoutData(
                    currentSetNumber = currentSetNumber + 1,
                    isNextSet = true
                )
            )
        }
    }
}

// //Temporary navigating to Do Workout Screen to test it...
//    workoutDetailsViewModel.onNavigationEvent(
//        NavigationEvent.NavigateTo(
//            Destination.DoWorkoutScreen(workoutId = workoutDetailsState.workoutDetails.workoutId)
//        )
//    )

//Prepare next exercise and start countdown...
//fun prepareNextExercise(){
//
//}

//fun startCountdown(){
//}