package com.koleff.kare_android.domain.usecases

import android.util.Log
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.repository.AuthenticationRepository
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.ui.state.BaseState
import com.koleff.kare_android.ui.state.LoginData
import com.koleff.kare_android.ui.state.LoginState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LoginUseCase(private val authenticationRepository: AuthenticationRepository) {

    suspend operator fun invoke(username: String, password: String): Flow<LoginState> =
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
                    }
                }
            }
        }
}