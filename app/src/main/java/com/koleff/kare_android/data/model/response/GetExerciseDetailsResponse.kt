package com.koleff.kare_android.data.model.response

import com.koleff.kare_android.data.model.dto.ExerciseDetailsDto
import com.koleff.kare_android.data.model.response.base_response.BaseResponse
import com.squareup.moshi.Json

data class GetExerciseDetailsResponse(
    @Json(name = "data")
    val exerciseDetails: ExerciseDetailsDto
) : BaseResponse()