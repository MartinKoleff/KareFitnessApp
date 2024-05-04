package com.koleff.kare_android.data.model.response

import com.koleff.kare_android.data.model.dto.WorkoutConfigurationDto
import com.koleff.kare_android.data.model.response.base_response.BaseResponse
import com.squareup.moshi.Json

data class WorkoutConfigurationResponse(
    @Json(name = "data")
    val workoutConfiguration: WorkoutConfigurationDto
) : BaseResponse()