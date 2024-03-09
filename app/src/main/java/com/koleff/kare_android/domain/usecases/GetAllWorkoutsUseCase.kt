package com.koleff.kare_android.domain.usecases

import android.util.Log
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.state.WorkoutListState
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAllWorkoutsUseCase(private val workoutRepository: WorkoutRepository) {

    suspend operator fun invoke(silentFetch: Boolean = false): Flow<WorkoutListState> =
        workoutRepository.getAllWorkouts().map { apiResult ->
            when (apiResult) {
                is ResultWrapper.ApiError -> WorkoutListState(
                    isError = true,
                    error = apiResult.error ?: KareError.GENERIC
                )

                is ResultWrapper.Loading -> WorkoutListState(isLoading = !silentFetch)

                is ResultWrapper.Success -> {
                    Log.d("GetAllWorkoutsUseCase", "Workouts fetched.")

                    WorkoutListState(
                        isSuccessful = true,
                        workoutList = apiResult.data.workouts
                    )
                }
            }
        }
}