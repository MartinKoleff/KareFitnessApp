package com.koleff.kare_android.domain.usecases.statistics

import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.repository.ExerciseStatisticsRepository
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.ui.state.ExerciseTotalSetsPerformedState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetExerciseTotalSetsPerformedUseCase(private val exerciseStatisticsRepository: ExerciseStatisticsRepository) {
    suspend operator fun invoke(exerciseId: Int): Flow<ExerciseTotalSetsPerformedState> =
        exerciseStatisticsRepository.getTotalSetsPerformed(exerciseId).map { apiResult ->
            when (apiResult) {
                is ResultWrapper.ApiError -> {
                    ExerciseTotalSetsPerformedState(
                        isError = true,
                        error = apiResult.error ?: KareError.GENERIC
                    )
                }

                is ResultWrapper.Loading -> {
                    ExerciseTotalSetsPerformedState(isLoading = true)
                }

                is ResultWrapper.Success -> {
                    ExerciseTotalSetsPerformedState(
                        isSuccessful = true,
                        totalSets = apiResult.data.totalSets
                    )
                }
            }
        }
}
