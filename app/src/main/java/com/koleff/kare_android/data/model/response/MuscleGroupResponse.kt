package com.koleff.kare_android.data.model.response

import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.response.base_response.BaseResponse
import com.squareup.moshi.Json

data class MuscleGroupResponse(
    @Json(name = "data")
    val muscleGroup: MuscleGroup
): BaseResponse()
