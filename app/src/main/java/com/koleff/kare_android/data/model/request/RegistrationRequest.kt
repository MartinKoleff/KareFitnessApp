package com.koleff.kare_android.data.model.request

import com.koleff.kare_android.data.model.dto.UserDto
import com.squareup.moshi.Json

data class RegistrationRequest(
    @field:Json(name = "user")
    val user: UserDto
)