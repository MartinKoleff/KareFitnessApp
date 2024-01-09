package com.koleff.kare_android.data.model.response

import com.koleff.kare_android.data.model.dto.ExerciseDetailsDto
import com.koleff.kare_android.data.model.dto.MuscleGroupUI
import com.koleff.kare_android.data.model.response.base_response.BaseResponse
import com.koleff.kare_android.ui.view_model.MuscleGroupUIList
import com.squareup.moshi.Json

data class GetDashboardResponse(
    @Json(name = "data")
    val muscleGroupList: List<MuscleGroupUI>
) : BaseResponse()