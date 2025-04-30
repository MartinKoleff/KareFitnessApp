package com.koleff.kare_android.data.datasource.do_workout

import android.util.Log
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.data.model.dto.ExerciseData
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.response.DoWorkoutResponse
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.data.room.dao.ExerciseDetailsDao
import com.koleff.kare_android.domain.wrapper.DoWorkoutWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.ui.state.DoWorkoutData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DoWorkoutLocalDataSource(
    val exerciseDetailsDao: ExerciseDetailsDao
) : DoWorkoutDataSource {

    override suspend fun initialSetup(workoutDetails: WorkoutDetailsDto): Flow<ResultWrapper<DoWorkoutWrapper>> =
        flow {
            Log.d("DoWorkoutLocalDataSource-initialSetup", "Initialization...")
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val exercisesFullData =
                workoutDetails.exercises.map { exercise -> convertToExerciseData(exercise) }

            val firstExerciseData = calculateNextExercise(
                currentExerciseData = null,
                allExercisesData = exercisesFullData
            )
            val firstSetNumber = if (firstExerciseData.exerciseDto.sets.isNotEmpty()) 1 else -1
            Log.d("DoWorkoutLocalDataSource-initialSetup", "First exercise: $firstExerciseData")

            val nextExerciseData = calculateNextExercise(
                currentExerciseData = firstExerciseData,
                allExercisesData = exercisesFullData
            )
            val nextSetNumber = calculateNextSetNumber(
                currentNextSetNumber = firstSetNumber,
                currentExerciseData = firstExerciseData,
                nextExerciseData = nextExerciseData,
                allExercisesData = exercisesFullData
            )
            Log.d("DoWorkoutLocalDataSource-initialSetup", "Next exercise: $nextExerciseData")

            exercisesFullData.forEach { exerciseData ->
                Log.d(
                    "DoWorkoutLocalDataSource-initialSetup",
                    "Exercises with full data: $exerciseData"
                )
            }

            val isInvalidWorkout =
                firstExerciseData == ExerciseData() || firstExerciseData.exerciseDto == ExerciseDto()
            if (isInvalidWorkout) {
                emit(ResultWrapper.ApiError(KareError.WORKOUT_HAS_NO_EXERCISES))
            } else {
                val result = DoWorkoutWrapper(
                    DoWorkoutResponse(
                        data = DoWorkoutData(
                            isSetupCompleted = true,
                            currentExercise = firstExerciseData,
                            currentSetNumber = firstSetNumber,
                            nextExercise = nextExerciseData,
                            nextSetNumber = nextSetNumber,
                            workout = workoutDetails,
                            exercises = exercisesFullData,
                            isBetweenExerciseCountdown = false,
                            countdownTime = workoutDetails.configuration.cooldownTime
                        )
                    )
                )
                emit(ResultWrapper.Success(result))
            }
        }


    //Helping functions
    private fun convertToExerciseData(exercise: ExerciseDto): ExerciseData {
        Log.d("DoWorkoutLocalDataSource", "Converting exercise: $exercise")
        val exerciseDetails = exerciseDetailsDao.getExerciseDetailsByExerciseAndWorkoutId(
            exercise.exerciseId,
            exercise.workoutId
        ).toDto()

        return ExerciseData(
            exercise,
            exerciseDetails
        )
    }

    private fun calculateNextExercise(
        currentExerciseData: ExerciseData?,
        allExercisesData: List<ExerciseData>
    ): ExerciseData {

        //No currentExercise -> no nextExercise
        if (currentExerciseData == ExerciseData()
            || currentExerciseData?.exerciseDto == ExerciseDto()
        ) return ExerciseData()

        //Find the index of the currentExercise, if it's not in the list indexOf returns -1
        val currentExerciseIndex = currentExerciseData?.let { currentExercise ->
            allExercisesData.indexOf(currentExercise)
        } ?: -1

        //Filter out all exercises before (and including) the currentExercise index
        val remainingExercisesData = allExercisesData.drop(currentExerciseIndex + 1)

        for (exercise in remainingExercisesData) {
            if (exercise.exerciseDto.sets.isNotEmpty()) return exercise
        }

        return ExerciseData() //No exercises are left -> end of the workout
    }

    private fun calculateNextSetNumber(
        currentNextSetNumber: Int,
        currentExerciseData: ExerciseData,
        nextExerciseData: ExerciseData,
        allExercisesData: List<ExerciseData>
    ): Int {
        if (currentNextSetNumber == -1) return -1 //Invalid workout

        if (currentExerciseData.exerciseDto.sets.size >= currentNextSetNumber + 1) return currentNextSetNumber + 1
        if (nextExerciseData.exerciseDto.sets.isNotEmpty()) return 1

        val remainingExercisesData = allExercisesData
            .dropWhile { it != nextExerciseData }
            .map { it.exerciseDto }
        for (exercise in remainingExercisesData) {
            if (exercise.sets.isNotEmpty()) return 1
        }

        return -1 //No more sets available -> end of the workout
    }

    override suspend fun skipNextSet(currentDoWorkoutData: DoWorkoutData) =
        flow {
            with(currentDoWorkoutData) {

                //Validation
                if (currentDoWorkoutData == DoWorkoutData()
                    || currentExercise.exerciseDto == ExerciseDto()
                    || currentExercise == ExerciseData()
                ) {
                    emit(ResultWrapper.ApiError(KareError.INVALID_WORKOUT))
                }

                var isNextExercise = false

                val currentExerciseData =
                    if (currentSetNumber + 1 <= currentExercise.exerciseDto.sets.size) {
                        currentExercise
                    } //There are sets left
                    else {
                        calculateNextExercise(
                            currentExerciseData = this.currentExercise,
                            allExercisesData = currentDoWorkoutData.exercises
                        ).also {
                            isNextExercise = true
                        }
                    }

                val nextExerciseData = calculateNextExercise(
                    currentExerciseData = currentExerciseData,
                    allExercisesData = currentDoWorkoutData.exercises
                )

                val currentSetNumber =
                    if (isNextExercise) currentExerciseData.exerciseDto.sets.firstOrNull()?.number
                        ?: -1 //TODO: Add error handling...
                    else nextSetNumber

                val nextSetNumber = calculateNextSetNumber(
                    currentNextSetNumber = currentSetNumber,
                    currentExerciseData = currentExerciseData,
                    nextExerciseData = nextExerciseData,
                    allExercisesData = currentDoWorkoutData.exercises
                )

                Log.d(
                    "DoWorkoutLocalDataSource-skipNextSet",
                    "New current exercise: $currentExerciseData"
                )
                Log.d(
                    "DoWorkoutLocalDataSource-skipNextSet",
                    "Current set number: $currentSetNumber"
                )
                Log.d(
                    "DoWorkoutLocalDataSource-skipNextSet",
                    "Next exercise: $nextExerciseData"
                )
                Log.d(
                    "DoWorkoutLocalDataSource-skipNextSet",
                    "Next set number: $nextSetNumber"
                )

                //No current exercise or no next set and no current set -> workout is completed
                val isWorkoutCompleted =
                    currentExerciseData.exerciseDto == ExerciseDto()
                            || currentExerciseData == ExerciseData()
                            || (currentSetNumber == -1 && nextSetNumber == -1)
                val updatedData = this.copy(
                    isSetupCompleted = isSetupCompleted,
                    currentExercise = currentExerciseData,
                    currentSetNumber = currentSetNumber,
                    nextExercise = nextExerciseData,
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

    //TODO: make it be on the same exercise last set and then 1st exercise of the next exercise
    override suspend fun skipNextExercise(currentDoWorkoutData: DoWorkoutData) =
        flow {
            with(currentDoWorkoutData) {

                //Validation
                if (currentDoWorkoutData == DoWorkoutData()
                    || currentExercise == ExerciseData()
                    || currentExercise.exerciseDto == ExerciseDto()
                ) {
                    emit(ResultWrapper.ApiError(KareError.INVALID_WORKOUT))
                }

                val newCurrentExerciseData = currentExercise
                val newCurrentSet = newCurrentExerciseData.exerciseDto.sets.lastOrNull()
                val newNextExerciseIndex = this.exercises.indexOf(nextExercise)
                val newNextExerciseData = if (newNextExerciseIndex == -1) {
                    ExerciseData()
                } else {
                    this.exercises.getOrNull(newNextExerciseIndex) ?: ExerciseData()
                }

                val currentSetNumber = newCurrentSet?.number
                    ?: -1 //TODO: add error handling...

                val nextSetNumber = calculateNextSetNumber(
                    currentNextSetNumber = currentSetNumber,
                    currentExerciseData = newCurrentExerciseData,
                    nextExerciseData = newNextExerciseData,
                    allExercisesData = currentDoWorkoutData.exercises
                )

                Log.d(
                    "DoWorkoutLocalDataSource-skipNextExercise",
                    "New current exercise: $newCurrentExerciseData"
                )
                Log.d(
                    "DoWorkoutLocalDataSource-skipNextExercise",
                    "Current set number: $currentSetNumber"
                )
                Log.d(
                    "DoWorkoutLocalDataSource-skipNextExercise",
                    "Next exercise: $newNextExerciseData"
                )
                Log.d(
                    "DoWorkoutLocalDataSource-skipNextExercise",
                    "Next set number: $nextSetNumber"
                )

                //No current exercise or no next set and no current set -> workout is completed
                val isWorkoutCompleted =
                    ((newNextExerciseData.exerciseDto == ExerciseDto() || newNextExerciseData == ExerciseData()) && nextSetNumber == -1)
                            && (currentSetNumber == newCurrentExerciseData.exerciseDto.sets.size || currentSetNumber == -1)
                val updatedData = this.copy(
                    isSetupCompleted = isSetupCompleted,
                    currentExercise = newCurrentExerciseData,
                    currentSetNumber = currentSetNumber,
                    nextExercise = newNextExerciseData,
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
}