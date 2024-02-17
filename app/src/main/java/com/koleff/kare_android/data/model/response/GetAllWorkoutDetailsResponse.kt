package com.koleff.kare_android.data.model.response

import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.response.base_response.BaseResponse
import com.squareup.moshi.Json

data class GetAllWorkoutDetailsResponse(
    @Json(name = "data")
    val workoutDetailsList: List<WorkoutDetailsDto>
) : BaseResponse()