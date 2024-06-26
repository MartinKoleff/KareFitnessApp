package com.koleff.kare_android.domain.usecases

import android.util.Log
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.state.ExerciseListState
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetCatalogExercisesUseCase(private val exerciseRepository: ExerciseRepository) {

    suspend operator fun invoke(muscleGroupId: Int): Flow<ExerciseListState> =
        exerciseRepository.getCatalogExercises(muscleGroupId).map { apiResult ->
            when (apiResult) {
                is ResultWrapper.ApiError -> ExerciseListState(
                    isError = true,
                    error = apiResult.error ?: KareError.GENERIC
                )

                is ResultWrapper.Loading -> ExerciseListState(isLoading = true)

                is ResultWrapper.Success -> {
                    Log.d("GetCatalogExercisesUseCase", "Exercises fetched for muscle group ${MuscleGroup.fromId(muscleGroupId).name}.")

                    ExerciseListState(
                        isSuccessful = true,
                        exerciseList = apiResult.data.exercises
                    )
                }
            }
        }
}