package com.koleff.kare_android.ui.state

import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.ExerciseTime
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.response.base_response.KareError

data class DoWorkoutState (
    val isSuccessful: Boolean = false,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val error : KareError = KareError.GENERIC,
    val doWorkoutData: DoWorkoutData = DoWorkoutData()
)

data class DoWorkoutData(
    val defaultExerciseTime: ExerciseTime = ExerciseTime(hours = 0, minutes = 1, seconds = 30),
    val currentExercise: ExerciseDto = ExerciseDto(),
    val currentSetNumber: Int = -1,
    val workout: WorkoutDetailsDto = WorkoutDetailsDto(),
    val isNextExercise: Boolean = false,
    val isNextSet: Boolean = false,
    val isWorkoutCompleted: Boolean = false
)
