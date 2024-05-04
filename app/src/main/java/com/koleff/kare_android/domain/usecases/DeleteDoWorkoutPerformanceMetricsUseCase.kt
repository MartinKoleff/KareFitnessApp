package com.koleff.kare_android.domain.usecases

import android.util.Log
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.repository.DoWorkoutPerformanceMetricsRepository
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.ui.state.BaseState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DeleteDoWorkoutPerformanceMetricsUseCase(private val repository: DoWorkoutPerformanceMetricsRepository) {

    suspend operator fun invoke(
        performanceMetricsId: Int
    ): Flow<BaseState> =
        repository.deleteDoWorkoutPerformanceMetrics(performanceMetricsId)
            .map { apiResult ->
                when (apiResult) {
                    is ResultWrapper.ApiError -> BaseState(
                        isError = true,
                        error = apiResult.error ?: KareError.GENERIC
                    )

                    is ResultWrapper.Loading -> BaseState(isLoading = true)
                    is ResultWrapper.Success -> {
                        Log.d(
                            "DeleteDoWorkoutPerformanceMetricsUseCase",
                            "Do workout performance metrics with id $performanceMetricsId successfully deleted!"
                        )

                        BaseState(
                            isSuccessful = true
                        )
                    }
                }
            }
}
