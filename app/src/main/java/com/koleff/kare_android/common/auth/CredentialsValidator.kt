package com.koleff.kare_android.common.auth

import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.ServerResponseData

interface CredentialsValidator {

    suspend fun validateRegister(credentials: Credentials): ResultWrapper<ServerResponseData>
    suspend fun validateLogin(username: String, password: String): ResultWrapper<ServerResponseData>
    fun validatePassword(password: String)
    suspend fun validateEmail(email: String)
    suspend fun validateUsername(username: String)
}