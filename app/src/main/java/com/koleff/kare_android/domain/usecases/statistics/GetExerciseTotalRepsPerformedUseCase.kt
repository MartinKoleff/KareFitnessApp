package com.koleff.kare_android.domain.usecases.statistics

import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.repository.ExerciseStatisticsRepository
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.ui.state.ExerciseTotalRepsPerformedState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetExerciseTotalRepsPerformedUseCase(private val exerciseStatisticsRepository: ExerciseStatisticsRepository) {
    suspend operator fun invoke(exerciseId: Int): Flow<ExerciseTotalRepsPerformedState> =
        exerciseStatisticsRepository.getTotalRepsPerformed(exerciseId).map { apiResult ->
            when (apiResult) {
                is ResultWrapper.ApiError -> {
                    ExerciseTotalRepsPerformedState(
                        isError = true,
                        error = apiResult.error ?: KareError.GENERIC
                    )
                }

                is ResultWrapper.Loading -> {
                    ExerciseTotalRepsPerformedState(isLoading = true)
                }

                is ResultWrapper.Success -> {
                    ExerciseTotalRepsPerformedState(
                        isSuccessful = true,
                        totalReps = apiResult.data.totalReps
                    )
                }
            }
        }
}
