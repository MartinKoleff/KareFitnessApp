package com.koleff.kare_android.data.model.response.base_response

import com.squareup.moshi.Json

open class BaseResponse(
    @Json(name = "success")
    val isSuccessful: Boolean = true,
    @field:Json(name = "error")
    val error: KareError? = null
)