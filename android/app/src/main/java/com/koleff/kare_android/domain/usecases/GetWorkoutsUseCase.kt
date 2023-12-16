package com.koleff.kare_android.domain.usecases

import android.util.Log
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.data.model.state.WorkoutState
import com.koleff.kare_android.data.model.wrapper.GetAllWorkoutsWrapper
import com.koleff.kare_android.data.model.wrapper.ResultWrapper
import com.koleff.kare_android.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetWorkoutsUseCase(private val workoutRepository: WorkoutRepository) {

    suspend operator fun invoke(): Flow<WorkoutState> = workoutRepository.getAllWorkouts().map { apiResult ->
        when (apiResult) {
            is ResultWrapper.ApiError -> WorkoutState(
                isError = true,
                error = apiResult.error ?: KareError.GENERIC
            )

            is ResultWrapper.Loading -> WorkoutState(isLoading = true)

            is ResultWrapper.Success -> {
                WorkoutState(
                    isSuccessful = true,
                    workoutList = apiResult.data.workouts
                )
            }
        }
    }
}