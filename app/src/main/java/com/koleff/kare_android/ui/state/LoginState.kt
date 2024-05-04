package com.koleff.kare_android.ui.state

import com.koleff.kare_android.data.model.dto.UserDto
import com.koleff.kare_android.data.model.response.base_response.KareError

data class LoginData(
    val accessToken: String = "",
    val refreshToken: String = "",
    val user: UserDto = UserDto(),
)

data class LoginState (
    val data: LoginData = LoginData(),
    override val isSuccessful: Boolean = false,
    override val isLoading: Boolean = false,
    override val isError: Boolean = false,
    override val error: KareError = KareError.GENERIC
): BaseState(isSuccessful, isLoading, isError, error)