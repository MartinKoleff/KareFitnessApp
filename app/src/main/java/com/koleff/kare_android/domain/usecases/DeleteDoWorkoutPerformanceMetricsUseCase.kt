package com.koleff.kare_android.domain.usecases

import android.util.Log
import com.koleff.kare_android.data.model.dto.DoWorkoutPerformanceMetricsDto
import com.koleff.kare_android.data.model.dto.ExerciseDetailsDto
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.repository.DoWorkoutPerformanceMetricsRepository
import com.koleff.kare_android.ui.state.ExerciseState
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.repository.ExerciseRepository
import com.koleff.kare_android.ui.state.BaseState
import com.koleff.kare_android.ui.state.ExerciseDetailsState
import com.koleff.kare_android.ui.state.WorkoutPerformanceMetricsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.util.UUID

class DeleteDoWorkoutPerformanceMetricsUseCase(private val repository: DoWorkoutPerformanceMetricsRepository) {

    suspend operator fun invoke(
        performanceMetrics: DoWorkoutPerformanceMetricsDto
    ): Flow<BaseState> =
        repository.deleteDoWorkoutPerformanceMetrics(performanceMetrics.id)
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
                            "Do workout performance metrics with id ${performanceMetrics.id} successfully deleted!"
                        )

                        BaseState(
                            isSuccessful = true
                        )
                    }
                }
            }
}
