package com.koleff.kare_android.domain.usecases.statistics

import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.repository.GeneralStatisticsRepository
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.ui.state.WorkoutState
import com.koleff.kare_android.ui.state.WorkoutStreakState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetMostFrequentWorkoutUseCase(private val generalStatisticsRepository: GeneralStatisticsRepository) {
    suspend operator fun invoke(): Flow<WorkoutState> =
        generalStatisticsRepository.getMostFrequentWorkout().map { apiResult ->
            when (apiResult) {
                is ResultWrapper.ApiError -> {
                    WorkoutState(
                        isError = true,
                        error = apiResult.error ?: KareError.GENERIC
                    )
                }

                is ResultWrapper.Loading -> {
                    WorkoutState(isLoading = true)
                }

                is ResultWrapper.Success -> {
                    WorkoutState(
                        isSuccessful = true,
                        workout = apiResult.data.workout
                    )
                }
            }
        }
}
