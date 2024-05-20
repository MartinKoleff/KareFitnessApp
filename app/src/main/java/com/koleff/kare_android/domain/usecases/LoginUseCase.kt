package com.koleff.kare_android.domain.usecases

import android.util.Log
import com.koleff.kare_android.common.auth.Credentials
import com.koleff.kare_android.common.auth.CredentialsAuthenticator
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.repository.AuthenticationRepository
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.ui.state.LoginData
import com.koleff.kare_android.ui.state.LoginState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

class LoginUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val credentialsAuthenticator: CredentialsAuthenticator
) {
    suspend operator fun invoke(credentials: Credentials): Flow<LoginState> = flow {

        //Initial loading
        emit(LoginState(isLoading = true))

        //Validate credentials
        val state = credentialsAuthenticator.checkLoginCredentials(credentials).firstOrNull()

        //Valid credentials -> proceed with login
        if (state?.isSuccessful == true) {
            authenticationRepository.login(credentials).collect { apiResult ->
                when (apiResult) {
                    is ResultWrapper.ApiError -> {
                        emit(
                            LoginState(
                                isError = true,
                                error = apiResult.error ?: KareError.GENERIC
                            )
                        )
                    }

                    is ResultWrapper.Loading -> {
                        emit(LoginState(isLoading = true))
                    }

                    is ResultWrapper.Success -> {
                        Log.d("LoginUseCase", "User ${credentials.username} has logged in successfully!")

                        with(apiResult.data) {

                            //Save credentials
                            credentialsAuthenticator.saveCredentials(user)

                            emit(
                                LoginState(
                                    isSuccessful = true,
                                    data = LoginData(
                                        accessToken = accessToken,
                                        refreshToken = refreshToken,
                                        user = user
                                    )
                                )
                            )
                        }
                    }
                }
            }
        } else { //if (credentialsAuthenticationState.value.isError)
            emit(
                LoginState(
                    isError = true,
                    error = KareError.INVALID_CREDENTIALS
                )
            )
        }
    }
}
