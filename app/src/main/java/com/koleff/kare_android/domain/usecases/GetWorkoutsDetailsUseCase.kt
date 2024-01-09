package com.koleff.kare_android.domain.usecases

import android.util.Log
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.state.WorkoutDetailsState
import com.koleff.kare_android.ui.state.WorkoutState
import com.koleff.kare_android.domain.wrapper.GetAllWorkoutsWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetWorkoutsDetailsUseCase(private val workoutRepository: WorkoutRepository) {

    suspend operator fun invoke(workoutId: Int): Flow<WorkoutDetailsState> =
        workoutRepository.getWorkoutDetails(workoutId).map { apiResult ->
            when (apiResult) {
                is ResultWrapper.ApiError -> WorkoutDetailsState(
                    isError = true,
                    error = apiResult.error ?: KareError.GENERIC
                )

                is ResultWrapper.Loading -> WorkoutDetailsState(isLoading = true)

                is ResultWrapper.Success -> {
                    Log.d("GetWorkoutsDetailsUseCase", "Workout with id ${apiResult.data.workoutDetails.workoutId} details fetched.")

                    WorkoutDetailsState(
                        isSuccessful = true,
                        workout = apiResult.data.workoutDetails
                    )
                }
            }
        }
}
