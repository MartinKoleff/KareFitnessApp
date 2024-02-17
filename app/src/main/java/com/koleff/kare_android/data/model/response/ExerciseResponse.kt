package com.koleff.kare_android.data.model.response

import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.response.base_response.BaseResponse
import com.squareup.moshi.Json

data class ExerciseResponse(
    @Json(name = "data")
    val exercise: ExerciseDto
) : BaseResponse()