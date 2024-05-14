package com.koleff.kare_android.domain.usecases

import android.util.Log
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.state.WorkoutDetailsState
import com.koleff.kare_android.ui.state.WorkoutListState
import com.koleff.kare_android.domain.wrapper.WorkoutListWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AddMultipleExercisesUseCase(private val workoutRepository: WorkoutRepository) {

    suspend operator fun invoke(workoutId: Int, exerciseList: List<ExerciseDto>): Flow<WorkoutDetailsState> =
        workoutRepository.addMultipleExercises(workoutId, exerciseList).map { apiResult ->
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
                    Log.d("AddMultipleExerciseUseCase", "Workout with id $workoutId updated\n. Exercises added. $exerciseList")

                    WorkoutDetailsState(
                        isSuccessful = true,
                        workoutDetails = apiResult.data.workoutDetails
                    )
                }
            }
        }
}
