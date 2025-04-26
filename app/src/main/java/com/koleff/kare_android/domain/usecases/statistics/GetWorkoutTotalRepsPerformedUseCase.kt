package com.koleff.kare_android.domain.usecases.statistics

import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.repository.WorkoutStatisticsRepository
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.ui.state.TotalRepsPerformedState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetWorkoutTotalRepsPerformedUseCase(private val workoutStatisticsRepository: WorkoutStatisticsRepository) {
    suspend operator fun invoke(workoutId: Int, exerciseId: Int): Flow<TotalRepsPerformedState> =
        workoutStatisticsRepository.getTotalRepsPerformed(
            workoutId = workoutId,
            exerciseId = exerciseId
        ).map { apiResult ->
            when (apiResult) {
                is ResultWrapper.ApiError -> {
                    TotalRepsPerformedState(
                        isError = true,
                        error = apiResult.error ?: KareError.GENERIC
                    )
                }

                is ResultWrapper.Loading -> {
                    TotalRepsPerformedState(isLoading = true)
                }

                is ResultWrapper.Success -> {
                    TotalRepsPerformedState(
                        isSuccessful = true,
                        totalReps = apiResult.data.totalReps
                    )
                }
            }
        }
}
