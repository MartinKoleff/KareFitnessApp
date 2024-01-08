package com.koleff.kare_android.domain.usecases

import android.util.Log
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.state.ExercisesState
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetExercisesUseCase(private val exerciseRepository: ExerciseRepository) {

    suspend operator fun invoke(muscleGroupId: Int): Flow<ExercisesState> =
        exerciseRepository.getExercises(muscleGroupId).map { apiResult ->
            when (apiResult) {
                is ResultWrapper.ApiError -> ExercisesState(
                    isError = true,
                    error = apiResult.error ?: KareError.GENERIC
                )

                is ResultWrapper.Loading -> ExercisesState(isLoading = true)

                is ResultWrapper.Success -> {
                    Log.d("GetExercisesUseCase", "Exercises fetched.")

                    ExercisesState(
                        isSuccessful = true,
                        exerciseList = apiResult.data.exercises
                    )
                }
            }
        }
}