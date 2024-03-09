package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.common.Network
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.data.model.dto.UserDto
import com.koleff.kare_android.data.model.request.LoginRequest
import com.koleff.kare_android.data.model.request.RegistrationRequest
import com.koleff.kare_android.data.remote.AuthenticationApi
import com.koleff.kare_android.domain.wrapper.LoginWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.ServerResponseData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthenticationRemoteDataSource @Inject constructor(
    private val authenticationApi: AuthenticationApi,
    @IoDispatcher val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : AuthenticationDataSource {
    override suspend fun login(
        username: String,
        password: String
    ): Flow<ResultWrapper<LoginWrapper>> {
        val body = LoginRequest(
            username,
            password
        )

        return Network.executeApiCall(dispatcher, {
            LoginWrapper(
                authenticationApi.login(body)
            )
        })
    }

    override suspend fun register(user: UserDto): Flow<ResultWrapper<ServerResponseData>> {
        val body = RegistrationRequest(
           user
        )

        return Network.executeApiCall(dispatcher, {
            ServerResponseData(
                authenticationApi.register(body)
            )
        })
    }
}