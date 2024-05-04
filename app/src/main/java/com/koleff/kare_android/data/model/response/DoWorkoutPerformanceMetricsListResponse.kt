package com.koleff.kare_android.data.model.response

import com.koleff.kare_android.data.model.dto.DoWorkoutPerformanceMetricsDto
import com.koleff.kare_android.data.model.response.base_response.BaseResponse
import com.squareup.moshi.Json

data class DoWorkoutPerformanceMetricsListResponse(
    @Json(name = "data")
    val data: List<DoWorkoutPerformanceMetricsDto>
): BaseResponse()