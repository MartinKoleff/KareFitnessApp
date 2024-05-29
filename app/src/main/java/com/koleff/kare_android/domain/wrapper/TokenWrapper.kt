package com.koleff.kare_android.domain.wrapper

import com.koleff.kare_android.data.model.response.TokenResponse

class TokenWrapper(tokenResponse: TokenResponse) :
    ServerResponseData(tokenResponse) {
    val tokens = tokenResponse.tokens
}