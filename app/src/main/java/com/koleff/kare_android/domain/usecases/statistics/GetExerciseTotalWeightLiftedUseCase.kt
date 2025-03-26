package com.koleff.kare_android.domain.usecases.statistics

import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.repository.ExerciseStatisticsRepository
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.ui.state.ExerciseTotalRepsPerformedState
import com.koleff.kare_android.ui.state.ExerciseTotalSetsPerformedState
import com.koleff.kare_android.ui.state.ExerciseTotalWeightLiftedState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetExerciseTotalWeightLiftedUseCase(private val exerciseStatisticsRepository: ExerciseStatisticsRepository) {
    suspend operator fun invoke(exerciseId: Int): Flow<ExerciseTotalWeightLiftedState> =
        exerciseStatisticsRepository.getTotalWeightLifted(exerciseId).map { apiResult ->
            when (apiResult) {
                is ResultWrapper.ApiError -> {
                    ExerciseTotalWeightLiftedState(
                        isError = true,
                        error = apiResult.error ?: KareError.GENERIC
                    )
                }

                is ResultWrapper.Loading -> {
                    ExerciseTotalWeightLiftedState(isLoading = true)
                }

                is ResultWrapper.Success -> {
                    ExerciseTotalWeightLiftedState(
                        isSuccessful = true,
                        totalWeight = apiResult.data.totalWeight
                    )
                }
            }
        }
}
