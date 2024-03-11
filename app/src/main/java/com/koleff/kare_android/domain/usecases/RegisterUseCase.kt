package com.koleff.kare_android.domain.usecases

import android.util.Log
import com.koleff.kare_android.common.auth.CredentialsAuthenticator
import com.koleff.kare_android.data.model.dto.UserDto
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.repository.AuthenticationRepository
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.ui.state.BaseState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class RegisterUseCase(
    private val authenticationRepository: AuthenticationRepository
) {

    suspend operator fun invoke(user: UserDto): Flow<BaseState> =
        authenticationRepository.register(user).map { apiResult ->
            when (apiResult) {
                is ResultWrapper.ApiError -> {
                    BaseState(
                        isError = true,
                        error = apiResult.error ?: KareError.GENERIC
                    )
                }

                is ResultWrapper.Loading -> {
                    BaseState(isLoading = true)
                }

                is ResultWrapper.Success -> {
                    Log.d("RegisterUseCase", "User $user has registered successfully!")
                    BaseState(
                        isSuccessful = true,
                    )
                }
            }
        }
}
