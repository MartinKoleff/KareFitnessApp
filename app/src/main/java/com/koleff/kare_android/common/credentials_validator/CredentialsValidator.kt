package com.koleff.kare_android.common.credentials_validator

import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.ServerResponseData

interface CredentialsValidator {

    suspend fun validate(credentials: Credentials): ResultWrapper<ServerResponseData>
}