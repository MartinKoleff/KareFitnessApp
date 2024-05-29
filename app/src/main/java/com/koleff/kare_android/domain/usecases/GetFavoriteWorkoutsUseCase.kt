package com.koleff.kare_android.domain.usecases

import android.util.Log
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.state.WorkoutListState
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetFavoriteWorkoutsUseCase(private val workoutRepository: WorkoutRepository) {

    suspend operator fun invoke(): Flow<WorkoutListState> =
        workoutRepository.getFavoriteWorkouts().map { apiResult ->
            when (apiResult) {
                is ResultWrapper.ApiError -> {
                    WorkoutListState(
                        isError = true,
                        error = apiResult.error ?: KareError.GENERIC
                    )
                }

                is ResultWrapper.Loading -> {
                    WorkoutListState(isLoading = true)
                }

                is ResultWrapper.Success -> {
                    Log.d("GetFavoriteWorkoutsUseCase", "Favorited workouts fetched: ${apiResult.data.workouts}")

                    WorkoutListState(
                        isSuccessful = true,
                        workoutList = apiResult.data.workouts
                    )
                }
            }
        }
}

