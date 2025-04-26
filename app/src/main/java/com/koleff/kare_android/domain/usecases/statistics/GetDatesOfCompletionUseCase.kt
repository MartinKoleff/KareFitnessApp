package com.koleff.kare_android.domain.usecases.statistics

import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.repository.WorkoutStatisticsRepository
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.ui.state.DatesOfCompletionState
import com.koleff.kare_android.ui.state.TotalTimesCompletedState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetDatesOfCompletionUseCase(private val workoutStatisticsRepository: WorkoutStatisticsRepository) {
    suspend operator fun invoke(workoutId: Int): Flow<DatesOfCompletionState> =
        workoutStatisticsRepository.getDatesOfCompletionForWorkout(workoutId).map { apiResult ->
            when (apiResult) {
                is ResultWrapper.ApiError -> {
                    DatesOfCompletionState(
                        isError = true,
                        error = apiResult.error ?: KareError.GENERIC
                    )
                }

                is ResultWrapper.Loading -> {
                    DatesOfCompletionState(isLoading = true)
                }

                is ResultWrapper.Success -> {
                    DatesOfCompletionState(
                        isSuccessful = true,
                        datesOfCompletion = apiResult.data.dates
                    )
                }
            }
        }
}
