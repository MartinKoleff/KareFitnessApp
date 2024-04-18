package com.koleff.kare_android.domain.wrapper

import com.koleff.kare_android.data.model.response.DoWorkoutPerformanceMetricsResponse
import com.koleff.kare_android.domain.wrapper.ServerResponseData

class DoWorkoutPerformanceMetricsWrapper(doWorkoutPerformanceMetricsResponse: DoWorkoutPerformanceMetricsResponse) :
    ServerResponseData(doWorkoutPerformanceMetricsResponse) {
    val data = doWorkoutPerformanceMetricsResponse.data
}
