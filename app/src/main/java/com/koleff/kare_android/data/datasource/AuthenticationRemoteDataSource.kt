package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.common.auth.Credentials
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.common.network.NetworkManager
import com.koleff.kare_android.data.model.dto.Tokens
import com.koleff.kare_android.data.model.dto.UserDto
import com.koleff.kare_android.data.model.request.RegenerateTokenRequest
import com.koleff.kare_android.data.model.request.RegistrationRequest
import com.koleff.kare_android.data.remote.AuthenticationApi
import com.koleff.kare_android.domain.wrapper.LoginWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.ServerResponseData
import com.koleff.kare_android.domain.wrapper.TokenWrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

typealias SignInRequest = RegistrationRequest

class AuthenticationRemoteDataSource @Inject constructor(
    private val authenticationApi: AuthenticationApi,
    private val networkManager: NetworkManager,
    @IoDispatcher val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : AuthenticationDataSource {
    override suspend fun login(
        credentials: Credentials
    ): Flow<ResultWrapper<LoginWrapper>> {
        val body = SignInRequest(credentials)

        return networkManager.executeApiCall(dispatcher, {
            LoginWrapper(
                authenticationApi.login(body)
            )
        })
    }

    override suspend fun register(user: UserDto): Flow<ResultWrapper<ServerResponseData>> {
        val body = RegistrationRequest(user)

        return networkManager.executeApiCall(
            dispatcher,
            { ServerResponseData(authenticationApi.register(body)) }
        )
    }

    override suspend fun logout(user: UserDto): Flow<ResultWrapper<ServerResponseData>> {
        val body = RegistrationRequest(user)

        return networkManager.executeApiCall(
            dispatcher,
            { ServerResponseData(authenticationApi.logout(body)) }
        )
    }

    override suspend fun regenerateToken(tokens: Tokens): Flow<ResultWrapper<TokenWrapper>> {
        val body = RegenerateTokenRequest(tokens)

        return networkManager.executeApiCall(
            dispatcher,
            { TokenWrapper(authenticationApi.regenerateToken(body)) }
        )
    }
}