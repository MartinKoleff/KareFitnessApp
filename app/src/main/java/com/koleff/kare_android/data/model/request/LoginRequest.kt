package com.koleff.kare_android.data.model.request

import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.squareup.moshi.Json

data class LoginRequest(
    @field:Json(name = "username")
    val username: String,
    @field:Json(name = "password")
    val password: String,
)