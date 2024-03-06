package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.common.credentials_validator.Credentials
import com.koleff.kare_android.common.credentials_validator.CredentialsAuthenticator
import com.koleff.kare_android.common.credentials_validator.CredentialsAuthenticatorImpl
import com.koleff.kare_android.data.model.dto.UserDto
import com.koleff.kare_android.data.model.response.ExerciseResponse
import com.koleff.kare_android.data.model.response.LoginResponse
import com.koleff.kare_android.data.model.response.base_response.BaseResponse
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.data.room.dao.UserDao
import com.koleff.kare_android.domain.wrapper.ExerciseWrapper
import com.koleff.kare_android.domain.wrapper.LoginWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.ServerResponseData
import com.koleff.kare_android.ui.state.BaseState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class AuthenticationLocalDataSource(
    private val userDao: UserDao,
    private val credentialsAuthenticator: CredentialsAuthenticator
) : AuthenticationDataSource {
    override suspend fun login(
        username: String,
        password: String
    ): Flow<ResultWrapper<LoginWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            //Validate credentials
            val credentials = Credentials(
                username = username,
                password = password
            )

            val state: MutableStateFlow<BaseState> = MutableStateFlow(BaseState())
            credentialsAuthenticator.checkCredentials(credentials).collect {
                state.value = it
            }

            if (state.value.isSuccessful) {
                val user = userDao.getUserByUsername(username)
                val accessToken = "access_token" //Not generating tokens for security concerns...
                val refreshToken = "refresh token"

                val result = LoginWrapper(
                    LoginResponse(
                        user = user.toUserDto(),
                        accessToken = accessToken,
                        refreshToken = refreshToken
                    )
                )

                emit(ResultWrapper.Success(result))
            }else{
                emit(ResultWrapper.ApiError(error = KareError.INVALID_CREDENTIALS))
            }
        }

    override suspend fun register(user: UserDto): Flow<ResultWrapper<ServerResponseData>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            //Save user
            userDao.saveUser(user.toEntity())

            val result = ServerResponseData(
                BaseResponse(isSuccessful = true)
            )

            emit(ResultWrapper.Success(result))
        }
}