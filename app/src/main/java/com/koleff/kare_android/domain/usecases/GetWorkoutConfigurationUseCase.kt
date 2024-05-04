package com.koleff.kare_android.domain.usecases

import android.util.Log
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.data.room.entity.WorkoutConfiguration
import com.koleff.kare_android.ui.state.ExerciseState
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.repository.WorkoutRepository
import com.koleff.kare_android.ui.state.WorkoutConfigurationState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetWorkoutConfigurationUseCase(private val workoutRepository: WorkoutRepository) {

    suspend operator fun invoke(workoutId: Int): Flow<WorkoutConfigurationState> =
        workoutRepository.getWorkoutConfiguration(workoutId).map { apiResult ->
            when (apiResult) {
                is ResultWrapper.ApiError -> WorkoutConfigurationState(
                    isError = true,
                    error = apiResult.error ?: KareError.GENERIC
                )

                is ResultWrapper.Loading -> WorkoutConfigurationState(isLoading = true)

                is ResultWrapper.Success -> {
                    Log.d("GetWorkoutConfigurationUseCase", "Workout configuration for workout with id $workoutId fetched.")

                    WorkoutConfigurationState(
                        isSuccessful = true,
                        workoutConfiguration = apiResult.data.workoutConfiguration
                    )
                }
            }
        }
}