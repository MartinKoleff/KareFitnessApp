package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.common.Network
import com.koleff.kare_android.common.auth.CredentialsAuthenticator
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.data.model.dto.UserDto
import com.koleff.kare_android.data.model.request.LoginRequest
import com.koleff.kare_android.data.model.request.RegistrationRequest
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.data.remote.AuthenticationApi
import com.koleff.kare_android.domain.wrapper.LoginWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.ServerResponseData
import com.koleff.kare_android.ui.state.BaseState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class AuthenticationRemoteDataSource @Inject constructor(
    private val authenticationApi: AuthenticationApi,
    private val credentialsAuthenticator: CredentialsAuthenticator,
    @IoDispatcher val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : AuthenticationDataSource {
    override suspend fun login(
        username: String,
        password: String
    ): Flow<ResultWrapper<LoginWrapper>> =
        flow {
            emit(ResultWrapper.Loading())

            //Validate credentials
            val state: MutableStateFlow<BaseState> = MutableStateFlow(BaseState())
            credentialsAuthenticator.checkLoginCredentials(username, password).collect {
                state.value = it
            }

            if (state.value.isSuccessful) {
                val body = LoginRequest(
                    username,
                    password
                )

                Network.executeApiCall(dispatcher, {
                    LoginWrapper(
                        authenticationApi.login(body)
                    )
                })
            } else {
                emit(ResultWrapper.ApiError(error = KareError.INVALID_CREDENTIALS))
            }
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