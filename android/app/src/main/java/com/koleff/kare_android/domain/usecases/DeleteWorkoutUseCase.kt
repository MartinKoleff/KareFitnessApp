package com.koleff.kare_android.domain.usecases

import android.util.Log
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.state.WorkoutDetailsState
import com.koleff.kare_android.ui.state.WorkoutState
import com.koleff.kare_android.domain.wrapper.GetAllWorkoutsWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.repository.WorkoutRepository
import com.koleff.kare_android.ui.state.BaseState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DeleteWorkoutUseCase(private val workoutRepository: WorkoutRepository) {

    suspend operator fun invoke(workoutId: Int): Flow<BaseState> =
        workoutRepository.deleteWorkout(workoutId).map { apiResult ->
            when (apiResult) {
                is ResultWrapper.ApiError -> {
                    BaseState(
                        isError = true,
                        error = apiResult.error ?: KareError.GENERIC
                    )
                }

                is ResultWrapper.Loading -> {
                    BaseState(isLoading = true)
                }

                is ResultWrapper.Success -> {
                    Log.d("DeleteWorkoutUseCase", "Workout with id $workoutId deleted.")

                    BaseState(
                        isSuccessful = true
                    )
                }
            }
        }
}
