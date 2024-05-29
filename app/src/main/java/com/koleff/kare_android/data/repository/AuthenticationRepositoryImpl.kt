package com.koleff.kare_android.data.repository

import com.koleff.kare_android.common.auth.Credentials
import com.koleff.kare_android.data.datasource.AuthenticationDataSource
import com.koleff.kare_android.data.model.dto.Tokens
import com.koleff.kare_android.data.model.dto.UserDto
import com.koleff.kare_android.domain.repository.AuthenticationRepository
import com.koleff.kare_android.domain.wrapper.LoginWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.ServerResponseData
import com.koleff.kare_android.domain.wrapper.TokenWrapper
import kotlinx.coroutines.flow.Flow

class AuthenticationRepositoryImpl(private val authenticationDataSource: AuthenticationDataSource) :
    AuthenticationRepository {
    override suspend fun login(
        credentials: Credentials
    ): Flow<ResultWrapper<LoginWrapper>> {
        return authenticationDataSource.login(credentials)
    }

    override suspend fun register(user: UserDto): Flow<ResultWrapper<ServerResponseData>> {
        return authenticationDataSource.register(user)
    }

    override suspend fun logout(user: UserDto): Flow<ResultWrapper<ServerResponseData>> {
        return authenticationDataSource.logout(user)
    }

    override suspend fun regenerateToken(tokens: Tokens): Flow<ResultWrapper<TokenWrapper>> {
        return authenticationDataSource.regenerateToken(tokens)
    }
}