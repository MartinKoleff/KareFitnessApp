package com.koleff.kare_android.domain.usecases

import android.util.Log
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.state.WorkoutDetailsState
import com.koleff.kare_android.ui.state.WorkoutListState
import com.koleff.kare_android.domain.wrapper.GetAllWorkoutsWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AddExerciseUseCase(private val workoutRepository: WorkoutRepository) {

    suspend operator fun invoke(workoutId: Int, exerciseId: Int): Flow<WorkoutDetailsState> =
        workoutRepository.addExercise(workoutId, exerciseId).map { apiResult ->
            when (apiResult) {
                is ResultWrapper.ApiError -> {
                    WorkoutDetailsState(
                        isError = true,
                        error = apiResult.error ?: KareError.GENERIC
                    )
                }

                is ResultWrapper.Loading -> {
                    WorkoutDetailsState(isLoading = true)
                }

                is ResultWrapper.Success -> {
                    Log.d("AddExerciseUseCase", "Workout with id $workoutId updated. Exercise with id $exerciseId added.")

                    WorkoutDetailsState(
                        isSuccessful = true,
                        workout = apiResult.data.workoutDetails
                    )
                }
            }
        }
}
