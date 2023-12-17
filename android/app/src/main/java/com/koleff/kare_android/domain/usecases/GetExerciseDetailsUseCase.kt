package com.koleff.kare_android.domain.usecases

import android.util.Log
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.state.ExerciseDetailsState
import com.koleff.kare_android.ui.state.ExercisesState
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetExerciseDetailsUseCase(private val exerciseRepository: ExerciseRepository) {

    suspend operator fun invoke(exerciseId: Int): Flow<ExerciseDetailsState> =
        exerciseRepository.getExerciseDetails(exerciseId).map { apiResult ->
            when (apiResult) {
                is ResultWrapper.ApiError -> ExerciseDetailsState(
                    isError = true,
                    error = apiResult.error ?: KareError.GENERIC
                )

                is ResultWrapper.Loading -> ExerciseDetailsState(isLoading = true)

                is ResultWrapper.Success -> {
                    Log.d("GetExerciseDetailsUseCase", "Exercise with id ${apiResult.data.exerciseDetails.id} details fetched.")

                    ExerciseDetailsState(
                        isSuccessful = true,
                        exercise = apiResult.data.exerciseDetails
                    )
                }
            }
        }
}