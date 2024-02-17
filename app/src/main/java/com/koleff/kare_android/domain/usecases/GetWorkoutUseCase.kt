package com.koleff.kare_android.domain.usecases

import android.util.Log
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.state.WorkoutListState
import com.koleff.kare_android.domain.wrapper.GetAllWorkoutsWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetWorkoutUseCase(private val workoutRepository: WorkoutRepository) {

    suspend operator fun invoke(workoutId: Int): Flow<WorkoutListState> =
        workoutRepository.getWorkout(workoutId).map { apiResult ->
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
                    Log.d("GetWorkoutUseCase", "Workout with id $workoutId fetched.")

                    WorkoutListState(
                        isSuccessful = true,
                        workoutList = listOf(apiResult.data.workout)
                    )
                }
            }
        }
}

