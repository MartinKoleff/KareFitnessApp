package com.koleff.kare_android.domain.usecases.statistics

import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.repository.ExerciseStatisticsRepository
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.ui.state.TotalWeightLiftedState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetExerciseTotalWeightLiftedUseCase(private val exerciseStatisticsRepository: ExerciseStatisticsRepository) {
    suspend operator fun invoke(exerciseId: Int): Flow<TotalWeightLiftedState> =
        exerciseStatisticsRepository.getTotalWeightLifted(exerciseId).map { apiResult ->
            when (apiResult) {
                is ResultWrapper.ApiError -> {
                    TotalWeightLiftedState(
                        isError = true,
                        error = apiResult.error ?: KareError.GENERIC
                    )
                }

                is ResultWrapper.Loading -> {
                    TotalWeightLiftedState(isLoading = true)
                }

                is ResultWrapper.Success -> {
                    TotalWeightLiftedState(
                        isSuccessful = true,
                        totalWeight = apiResult.data.totalWeight
                    )
                }
            }
        }
}
