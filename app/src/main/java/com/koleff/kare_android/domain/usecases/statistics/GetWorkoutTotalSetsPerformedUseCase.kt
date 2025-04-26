package com.koleff.kare_android.domain.usecases.statistics

import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.repository.WorkoutStatisticsRepository
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.ui.state.TotalSetsPerformedState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetWorkoutTotalSetsPerformedUseCase(private val workoutStatisticsRepository: WorkoutStatisticsRepository) {
    suspend operator fun invoke(workoutId: Int): Flow<TotalSetsPerformedState> =
        workoutStatisticsRepository.getTotalSetsPerformed(
            workoutId = workoutId
        ).map { apiResult ->
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
