package com.koleff.kare_android.domain.usecases

import android.util.Log
import com.koleff.kare_android.data.model.dto.DoWorkoutPerformanceMetricsDto
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.data.room.entity.DoWorkoutPerformanceMetrics
import com.koleff.kare_android.domain.repository.DoWorkoutPerformanceMetricsRepository
import com.koleff.kare_android.ui.state.WorkoutDetailsState
import com.koleff.kare_android.ui.state.WorkoutListState
import com.koleff.kare_android.domain.wrapper.WorkoutListWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.repository.WorkoutRepository
import com.koleff.kare_android.ui.state.WorkoutPerformanceMetricsState
import com.koleff.kare_android.ui.state.WorkoutState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UpdateDoWorkoutPerformanceMetricsUseCase(private val repository: DoWorkoutPerformanceMetricsRepository) {

    suspend operator fun invoke(performanceMetrics: DoWorkoutPerformanceMetricsDto): Flow<WorkoutPerformanceMetricsState> =
        repository.updateDoWorkoutPerformanceMetrics(performanceMetrics).map { apiResult ->
            when (apiResult) {
                is ResultWrapper.ApiError -> {
                    WorkoutPerformanceMetricsState(
                        isError = true,
                        error = apiResult.error ?: KareError.GENERIC
                    )
                }

                is ResultWrapper.Loading -> {
                    WorkoutPerformanceMetricsState(isLoading = true)
                }

                is ResultWrapper.Success -> {
                    Log.d(
                        "UpdateDoWorkoutPerformanceMetricsUseCase",
                        "Do workout performance metrics with id ${performanceMetrics.id} updated."
                    )

                    WorkoutPerformanceMetricsState(
                        isSuccessful = true,
                        doWorkoutPerformanceMetrics = performanceMetrics
                    )
                }
            }
        }
}
