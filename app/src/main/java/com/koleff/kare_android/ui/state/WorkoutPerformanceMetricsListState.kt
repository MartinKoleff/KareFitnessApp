package com.koleff.kare_android.ui.state

import com.koleff.kare_android.data.model.dto.DoWorkoutPerformanceMetricsDto
import com.koleff.kare_android.data.model.response.base_response.KareError

open class WorkoutPerformanceMetricsListState (
    val doWorkoutPerformanceMetricsList: List<DoWorkoutPerformanceMetricsDto> = emptyList(),
    override val isSuccessful: Boolean = false,
    override val isLoading: Boolean = false,
    override val isError: Boolean = false,
    override val error : KareError = KareError.GENERIC
): BaseState(isSuccessful, isLoading, isError, error)