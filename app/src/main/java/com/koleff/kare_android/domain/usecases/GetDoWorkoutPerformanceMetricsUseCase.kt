package com.koleff.kare_android.domain.usecases

import android.util.Log
import com.koleff.kare_android.data.model.dto.DoWorkoutPerformanceMetricsDto
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.repository.DoWorkoutPerformanceMetricsRepository
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.ui.state.BaseState
import com.koleff.kare_android.ui.state.WorkoutPerformanceMetricsListState
import com.koleff.kare_android.ui.state.WorkoutPerformanceMetricsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date

class GetDoWorkoutPerformanceMetricsUseCase(private val repository: DoWorkoutPerformanceMetricsRepository) {
    suspend operator fun invoke(workoutId: Int): Flow<WorkoutPerformanceMetricsListState> =
        repository.getDoWorkoutPerformanceMetricsByWorkoutId(workoutId).map { apiResult ->
            when (apiResult) {
                is ResultWrapper.ApiError -> {
                    WorkoutPerformanceMetricsListState(
                        isError = true,
                        error = apiResult.error ?: KareError.GENERIC
                    )
                }

                is ResultWrapper.Loading -> {
                    WorkoutPerformanceMetricsListState(isLoading = true)
                }

                is ResultWrapper.Success -> {
                    Log.d(
                        "GetDoWorkoutPerformanceMetricsUseCase",
                        "Workout performance metrics fetched for workoutId $workoutId. Data: ${apiResult.data.data}"
                    )

                    WorkoutPerformanceMetricsListState(
                        isSuccessful = true,
                        doWorkoutPerformanceMetricsList = apiResult.data.data
                    )
                }
            }
        }

    suspend operator fun invoke(workoutId: Int, start: Date, end: Date): Flow<WorkoutPerformanceMetricsListState> =
        repository.getAllDoWorkoutPerformanceMetricsByWorkoutId(workoutId, start, end).map { apiResult ->
            when (apiResult) {
                is ResultWrapper.ApiError -> {
                    WorkoutPerformanceMetricsListState(
                        isError = true,
                        error = apiResult.error ?: KareError.GENERIC
                    )
                }

                is ResultWrapper.Loading -> {
                    WorkoutPerformanceMetricsListState(isLoading = true)
                }

                is ResultWrapper.Success -> {
                    Log.d(
                        "GetDoWorkoutPerformanceMetricsUseCase",
                        "Workout performance metrics fetched for workoutId $workoutId for the period between $start and $end. Data: ${apiResult.data.data}"
                    )

                    WorkoutPerformanceMetricsListState(
                        isSuccessful = true,
                        doWorkoutPerformanceMetricsList = apiResult.data.data
                    )
                }
            }
        }
}
