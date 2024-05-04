package com.koleff.kare_android.domain.usecases

import android.util.Log
import com.koleff.kare_android.data.model.dto.ExerciseDetailsDto
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.state.ExerciseState
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.repository.ExerciseRepository
import com.koleff.kare_android.ui.state.ExerciseDetailsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class AddNewExerciseSetUseCase(private val exerciseRepository: ExerciseRepository) {

    suspend operator fun invoke(exerciseId: Int, workoutId: Int): Flow<ExerciseState> =
        exerciseRepository.addNewExerciseSet(exerciseId, workoutId).map { apiResult ->
            when (apiResult) {
                is ResultWrapper.ApiError -> ExerciseState(
                    isError = true,
                    error = apiResult.error ?: KareError.GENERIC
                )

                is ResultWrapper.Loading -> ExerciseState(isLoading = true)
                is ResultWrapper.Success -> {
                    Log.d(
                        "DeleteExerciseSetUseCase",
                        "New exercise set successfully added to exercise with id $exerciseId.\nExercise sets: ${apiResult.data.exercise.sets}"
                    )

                    ExerciseState(
                        isSuccessful = true,
                        exercise = apiResult.data.exercise
                    )
                }
            }
        }
}