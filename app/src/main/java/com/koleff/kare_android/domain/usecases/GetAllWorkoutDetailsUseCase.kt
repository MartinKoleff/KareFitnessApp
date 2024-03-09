package com.koleff.kare_android.domain.usecases

import android.util.Log
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.state.WorkoutListState
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.repository.WorkoutRepository
import com.koleff.kare_android.ui.state.WorkoutDetailsListState
import com.koleff.kare_android.ui.state.WorkoutDetailsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAllWorkoutDetailsUseCase(private val workoutRepository: WorkoutRepository) {

    suspend operator fun invoke(silentFetch: Boolean = false): Flow<WorkoutDetailsListState> =
        workoutRepository.getAllWorkoutDetails().map { apiResult ->
            when (apiResult) {
                is ResultWrapper.ApiError -> WorkoutDetailsListState(
                    isError = true,
                    error = apiResult.error ?: KareError.GENERIC
                )

                is ResultWrapper.Loading -> WorkoutDetailsListState(isLoading = !silentFetch)

                is ResultWrapper.Success -> {
                    Log.d("GetAllWorkoutsUseCase", "Workouts fetched.")

                    WorkoutDetailsListState(
                        isSuccessful = true,
                        workoutDetailsList = apiResult.data.workoutDetailsList
                    )
                }
            }
        }
}