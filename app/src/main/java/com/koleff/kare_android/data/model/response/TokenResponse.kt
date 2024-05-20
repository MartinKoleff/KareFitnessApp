package com.koleff.kare_android.data.model.response

import com.koleff.kare_android.data.model.dto.Tokens
import com.koleff.kare_android.data.model.response.base_response.BaseResponse
import com.squareup.moshi.Json

data class TokenResponse(
    @Json(name = "tokens")
    val tokens: Tokens
) : BaseResponse()