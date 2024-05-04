package com.koleff.kare_android.domain.usecases

import android.util.Log
import com.koleff.kare_android.data.model.dto.WorkoutConfigurationDto
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.data.room.entity.WorkoutConfiguration
import com.koleff.kare_android.ui.state.ExerciseState
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.repository.WorkoutRepository
import com.koleff.kare_android.ui.state.WorkoutConfigurationState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CreateWorkoutConfigurationUseCase(private val workoutRepository: WorkoutRepository) {

    suspend operator fun invoke(workoutConfiguration: WorkoutConfigurationDto): Flow<WorkoutConfigurationState> =
        workoutRepository.saveWorkoutConfiguration(workoutConfiguration).map { apiResult ->
            when (apiResult) {
                is ResultWrapper.ApiError -> WorkoutConfigurationState(
                    isError = true,
                    error = apiResult.error ?: KareError.GENERIC
                )

                is ResultWrapper.Loading -> WorkoutConfigurationState(isLoading = true)

                is ResultWrapper.Success -> {
                    Log.d("GetWorkoutConfigurationUseCase", "Workout configuration for workout with id ${workoutConfiguration.workoutId} created successfully!")

                    WorkoutConfigurationState(
                        isSuccessful = true,
                        workoutConfiguration = apiResult.data.workoutConfiguration
                    )
                }
            }
        }
}