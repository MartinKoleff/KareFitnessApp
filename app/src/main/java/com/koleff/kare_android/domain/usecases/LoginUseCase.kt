package com.koleff.kare_android.domain.usecases

import android.util.Log
import com.koleff.kare_android.common.credentials_validator.Credentials
import com.koleff.kare_android.common.credentials_validator.CredentialsAuthenticator
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.repository.AuthenticationRepository
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.ui.state.BaseState
import com.koleff.kare_android.ui.state.LoginData
import com.koleff.kare_android.ui.state.LoginState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class LoginUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val credentialsAuthenticator: CredentialsAuthenticator
) {

    private var _credentialsAuthenticationState: MutableStateFlow<BaseState> = MutableStateFlow(
        BaseState()
    )
    private val credentialsAuthenticationState: StateFlow<BaseState> =
        _credentialsAuthenticationState

    suspend operator fun invoke(username: String, password: String): Flow<LoginState> = flow {

        //Validate credentials
        credentialsAuthenticator.checkLoginCredentials(username, password)
            .collect { credentialsAuthenticationState ->
                _credentialsAuthenticationState.value = credentialsAuthenticationState
            }

        //Valid credentials -> proceed with login
        if (credentialsAuthenticationState.value.isSuccessful) {
            authenticationRepository.login(username, password).map { apiResult ->
                when (apiResult) {
                    is ResultWrapper.ApiError -> {
                        LoginState(
                            isError = true,
                            error = apiResult.error ?: KareError.GENERIC
                        )
                    }

                    is ResultWrapper.Loading -> {
                        LoginState(isLoading = true)
                    }

                    is ResultWrapper.Success -> {
                        Log.d("LoginUseCase", "User $username has logged in successfully!")

                        with(apiResult.data) {
                            LoginState(
                                isSuccessful = true,
                                data = LoginData(
                                    accessToken = accessToken,
                                    refreshToken = refreshToken,
                                    user = user
                                )
                            )

                            //Save credentials
                            credentialsAuthenticator.saveCredentials(user)
                        }
                    }
                }
            }
        } else //if (credentialsAuthenticationState.value.isError) {
            LoginState(
                isError = true,
                error = KareError.INVALID_CREDENTIALS
            )
    }
}