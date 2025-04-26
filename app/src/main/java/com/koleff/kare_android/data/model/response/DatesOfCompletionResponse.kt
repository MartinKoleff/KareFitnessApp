package com.koleff.kare_android.data.model.response

import com.koleff.kare_android.data.model.response.base_response.BaseResponse
import com.squareup.moshi.Json
import java.util.Date

data class DatesOfCompletionResponse(
    @Json(name = "dates")
    val dates: List<Date>
) : BaseResponse()