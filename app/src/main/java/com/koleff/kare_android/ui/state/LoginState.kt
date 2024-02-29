package com.koleff.kare_android.ui.state

import com.koleff.kare_android.data.model.dto.UserDto
import com.koleff.kare_android.data.model.response.base_response.KareError

data class LoginData(
    val accessToken: String = "",
    val refreshToken: String = "",
    val user: UserDto = UserDto(),
)

data class LoginState (
    val isSuccessful: Boolean = false,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val error : KareError = KareError.GENERIC,
    val data: LoginData = LoginData()
)