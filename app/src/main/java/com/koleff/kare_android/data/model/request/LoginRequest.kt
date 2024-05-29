package com.koleff.kare_android.data.model.request

import com.squareup.moshi.Json

@Deprecated("using RegistrationRequest")
data class LoginRequest(
    @field:Json(name = "username")
    val username: String,
    @field:Json(name = "password")
    val password: String,
)