package com.koleff.kare_android.ui.state

import com.koleff.kare_android.data.model.dto.ExerciseDetailsDto
import com.koleff.kare_android.data.model.dto.WorkoutConfigurationDto
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.data.room.entity.WorkoutConfiguration

data class WorkoutConfigurationState (
    val workoutConfiguration: WorkoutConfigurationDto = WorkoutConfigurationDto(),
    override val isSuccessful: Boolean = false,
    override val isLoading: Boolean = false,
    override val isError: Boolean = false,
    override val error: KareError = KareError.GENERIC
): BaseState(isSuccessful, isLoading, isError, error)