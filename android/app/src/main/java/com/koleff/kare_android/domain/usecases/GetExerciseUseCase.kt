package com.koleff.kare_android.domain.usecases

import android.util.Log
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.state.ExerciseState
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetExerciseUseCase(private val exerciseRepository: ExerciseRepository) {

    suspend operator fun invoke(exerciseId: Int): Flow<ExerciseState> =
        exerciseRepository.getExercise(exerciseId).map { apiResult ->
            when (apiResult) {
                is ResultWrapper.ApiError -> ExerciseState(
                    isError = true,
                    error = apiResult.error ?: KareError.GENERIC
                )

                is ResultWrapper.Loading -> ExerciseState(isLoading = true)

                is ResultWrapper.Success -> {
                    Log.d("GetExercisesUseCase", "Exercise with id ${apiResult.data.exercise.exerciseId} fetched.")

                    ExerciseState(
                        isSuccessful = true,
                        exercise = apiResult.data.exercise
                    )
                }
            }
        }
}