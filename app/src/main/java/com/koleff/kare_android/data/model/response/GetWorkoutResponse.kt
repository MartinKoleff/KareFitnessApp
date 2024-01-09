package com.koleff.kare_android.data.model.response

import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.data.model.response.base_response.BaseResponse
import com.squareup.moshi.Json

data class GetWorkoutResponse(
    @Json(name = "data")
    val workout: WorkoutDto
) : BaseResponse()