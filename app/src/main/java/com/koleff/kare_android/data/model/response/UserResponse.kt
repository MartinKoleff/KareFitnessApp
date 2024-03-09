package com.koleff.kare_android.data.model.response

import com.koleff.kare_android.data.model.response.base_response.BaseResponse
import com.koleff.kare_android.data.room.entity.User
import com.squareup.moshi.Json

data class UserResponse(
    @Json(name = "data")
    val user: User
) : BaseResponse()