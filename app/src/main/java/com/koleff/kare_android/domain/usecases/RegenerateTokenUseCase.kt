package com.koleff.kare_android.domain.usecases

import android.util.Log
import com.koleff.kare_android.data.model.dto.Tokens
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.repository.AuthenticationRepository
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.ui.state.BaseState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RegenerateTokenUseCase(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend operator fun invoke(tokens: Tokens): Flow<BaseState> = flow {
        authenticationRepository.regenerateToken(tokens).collect { apiResult ->
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
                    Log.d("RegenerateTokenUseCase", "Token regenerated successfully! New token: ${apiResult.data.tokens}")
                    emit(
                        BaseState(
                            isSuccessful = true,
                        )
                    )
                }
            }
        }
    }
}
