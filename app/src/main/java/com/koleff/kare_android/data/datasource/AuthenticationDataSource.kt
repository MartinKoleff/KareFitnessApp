package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.common.auth.Credentials
import com.koleff.kare_android.data.model.dto.Tokens
import com.koleff.kare_android.data.model.dto.UserDto
import com.koleff.kare_android.domain.wrapper.LoginWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.ServerResponseData
import com.koleff.kare_android.domain.wrapper.TokenWrapper
import kotlinx.coroutines.flow.Flow

interface AuthenticationDataSource {

    suspend fun login(credentials: Credentials): Flow<ResultWrapper<LoginWrapper>>

    suspend fun register(user: UserDto): Flow<ResultWrapper<ServerResponseData>>

    suspend fun logout(user: UserDto): Flow<ResultWrapper<ServerResponseData>>

    suspend fun regenerateToken(tokens: Tokens): Flow<ResultWrapper<TokenWrapper>>
}