package com.koleff.kare_android.domain.usecases

import android.util.Log
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.repository.WorkoutRepository
import com.koleff.kare_android.ui.state.WorkoutState
import com.koleff.kare_android.ui.state.WorkoutDetailsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CreateCustomWorkoutDetailsUseCase(private val workoutRepository: WorkoutRepository) {

    suspend operator fun invoke(workoutDetailsDto: WorkoutDetailsDto): Flow<WorkoutDetailsState> =
        workoutRepository.createCustomWorkoutDetails(workoutDetailsDto).map { apiResult ->
            when (apiResult) {
                is ResultWrapper.ApiError -> {
                    WorkoutDetailsState(
                        isError = true,
                        error = apiResult.error ?: KareError.GENERIC
                    )
                }

                is ResultWrapper.Loading -> {
                    WorkoutDetailsState(isLoading = true)
                }

                is ResultWrapper.Success -> {
                    val workoutDetails = apiResult.data.workoutDetails

                    Log.d(
                        "CreateCustomWorkoutDetailsUseCase",
                        "Workout details with id ${workoutDetails.workoutId} created."
                    )
                    Log.d(
                        "CreateCustomWorkoutDetailsUseCase",
                        "Workout details: $workoutDetails"
                    )

                    WorkoutDetailsState(
                        isSuccessful = true,
                        workoutDetails = workoutDetails
                    )
                }
            }
        }
}
