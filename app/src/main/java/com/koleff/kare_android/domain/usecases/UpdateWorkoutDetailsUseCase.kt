package com.koleff.kare_android.domain.usecases

import android.util.Log
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.state.WorkoutDetailsState
import com.koleff.kare_android.ui.state.WorkoutListState
import com.koleff.kare_android.domain.wrapper.WorkoutListWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.repository.WorkoutRepository
import com.koleff.kare_android.ui.state.WorkoutState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UpdateWorkoutDetailsUseCase(private val workoutRepository: WorkoutRepository) {

    suspend operator fun invoke(workoutDetails: WorkoutDetailsDto): Flow<WorkoutDetailsState> =
        workoutRepository.updateWorkoutDetails(workoutDetails).map { apiResult ->
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
                    Log.d(
                        "UpdateWorkoutUseCase",
                        "Workout details with id ${workoutDetails.workoutId} updated."
                    )

                    WorkoutDetailsState(
                        isSuccessful = true,
                        workoutDetails = workoutDetails
                    )
                }
            }
        }
}
