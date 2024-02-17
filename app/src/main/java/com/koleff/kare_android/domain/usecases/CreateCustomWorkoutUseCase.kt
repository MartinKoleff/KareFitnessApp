package com.koleff.kare_android.domain.usecases

import android.util.Log
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.repository.WorkoutRepository
import com.koleff.kare_android.ui.state.WorkoutState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CreateCustomWorkoutUseCase(private val workoutRepository: WorkoutRepository) {

    suspend operator fun invoke(workoutDto: WorkoutDto): Flow<WorkoutState> =
        workoutRepository.createCustomWorkout(workoutDto).map { apiResult ->
            when (apiResult) {
                is ResultWrapper.ApiError -> {
                    WorkoutState(
                        isError = true,
                        error = apiResult.error ?: KareError.GENERIC
                    )
                }

                is ResultWrapper.Loading -> {
                    WorkoutState(isLoading = true)
                }

                is ResultWrapper.Success -> {
                    val workout = apiResult.data.workout

                    Log.d(
                        "CreateCustomWorkoutUseCase",
                        "Workout with id ${workout.workoutId} created."
                    )
                    Log.d(
                        "CreateCustomWorkoutUseCase",
                        "Workout: $workout"
                    )

                    WorkoutState(
                        isSuccessful = true,
                        workout = workout
                    )
                }
            }
        }
}
