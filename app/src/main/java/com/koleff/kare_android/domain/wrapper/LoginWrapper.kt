package com.koleff.kare_android.domain.wrapper

import com.koleff.kare_android.data.model.response.LoginResponse

class LoginWrapper(loginResponse: LoginResponse):
    ServerResponseData(loginResponse) {
    val accessToken = loginResponse.accessToken
    val refreshToken = loginResponse.refreshToken
    val user = loginResponse.user
}