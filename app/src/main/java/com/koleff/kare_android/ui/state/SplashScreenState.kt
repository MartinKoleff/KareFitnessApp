package com.koleff.kare_android.ui.state

import com.koleff.kare_android.common.credentials_validator.Credentials
import com.koleff.kare_android.data.model.dto.Tokens
import com.koleff.kare_android.data.model.response.base_response.KareError

data class SplashScreenState (
    val isSuccessful: Boolean = false,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val error : KareError = KareError.GENERIC,
    val hasCredentials: Boolean = false,
    val credentials: Credentials = Credentials(),
    val tokens: Tokens = Tokens()
)