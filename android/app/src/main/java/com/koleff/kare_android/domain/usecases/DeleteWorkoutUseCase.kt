package com.koleff.kare_android.domain.usecases

import android.util.Log
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.state.WorkoutDetailsState
import com.koleff.kare_android.ui.state.WorkoutState
import com.koleff.kare_android.domain.wrapper.GetAllWorkoutsWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.repository.WorkoutRepository
import com.koleff.kare_android.ui.state.DeleteWorkoutState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DeleteWorkoutUseCase(private val workoutRepository: WorkoutRepository) {

    suspend operator fun invoke(workoutId: Int): Flow<DeleteWorkoutState> =
        workoutRepository.deleteWorkout(workoutId).map { apiResult ->
            when (apiResult) {
                is ResultWrapper.ApiError -> {
                    DeleteWorkoutState(
                        isError = true,
                        error = apiResult.error ?: KareError.GENERIC
                    )
                }

                is ResultWrapper.Loading -> {
                    DeleteWorkoutState(isLoading = true)
                }

                is ResultWrapper.Success -> {
                    Log.d("DeleteWorkoutUseCase", "Workout with id $workoutId deleted.")

                    DeleteWorkoutState(
                        isSuccessful = true
                    )
                }
            }
        }
}
