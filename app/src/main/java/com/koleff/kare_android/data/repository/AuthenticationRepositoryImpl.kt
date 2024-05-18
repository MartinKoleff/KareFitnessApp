package com.koleff.kare_android.data.repository

import com.koleff.kare_android.data.datasource.AuthenticationDataSource
import com.koleff.kare_android.data.model.dto.UserDto
import com.koleff.kare_android.domain.repository.AuthenticationRepository
import com.koleff.kare_android.domain.wrapper.LoginWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.ServerResponseData
import kotlinx.coroutines.flow.Flow

class AuthenticationRepositoryImpl(private val authenticationDataSource: AuthenticationDataSource) :
    AuthenticationRepository {
    override suspend fun login(
        username: String,
        password: String
    ): Flow<ResultWrapper<LoginWrapper>> {
        return authenticationDataSource.login(username, password)
    }

    override suspend fun register(user: UserDto): Flow<ResultWrapper<ServerResponseData>> {
        return authenticationDataSource.register(user)
    }

    override suspend fun logout(user: UserDto): Flow<ResultWrapper<ServerResponseData>> {
        return authenticationDataSource.logout(user)
    }
}