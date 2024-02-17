package com.koleff.kare_android.data.model.response

import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.response.base_response.BaseResponse
import com.squareup.moshi.Json

data class GetDashboardResponse(
    @Json(name = "data")
    val muscleGroupList: List<MuscleGroup>
) : BaseResponse()