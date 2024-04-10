package com.koleff.kare_android.data.model.response

import com.koleff.kare_android.data.model.response.base_response.BaseResponse
import com.koleff.kare_android.ui.state.DoWorkoutData
import com.squareup.moshi.Json

data class DoWorkoutResponse(
    @Json(name = "data")
    val data: DoWorkoutData
): BaseResponse()
