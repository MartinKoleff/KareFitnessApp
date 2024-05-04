package com.koleff.kare_android.domain.wrapper

import com.koleff.kare_android.data.model.response.DoWorkoutPerformanceMetricsListResponse
import com.koleff.kare_android.data.model.response.DoWorkoutPerformanceMetricsResponse
import com.koleff.kare_android.domain.wrapper.ServerResponseData

class DoWorkoutPerformanceMetricsListWrapper(doWorkoutPerformanceMetricsListResponse: DoWorkoutPerformanceMetricsListResponse) :
    ServerResponseData(doWorkoutPerformanceMetricsListResponse) {
    val data = doWorkoutPerformanceMetricsListResponse.data
}
