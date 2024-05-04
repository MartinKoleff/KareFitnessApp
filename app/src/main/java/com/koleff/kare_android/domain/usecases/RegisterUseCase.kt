package com.koleff.kare_android.domain.usecases

import android.util.Log
import com.koleff.kare_android.common.auth.CredentialsAuthenticator
import com.koleff.kare_android.data.model.dto.UserDto
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.repository.AuthenticationRepository
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.ui.state.BaseState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

class RegisterUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val credentialsAuthenticator: CredentialsAuthenticator
) {
    suspend operator fun invoke(user: UserDto): Flow<BaseState> = flow {

        //Initial loading
        emit(BaseState(isLoading = true))

        //Validate credentials
        val state = credentialsAuthenticator.checkRegisterCredentials(user).firstOrNull()

        //Valid credentials -> proceed with register
        if (state?.isSuccessful == true) {
            authenticationRepository.register(user).collect { apiResult ->
                when (apiResult) {
                    is ResultWrapper.ApiError -> {
                        emit(
                            BaseState(
                                isError = true,
                                error = apiResult.error ?: KareError.GENERIC
                            )
                        )
                    }

                    is ResultWrapper.Loading -> {
                        emit(
                            BaseState(isLoading = true)
                        )
                    }

                    is ResultWrapper.Success -> {
                        Log.d("RegisterUseCase", "User $user has registered successfully!")
                        emit(
                            BaseState(
                                isSuccessful = true,
                            )
                        )
                    }
                }
            }
        } else {
            emit(
                BaseState(
                    isError = true,
                    error = KareError.INVALID_CREDENTIALS
                )
            )
        }
    }
}
