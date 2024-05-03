package com.koleff.kare_android.domain.usecases

import android.util.Log
import com.koleff.kare_android.data.model.dto.DoWorkoutPerformanceMetricsDto
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.repository.DoWorkoutPerformanceMetricsRepository
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.ui.state.BaseState
import com.koleff.kare_android.ui.state.DoWorkoutPerformanceMetricsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SaveDoWorkoutPerformanceMetricsUseCase(private val repository: DoWorkoutPerformanceMetricsRepository) {
    suspend operator fun invoke(performanceMetrics: DoWorkoutPerformanceMetricsDto): Flow<DoWorkoutPerformanceMetricsState> =
        repository.saveDoWorkoutPerformanceMetrics(performanceMetrics).map { apiResult ->
            when (apiResult) {
                is ResultWrapper.ApiError -> {
                    DoWorkoutPerformanceMetricsState(
                        isError = true,
                        error = apiResult.error ?: KareError.GENERIC
                    )
                }

                is ResultWrapper.Loading -> {
                    DoWorkoutPerformanceMetricsState(isLoading = true)
                }

                is ResultWrapper.Success -> {
                    Log.d(
                        "SaveDoWorkoutPerformanceMetricsUseCase",
                        "Workout performance metrics saved! Performance metrics: ${apiResult.data.doWorkoutPerformanceMetrics}"
                    )

                    DoWorkoutPerformanceMetricsState(
                        isSuccessful = true,
                        doWorkoutPerformanceMetrics = apiResult.data.doWorkoutPerformanceMetrics
                    )
                }
            }
        }
}
