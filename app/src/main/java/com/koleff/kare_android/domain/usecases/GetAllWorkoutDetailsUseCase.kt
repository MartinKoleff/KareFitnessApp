package com.koleff.kare_android.domain.usecases

import android.util.Log
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.state.WorkoutState
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.repository.WorkoutRepository
import com.koleff.kare_android.ui.state.WorkoutDetailsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAllWorkoutDetailsUseCase(private val workoutRepository: WorkoutRepository) {

    suspend operator fun invoke(silentFetch: Boolean = false): Flow<WorkoutDetailsState> =
        workoutRepository.getAllWorkoutDetails().map { apiResult ->
            when (apiResult) {
                is ResultWrapper.ApiError -> WorkoutDetailsState(
                    isError = true,
                    error = apiResult.error ?: KareError.GENERIC
                )

                is ResultWrapper.Loading -> WorkoutDetailsState(isLoading = !silentFetch)

                is ResultWrapper.Success -> {
                    Log.d("GetAllWorkoutsUseCase", "Workouts fetched.")

                    WorkoutDetailsState(
                        isSuccessful = true,
                        workoutDetailsList = apiResult.data.workoutDetailsList
                    )
                }
            }
        }
}