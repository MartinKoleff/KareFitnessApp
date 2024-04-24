package com.koleff.kare_android.ui.state

import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.ExerciseSetDto
import com.koleff.kare_android.data.model.dto.ExerciseTime
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.response.base_response.KareError

data class DoWorkoutState(
    val doWorkoutData: DoWorkoutData = DoWorkoutData(),
    override val isSuccessful: Boolean = false,
    override val isLoading: Boolean = false,
    override val isError: Boolean = false,
    override val error: KareError = KareError.GENERIC
) : BaseState(isSuccessful, isLoading, isError, error)

data class DoWorkoutData(
    val currentExercise: ExerciseDto = ExerciseDto(),
    val nextExercise: ExerciseDto = ExerciseDto(),
    val currentSetNumber: Int = -1,
    val nextSetNumber: Int = -1,
    val workout: WorkoutDetailsDto = WorkoutDetailsDto(),
    var defaultTotalSets: Int = 4,
    val defaultExerciseTime: ExerciseTime = ExerciseTime(hours = 0, minutes = 1, seconds = 0),
    val countdownTime: ExerciseTime = ExerciseTime(hours = 0, minutes = 0, seconds = 10),
    val restTime: ExerciseTime = ExerciseTime(hours = 0, minutes = 0, seconds = 30),
    val isWorkoutCompleted: Boolean = false,
    var isBetweenExerciseCountdown: Boolean = false,
    var isNextExercise: Boolean = false,
    var isRestCountdown: Boolean = false
){
) {
    val currentSet: ExerciseSetDto
        get() = currentExercise.sets.getOrNull(currentSetNumber - 1)
            ?: ExerciseSetDto(
                number = -1,
                workoutId = currentExercise.workoutId,
                exerciseId = currentExercise.exerciseId,
                reps = -1,
                weight = -1f
            )

    val nextSet: ExerciseSetDto
        get() = currentExercise.sets.getOrNull(nextSetNumber - 1)
            ?: ExerciseSetDto(
                number = -1,
                workoutId = currentExercise.workoutId,
                exerciseId = currentExercise.exerciseId,
                reps = -1,
                weight = -1f
            )
}
