package com.koleff.kare_android.ui.state

import com.koleff.kare_android.data.model.dto.ExerciseData
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
    val isSetupCompleted: Boolean = false,
    val currentExercise: ExerciseData = ExerciseData(),
    val nextExercise: ExerciseData = ExerciseData(),
    val currentSetNumber: Int = -1,
    val nextSetNumber: Int = -1,
    val workout: WorkoutDetailsDto = WorkoutDetailsDto(),
    val exercises: List<ExerciseData> = emptyList(),
    var defaultTotalSets: Int = 4,
    val defaultExerciseTime: ExerciseTime = ExerciseTime(hours = 0, minutes = 1, seconds = 0),
    val countdownTime: ExerciseTime = ExerciseTime(hours = 0, minutes = 0, seconds = 10),
    val restTime: ExerciseTime = ExerciseTime(hours = 0, minutes = 0, seconds = 30),
    val isWorkoutCompleted: Boolean = false,
    var isBetweenExerciseCountdown: Boolean = false,
    var isRestCountdown: Boolean = false
) {
    val currentSet: ExerciseSetDto
        get() = currentExercise.exerciseDto.sets.getOrNull(currentSetNumber - 1)
            ?: ExerciseSetDto(
                number = -1,
                workoutId = currentExercise.exerciseDto.workoutId,
                exerciseId = currentExercise.exerciseDto.exerciseId,
                reps = -1,
                weight = -1f
            )

    val nextSet: ExerciseSetDto
        get() = if (currentSetNumber == currentExercise.exerciseDto.sets.size && nextSetNumber <= currentSetNumber)
            nextExercise.exerciseDto.sets.getOrNull(nextSetNumber - 1) ?: defaultSet
        else
            currentExercise.exerciseDto.sets.getOrNull(nextSetNumber - 1) ?: defaultSet

    val isNextExercise: Boolean //Used for UI only...
        get() = currentSetNumber == currentExercise.exerciseDto.sets.size

    val totalSets: Int = if(isNextExercise) nextExercise.exerciseDto.sets.size else currentExercise.exerciseDto.sets.size

    private val defaultSet =  ExerciseSetDto(
        number = -1,
        workoutId = currentExercise.exerciseDto.workoutId,
        exerciseId = currentExercise.exerciseDto.exerciseId,
        reps = -1,
        weight = -1f
    )
}
