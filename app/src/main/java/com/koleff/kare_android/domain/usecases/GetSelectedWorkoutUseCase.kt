package com.koleff.kare_android.domain.usecases

import android.util.Log
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.state.WorkoutState
import com.koleff.kare_android.domain.wrapper.GetAllWorkoutsWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.repository.WorkoutRepository
import com.koleff.kare_android.ui.state.SelectedWorkoutState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetSelectedWorkoutUseCase(private val workoutRepository: WorkoutRepository) {

    suspend operator fun invoke(): Flow<SelectedWorkoutState> =
        workoutRepository.getSelectedWorkout().map { apiResult ->
            when (apiResult) {
                is ResultWrapper.ApiError -> {
                    SelectedWorkoutState(
                        isError = true,
                        error = apiResult.error ?: KareError.GENERIC
                    )
                }

                is ResultWrapper.Loading -> {
                    SelectedWorkoutState(isLoading = true)
                }

                is ResultWrapper.Success -> {
                    Log.d("GetSelectedWorkoutUseCase", "Selected workout fetched: ${apiResult.data.workout}") //TODO: if workout is selected -> deselect

                    SelectedWorkoutState(
                        isSuccessful = true,
                        selectedWorkout = apiResult.data.workout
                    )
                }
            }
        }
}

