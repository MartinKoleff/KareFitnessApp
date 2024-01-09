package com.koleff.kare_android.data.model.response

import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.data.model.response.base_response.BaseResponse
import com.squareup.moshi.Json

data class GetWorkoutDetailsResponse(
    @Json(name = "data")
    val workoutDetails: WorkoutDetailsDto
) : BaseResponse()