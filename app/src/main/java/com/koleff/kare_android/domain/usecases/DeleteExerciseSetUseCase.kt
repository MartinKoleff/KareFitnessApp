package com.koleff.kare_android.domain.usecases

import android.util.Log
import com.koleff.kare_android.data.model.dto.ExerciseDetailsDto
import com.koleff.kare_android.data.model.dto.ExerciseSetDto
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.state.ExerciseState
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.repository.ExerciseRepository
import com.koleff.kare_android.ui.state.ExerciseDetailsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.util.UUID

class DeleteExerciseSetUseCase(private val exerciseRepository: ExerciseRepository) {

    suspend operator fun invoke(
        exerciseId: Int,
        workoutId: Int,
        setId: UUID?,
        currentSets: List<ExerciseSetDto>
    ): Flow<ExerciseState> =
        setId?.let {
            exerciseRepository.deleteExerciseSet(exerciseId, workoutId, setId, currentSets).map { apiResult ->
                when (apiResult) {
                    is ResultWrapper.ApiError -> ExerciseState(
                        isError = true,
                        error = apiResult.error ?: KareError.GENERIC
                    )

                    is ResultWrapper.Loading -> ExerciseState(isLoading = true)
                    is ResultWrapper.Success -> {
                        Log.d(
                            "DeleteExerciseSetUseCase",
                            "Exercise set with id $setId successfully deleted!"
                        )

                        ExerciseState(
                            isSuccessful = true,
                            exercise = apiResult.data.exercise
                        )
                    }
                }
            }
        } ?: run {
            flowOf(
                ExerciseState(
                    isError = true,
                    error = KareError.EXERCISE_SET_NOT_FOUND
                )
            )
        }
}
