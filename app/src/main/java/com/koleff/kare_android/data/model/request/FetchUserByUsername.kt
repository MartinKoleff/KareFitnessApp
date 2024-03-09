package com.koleff.kare_android.data.model.request

import com.squareup.moshi.Json

data class FetchUserByUsername(
    @field:Json(name = "username")
    val username: String,
)