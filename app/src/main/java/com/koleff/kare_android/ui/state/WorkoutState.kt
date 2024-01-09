package com.koleff.kare_android.ui.state

import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.data.model.response.base_response.KareError

data class WorkoutState ( //TODO: rename to workoutsState
    val workoutList: List<WorkoutDto> = emptyList(),
    val isMyWorkoutScreen: Boolean = false,
    val isSuccessful: Boolean = false,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val error : KareError = KareError.GENERIC
)