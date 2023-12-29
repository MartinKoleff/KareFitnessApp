package com.koleff.kare_android.domain.usecases

import android.util.Log
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.state.WorkoutState
import com.koleff.kare_android.domain.wrapper.GetAllWorkoutsWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetWorkoutsUseCase(private val workoutRepository: WorkoutRepository) {

    suspend operator fun invoke(silentFetch: Boolean = false): Flow<WorkoutState> =
        workoutRepository.getAllWorkouts().map { apiResult ->
            when (apiResult) {
                is ResultWrapper.ApiError -> WorkoutState(
                    isError = true,
                    error = apiResult.error ?: KareError.GENERIC
                )

                is ResultWrapper.Loading -> WorkoutState(isLoading = !silentFetch)

                is ResultWrapper.Success -> {
                    Log.d("GetWorkoutsUseCase", "Workouts fetched.")

                    WorkoutState(
                        isSuccessful = true,
                        workoutList = apiResult.data.workouts
                    )
                }
            }
        }
}