package com.koleff.kare_android.domain.usecases

import android.util.Log
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.repository.WorkoutRepository
import com.koleff.kare_android.ui.state.UpdateWorkoutState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CreateNewWorkoutUseCase(private val workoutRepository: WorkoutRepository) {

    suspend operator fun invoke(): Flow<UpdateWorkoutState> =
        workoutRepository.createNewWorkout().map { apiResult ->
            when (apiResult) {
                is ResultWrapper.ApiError -> {
                    UpdateWorkoutState(
                        isError = true,
                        error = apiResult.error ?: KareError.GENERIC
                    )
                }

                is ResultWrapper.Loading -> {
                    UpdateWorkoutState(isLoading = true)
                }

                is ResultWrapper.Success -> {
                    val workout = apiResult.data.workout

                    Log.d(
                        "CreateNewWorkoutUseCase-createNewWorkout",
                        "Workout details with id ${workout.workoutId} created."
                    )

                    UpdateWorkoutState(
                        isSuccessful = true,
                        workout = workout
                    )
                }
            }
        }
}
