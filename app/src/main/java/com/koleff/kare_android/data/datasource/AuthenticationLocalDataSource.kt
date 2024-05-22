package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.common.MockupDataGeneratorV2
import com.koleff.kare_android.common.auth.Credentials
import com.koleff.kare_android.common.auth.CredentialsAuthenticator
import com.koleff.kare_android.data.model.dto.Tokens
import com.koleff.kare_android.data.model.dto.UserDto
import com.koleff.kare_android.data.model.response.LoginResponse
import com.koleff.kare_android.data.model.response.TokenResponse
import com.koleff.kare_android.data.model.response.base_response.BaseResponse
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.data.room.dao.UserDao
import com.koleff.kare_android.domain.wrapper.LoginWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.ServerResponseData
import com.koleff.kare_android.domain.wrapper.TokenWrapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

class AuthenticationLocalDataSource(
    private val userDao: UserDao,
    private val credentialsAuthenticator: CredentialsAuthenticator
) : AuthenticationDataSource {
    override suspend fun login(
        credentials: Credentials
    ): Flow<ResultWrapper<LoginWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            //Validate credentials
            val state =
                credentialsAuthenticator.checkLoginCredentials(credentials).firstOrNull()

            if (state?.isSuccessful == true) {
                val user = userDao.getUserByUsername(credentials.username) ?: run {
                    //No user was found...
                    emit(ResultWrapper.ApiError(error = KareError.USER_NOT_FOUND))
                    return@flow
                }

                val accessToken = "access_token" //Not generating tokens for security concerns...
                val refreshToken = "refresh_token"

                val result = LoginWrapper(
                    LoginResponse(
                        user = user.toDto(),
                        accessToken = accessToken,
                        refreshToken = refreshToken
                    )
                )

                emit(ResultWrapper.Success(result))
            } else {
                emit(ResultWrapper.ApiError(error = KareError.INVALID_CREDENTIALS))
            }
        }

    override suspend fun register(user: UserDto): Flow<ResultWrapper<ServerResponseData>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            //Validate credentials
            val state = credentialsAuthenticator.checkRegisterCredentials(user).firstOrNull()

            if (state?.isSuccessful == true) {

                //Save user
                userDao.saveUser(user.toEntity())

                val result = ServerResponseData(
                    BaseResponse()
                )

                emit(ResultWrapper.Success(result))
            } else {
                emit(ResultWrapper.ApiError(error = KareError.INVALID_CREDENTIALS))
            }
        }

    override suspend fun logout(user: UserDto): Flow<ResultWrapper<ServerResponseData>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val result = ServerResponseData(
                BaseResponse()
            )

            emit(ResultWrapper.Success(result))
        }

    override suspend fun regenerateToken(tokens: Tokens): Flow<ResultWrapper<TokenWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val result = TokenWrapper(
                TokenResponse(
                    tokens = MockupDataGeneratorV2.generateTokens()
                )
            )

            emit(ResultWrapper.Success(result))
        }
}