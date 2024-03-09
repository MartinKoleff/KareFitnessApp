package com.koleff.kare_android.data.model.response

import com.koleff.kare_android.data.model.dto.UserDto
import com.koleff.kare_android.data.model.response.base_response.BaseResponse
import com.squareup.moshi.Json

data class LoginResponse(
    @Json(name = "user")
    val user: UserDto,
    @Json(name = "access_token")
    val accessToken: String,
    @Json(name = "refresh_token")
    val refreshToken: String
) : BaseResponse()