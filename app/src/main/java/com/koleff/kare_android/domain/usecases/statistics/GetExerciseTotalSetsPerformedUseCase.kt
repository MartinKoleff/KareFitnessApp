package com.koleff.kare_android.domain.usecases.statistics

import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.repository.ExerciseStatisticsRepository
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.ui.state.TotalSetsPerformedState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetExerciseTotalSetsPerformedUseCase(private val exerciseStatisticsRepository: ExerciseStatisticsRepository) {
    suspend operator fun invoke(exerciseId: Int): Flow<TotalSetsPerformedState> =
        exerciseStatisticsRepository.getTotalSetsPerformed(exerciseId).map { apiResult ->
            when (apiResult) {
                is ResultWrapper.ApiError -> {
                    TotalSetsPerformedState(
                        isError = true,
                        error = apiResult.error ?: KareError.GENERIC
                    )
                }

                is ResultWrapper.Loading -> {
                    TotalSetsPerformedState(isLoading = true)
                }

                is ResultWrapper.Success -> {
                    TotalSetsPerformedState(
                        isSuccessful = true,
                        totalSets = apiResult.data.totalSets
                    )
                }
            }
        }
}
