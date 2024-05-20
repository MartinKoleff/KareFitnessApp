package com.koleff.kare_android.data.model.request

import com.koleff.kare_android.data.model.dto.Tokens
import com.squareup.moshi.Json

data class RegenerateTokenRequest(
    @field:Json(name = "tokens")
    val tokens: Tokens
)