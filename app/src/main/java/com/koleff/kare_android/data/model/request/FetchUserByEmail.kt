package com.koleff.kare_android.data.model.request

import com.squareup.moshi.Json

data class FetchUserByEmail(
    @field:Json(name = "email")
    val email: String,
)