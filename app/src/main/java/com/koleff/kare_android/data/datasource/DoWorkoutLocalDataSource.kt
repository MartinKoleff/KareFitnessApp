package com.koleff.kare_android.data.datasource

import android.util.Log
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.response.DoWorkoutResponse
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.wrapper.DoWorkoutWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.ui.state.DoWorkoutData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class DoWorkoutLocalDataSource : DoWorkoutDataSource {

    override suspend fun initialSetup(workoutDetailsDto: WorkoutDetailsDto): Flow<ResultWrapper<DoWorkoutWrapper>> =
        flow {
            Log.d("DoWorkoutLocalDataSource-initialSetup", "Initialization...")
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val firstExercise = calculateNextExercise(
                currentExercise = null,
                allExercises = workoutDetailsDto.exercises
            )
            val firstSetNumber = if (firstExercise.sets.isNotEmpty()) 1 else -1
            Log.d("DoWorkoutLocalDataSource-initialSetup", "First exercise: $firstExercise")

            val nextExercise = calculateNextExercise(
                currentExercise = firstExercise,
                allExercises = workoutDetailsDto.exercises
            )
            val nextSetNumber = calculateNextSetNumber(
                currentNextSetNumber = firstSetNumber,
                currentExercise = firstExercise,
                nextExercise = nextExercise,
                allExercises = workoutDetailsDto.exercises
            )
            Log.d("DoWorkoutLocalDataSource-initialSetup", "Next exercise: $nextExercise")

            val isInvalidWorkout = firstExercise == ExerciseDto()
            if (isInvalidWorkout) {
                emit(ResultWrapper.ApiError(KareError.WORKOUT_HAS_NO_EXERCISES))
            } else {
                val result = DoWorkoutWrapper(
                    DoWorkoutResponse(
                        data = DoWorkoutData(
                            isSetupCompleted = true,
                            currentExercise = firstExercise,
                            currentSetNumber = firstSetNumber,
                            nextExercise = nextExercise,
                            nextSetNumber = nextSetNumber,
                            workout = workoutDetailsDto,
                            isBetweenExerciseCountdown = false,
                            countdownTime = workoutDetailsDto.configuration.cooldownTime
                        )
                    )
                )
                emit(ResultWrapper.Success(result))
            }
        }

    private fun calculateNextExercise(
        currentExercise: ExerciseDto?,
        allExercises: List<ExerciseDto>
    ): ExerciseDto {

        //No currentExercise -> no nextExercise
        if (currentExercise == ExerciseDto()) return ExerciseDto()

        //Find the index of the currentExercise, if it's not in the list indexOf returns -1
        val currentExerciseIndex = currentExercise?.let { allExercises.indexOf(it) } ?: -1

        //Filter out all exercises before (and including) the currentExercise index
        val remainingExercises = allExercises.drop(currentExerciseIndex + 1)

        for (exercise in remainingExercises) {
            if (exercise.sets.isNotEmpty()) return exercise
        }

        return ExerciseDto() //No exercises are left -> end of the workout
    }

    private fun calculateNextSetNumber(
        currentNextSetNumber: Int,
        currentExercise: ExerciseDto,
        nextExercise: ExerciseDto,
        allExercises: List<ExerciseDto>
    ): Int {
        if (currentNextSetNumber == -1) return -1 //Invalid workout

        if (currentExercise.sets.size >= currentNextSetNumber + 1) return currentNextSetNumber + 1
        if (nextExercise.sets.isNotEmpty()) return 1

        val remainingExercises = allExercises.dropWhile { it != nextExercise }
        for (exercise in remainingExercises) {
            if (exercise.sets.isNotEmpty()) return 1
        }

        return -1 //No more sets available -> end of the workout
    }

    override suspend fun skipNextSet(currentDoWorkoutData: DoWorkoutData) =
        flow {
            with(currentDoWorkoutData) {

                //Validation
                if (currentDoWorkoutData == DoWorkoutData()
                    || currentExercise == ExerciseDto()
                ) {
                    emit(ResultWrapper.ApiError(KareError.INVALID_WORKOUT))
                }

                var isNextExercise = false

                val currentExercise =
                    if (currentSetNumber + 1 <= currentExercise.sets.size) {
                        currentExercise
                    } //There are sets left
                    else {
                        calculateNextExercise(
                            currentExercise = this.currentExercise,
                            allExercises = workout.exercises
                        ).also {
                            isNextExercise = true
                        }
                    }

                val nextExercise = calculateNextExercise(
                    currentExercise = currentExercise,
                    allExercises = workout.exercises
                )

                val currentSetNumber =
                    if (isNextExercise) currentExercise.sets.firstOrNull()?.number
                        ?: -1 //TODO: Add error handling...
                    else nextSetNumber

                val nextSetNumber = calculateNextSetNumber(
                    currentNextSetNumber = currentSetNumber,
                    currentExercise = currentExercise,
                    nextExercise = nextExercise,
                    allExercises = workout.exercises
                )

                Log.d(
                    "DoWorkoutLocalDataSource-skipNextSet",
                    "New current exercise: $currentExercise"
                )
                Log.d(
                    "DoWorkoutLocalDataSource-skipNextSet",
                    "Current set number: $currentSetNumber"
                )
                Log.d(
                    "DoWorkoutLocalDataSource-skipNextSet",
                    "Next exercise: $nextExercise"
                )
                Log.d(
                    "DoWorkoutLocalDataSource-skipNextSet",
                    "Next set number: $nextSetNumber"
                )

                //No current exercise or no next set and no current set -> workout is completed
                val isWorkoutCompleted =
                    currentExercise == ExerciseDto()
                            || (currentSetNumber == -1 && nextSetNumber == -1)
                val updatedData = this.copy(
                    isSetupCompleted = isSetupCompleted,
                    currentExercise = currentExercise,
                    currentSetNumber = currentSetNumber,
                    nextExercise = nextExercise,
                    nextSetNumber = nextSetNumber,
                    isBetweenExerciseCountdown = false,
                    isWorkoutCompleted = isWorkoutCompleted
                )

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

    override suspend fun skipNextExercise(currentDoWorkoutData: DoWorkoutData) =
        flow {
            with(currentDoWorkoutData) {

                //Validation
                if (currentDoWorkoutData == DoWorkoutData()
                    || currentExercise == ExerciseDto()
                ) {
                    emit(ResultWrapper.ApiError(KareError.INVALID_WORKOUT))
                }

                val newCurrentExercise: ExerciseDto
                val newNextExercise: ExerciseDto
                val currentSetNumber: Int
                val nextSetNumber: Int
                if (currentSet == currentExercise.sets.last()) {
                    newCurrentExercise = calculateNextExercise(
                        currentExercise = currentExercise,
                        allExercises = workout.exercises
                    )

                    newNextExercise = calculateNextExercise(
                        currentExercise = newCurrentExercise,
                        allExercises = workout.exercises
                    )

                    currentSetNumber =
                        newCurrentExercise.sets.lastOrNull()?.number
                            ?: -1 //TODO: Add error handling...
                    nextSetNumber =
                        newNextExercise.sets.firstOrNull()?.number
                            ?: -1 //TODO: Add error handling...
                } else {

                    currentSetNumber =
                        currentExercise.sets.lastOrNull()?.number
                            ?: -1 //TODO: Add error handling...
                    nextSetNumber =
                        nextExercise.sets.firstOrNull()?.number ?: -1 //TODO: Add error handling...

                    newNextExercise = nextExercise
                    newCurrentExercise = currentExercise
                }

                Log.d(
                    "DoWorkoutLocalDataSource-skipNextExercise",
                    "New current exercise: $newCurrentExercise"
                )
                Log.d(
                    "DoWorkoutLocalDataSource-skipNextExercise",
                    "Current set number: $currentSetNumber"
                )
                Log.d(
                    "DoWorkoutLocalDataSource-skipNextExercise",
                    "Next exercise: $newNextExercise"
                )
                Log.d(
                    "DoWorkoutLocalDataSource-skipNextExercise",
                    "Next set number: $nextSetNumber"
                )

                //No current exercise or no next set and no current set -> workout is completed
                val isWorkoutCompleted =
                    (newNextExercise == ExerciseDto() && nextSetNumber == -1)
                            && (currentSetNumber == newCurrentExercise.sets.size || currentSetNumber == -1)
                val updatedData = this.copy(
                    isSetupCompleted = isSetupCompleted,
                    currentExercise = newCurrentExercise,
                    currentSetNumber = currentSetNumber,
                    nextExercise = newNextExercise,
                    nextSetNumber = nextSetNumber,
                    isBetweenExerciseCountdown = false,
                    isWorkoutCompleted = isWorkoutCompleted
                )

                val result = DoWorkoutWrapper(
                    DoWorkoutResponse(
                        data = updatedData
                    )
                )
                emit(ResultWrapper.Success(result))
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