package com.koleff.kare_android.data.datasource

import android.util.Log
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.ExerciseTime
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.response.DoWorkoutResponse
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.wrapper.DoWorkoutWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.ui.state.DoWorkoutData
import com.koleff.kare_android.ui.state.DoWorkoutState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class DoWorkoutLocalDataSource : DoWorkoutDataSource {

    override suspend fun initialSetup(workoutDetailsDto: WorkoutDetailsDto): Flow<ResultWrapper<DoWorkoutWrapper>> =
        flow {
            Log.d("DoWorkoutLocalDataSource-initialSetup", "Initialization...")
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val firstExercise = workoutDetailsDto.exercises.firstOrNull() ?: run {
                emit(
                    ResultWrapper.ApiError(
                        error = KareError.WORKOUT_HAS_NO_EXERCISES
                    )
                )
                return@flow
            }
            val firstSetNumber = 1
            Log.d("DoWorkoutLocalDataSource-initialSetup", "First exercise: $firstExercise")

            val nextExercise = workoutDetailsDto.exercises.getOrNull(1) ?: ExerciseDto()
            val nextSetNumber = 2
            Log.d("DoWorkoutLocalDataSource-initialSetup", "Next exercise: $nextExercise")

            val result = DoWorkoutWrapper(
                DoWorkoutResponse(
                    data = DoWorkoutData(
                        currentExercise = firstExercise,
                        currentSetNumber = firstSetNumber,
                        nextExercise = nextExercise,
                        nextSetNumber = nextSetNumber,
                        workout = workoutDetailsDto,
                        isBetweenExerciseCountdown = true
                    )
                )
            )
            emit(ResultWrapper.Success(result))
        }

    override suspend fun selectNextExercise(
        currentDoWorkoutData: DoWorkoutData
    ): Flow<ResultWrapper<DoWorkoutWrapper>> =
        flow {
            with(currentDoWorkoutData) {
                val workoutDetails = currentDoWorkoutData.workout

                //Find exercise position in list
                val currentExercisePosition = workoutDetails.exercises.indexOf(currentExercise)
                if (currentExercisePosition == -1) {
                    emit(
                        ResultWrapper.ApiError(
                            error = KareError.INVALID_EXERCISE
                        )
                    )
                    return@flow
                }

                val currentExerciseSets =
                    workoutDetails.exercises[currentExercisePosition].sets
                val isNextExercise =
                    (currentSetNumber + 1 > currentExerciseSets.size && currentExerciseSets.isNotEmpty())
                            || (currentExerciseSets.isEmpty() && currentSetNumber + 1 > defaultTotalSets)

                if (isNextExercise) {

                    //Latest exercise (and set)
                    if (currentExercisePosition + 1 == workout.exercises.size) {
                        Log.d(
                            "DoWorkoutLocalDataSource-selectNextExercise",
                            "Workout ${workoutDetails.name} finished!"
                        )

                        //Workout finished...
                        val result = DoWorkoutWrapper(
                            DoWorkoutResponse(
                                data = DoWorkoutData(
                                    isWorkoutCompleted = true
                                )
                            )
                        )
                        emit(ResultWrapper.Success(result))
                    } else {
                        val nextSetNumber =
                            if (currentExerciseSets.size >= currentSetNumber + 1) currentSetNumber + 1 else 1
                        Log.d(
                            "DoWorkoutLocalDataSource-selectNextExercise",
                            "Next exercise: $nextExercise"
                        )

                        //Next exercise
                        val updatedData = currentDoWorkoutData.copy(
                            //currentSetNumber = currentExerciseSets.size,
                            nextSetNumber = nextSetNumber,
                            isNextExercise = true
                        )
                        val result = DoWorkoutWrapper(
                            DoWorkoutResponse(
                                data = updatedData
                            )
                        )
                        emit(ResultWrapper.Success(result))
                    }
                } else {
                    val nextSetNumber =
                        if (currentExerciseSets.size >= currentSetNumber + 1) currentSetNumber + 1 else 1
                    Log.d(
                        "DoWorkoutLocalDataSource-selectNextExercise",
                        "Current set becomes: $currentSetNumber"
                    )
                    Log.d("DoWorkoutLocalDataSource-selectNextExercise", "Next set: $nextSetNumber")

                    //Next set
                    val updatedData = currentDoWorkoutData.copy(
                        currentSetNumber = currentSetNumber,
                        nextSetNumber = nextSetNumber,
                        isNextExercise = false
                    )
                    val result = DoWorkoutWrapper(
                        DoWorkoutResponse(
                            data = updatedData
                        )
                    )
                    emit(ResultWrapper.Success(result))
                }
            }
        }

    override suspend fun skipNextExercise(
        currentDoWorkoutData: DoWorkoutData
    ): Flow<ResultWrapper<DoWorkoutWrapper>> = flow {
        updateExerciseSetsAfterTimerUseCase(currentDoWorkoutData).collect { result ->
            if (result.data.isSuccessful) {
                emit(selectNextExercise(result.data.data).first())
            } else {
                emit(ResultWrapper.ApiError(error = result.data.error))
            }
        }
    }

    override suspend fun updateExerciseSetsAfterTimerUseCase(currentDoWorkoutData: DoWorkoutData) =
        flow {
            with(currentDoWorkoutData) {
                val currentSetNumber = nextSetNumber //Next set becomes current
                val nextSetNumber =
                    if (currentExercise.sets.size >= currentSetNumber + 1) currentSetNumber + 1 else 1

                var updatedData = this.copy(
                    currentSetNumber = currentSetNumber,
                    nextSetNumber = nextSetNumber,
                )

                //Update current and next exercise
                if (isNextExercise) {
                    val currentExercisePosition = workout.exercises.indexOf(currentExercise)
                    val currentExercise = workout.exercises[currentExercisePosition + 1]
                    val nextExercise = workout.exercises[currentExercisePosition + 2]
                    Log.d(
                        "DoWorkoutLocalDataSource-updateExerciseSet",
                        "New current exercise: $currentExercise"
                    )
                    Log.d(
                        "DoWorkoutLocalDataSource-updateExerciseSet",
                        "Next exercise: $nextExercise"
                    )

                    updatedData = updatedData.copy(
                        currentExercise = currentExercise,
                        currentSetNumber = 1,
                        nextExercise = nextExercise,
                        isNextExercise = false
                    )
                }

                val result = DoWorkoutWrapper(
                    DoWorkoutResponse(
                        data = updatedData
                    )
                )
                emit(ResultWrapper.Success(result))
                Log.d(
                    "DoWorkoutLocalDataSource-updateExerciseSet",
                    "Current set becomes: $currentSetNumber"
                )
                Log.d("DoWorkoutLocalDataSource-updateExerciseSet", "Next set: $nextSetNumber")
            }
        }


//    override suspend fun startCountdownTimer(): Flow<ResultWrapper<DoWorkoutWrapper>> = flow{
//
//        //Reset workout timer and state
//        workoutTimer.resetTimer().also {
//            val defaultWorkoutTime = state.value.doWorkoutData.defaultExerciseTime
//            _workoutTimerState.value =
//                _countdownTimerState.value.copy(time = defaultWorkoutTime)
//        }
//
//        countdownTimer.startTimer(totalSeconds = countdownTime.toSeconds()) { timeLeft ->
//            _countdownTimerState.value = _countdownTimerState.value.copy(time = timeLeft)
//
//            //Countdown has finished
//            if (countdownTimerState.value.time == ExerciseTime(0, 0, 0)) {
//                Log.d("DoWorkoutViewModel", "Countdown finished! Selecting next exercise...")
//                startWorkoutTimer()
//            }
//        }
//    }
//
//    override suspend fun startWorkoutTimer(): Flow<ResultWrapper<DoWorkoutWrapper>> = flow {
//
//        //Reset countdown timer and state
//        countdownTimer.resetTimer().also {
//            val defaultCountdownTime = countdownTime
//            _countdownTimerState.value =
//                _countdownTimerState.value.copy(time = defaultCountdownTime)
//        }
//
//        //Update current and next exercise sets after countdown timer has finished.
//        if (!isInitialCall) {
//            updateExerciseSets()
//        }
//
//        workoutTimer.startTimer(totalSeconds = exerciseTime.toSeconds()) { timeLeft ->
//            _workoutTimerState.value = _workoutTimerState.value.copy(time = timeLeft)
//
//            //Workout timer has finished -> select next exercise
//            if (workoutTimerState.value.time == ExerciseTime(0, 0, 0)) {
//                Log.d(
//                    "DoWorkoutViewModel",
//                    "Exercise timer finished! Starting countdown timer for next exercise."
//                )
//                selectNextExercise()
//            }
//        }
//    }
}