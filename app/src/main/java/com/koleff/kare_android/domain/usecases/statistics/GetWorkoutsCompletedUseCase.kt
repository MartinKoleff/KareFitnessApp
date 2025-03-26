package com.koleff.kare_android.domain.usecases.statistics

import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.repository.GeneralStatisticsRepository
import com.koleff.kare_android.domain.repository.WorkoutStatisticsRepository
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.TotalTimesCompletedWrapper
import com.koleff.kare_android.ui.state.TotalRepsPerformedState
import com.koleff.kare_android.ui.state.TotalTimesCompletedState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetWorkoutsCompletedUseCase(private val generalStatisticsRepository: GeneralStatisticsRepository) {
    suspend operator fun invoke(): Flow<TotalTimesCompletedState> =
        generalStatisticsRepository.getWorkoutsCompleted().map { apiResult ->
            when (apiResult) {
                is ResultWrapper.ApiError -> {
                    TotalTimesCompletedState(
                        isError = true,
                        error = apiResult.error ?: KareError.GENERIC
                    )
                }

                is ResultWrapper.Loading -> {
                    TotalTimesCompletedState(isLoading = true)
                }

                is ResultWrapper.Success -> {
                    TotalTimesCompletedState(
                        isSuccessful = true,
                        totalTimesCompleted = apiResult.data.totalTimesCompleted
                    )
                }
            }
        }
}
