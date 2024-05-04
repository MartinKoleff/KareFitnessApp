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

class GetAllDoWorkoutPerformanceMetricsUseCase(private val repository: DoWorkoutPerformanceMetricsRepository) {
    suspend operator fun invoke(): Flow<WorkoutPerformanceMetricsListState> =
        repository.getAllDoWorkoutPerformanceMetrics().map { apiResult ->
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
                        "GetAllDoWorkoutPerformanceMetricsUseCase",
                        "All workout performance metrics fetched. Data: ${apiResult.data.data}"
                    )

                    WorkoutPerformanceMetricsListState(
                        isSuccessful = true,
                        doWorkoutPerformanceMetricsList = apiResult.data.data
                    )
                }
            }
        }

    suspend operator fun invoke(start: Date, end: Date): Flow<WorkoutPerformanceMetricsListState> =
        repository.getAllDoWorkoutPerformanceMetrics(start, end).map { apiResult ->
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
                        "GetAllDoWorkoutPerformanceMetricsUseCase",
                        "All workout performance metrics fetched for the period between $start and $end. Data: ${apiResult.data.data}"
                    )

                    WorkoutPerformanceMetricsListState(
                        isSuccessful = true,
                        doWorkoutPerformanceMetricsList = apiResult.data.data
                    )
                }
            }
        }
}
