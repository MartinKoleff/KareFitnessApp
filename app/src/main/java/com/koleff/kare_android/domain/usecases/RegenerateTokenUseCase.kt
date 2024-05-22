package com.koleff.kare_android.domain.usecases

import android.util.Log
import com.koleff.kare_android.data.model.dto.Tokens
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.repository.AuthenticationRepository
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.ui.state.TokenState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RegenerateTokenUseCase(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend operator fun invoke(tokens: Tokens): Flow<TokenState> = flow {
        authenticationRepository.regenerateToken(tokens).collect { apiResult ->
            when (apiResult) {
                is ResultWrapper.ApiError -> {
                    emit(
                        TokenState(
                            isError = true,
                            error = apiResult.error ?: KareError.GENERIC
                        )
                    )
                }

                is ResultWrapper.Loading -> {
                    emit(
                        TokenState(isLoading = true)
                    )
                }

                is ResultWrapper.Success -> {
                    Log.d("RegenerateTokenUseCase", "Token regenerated successfully! New token: ${apiResult.data.tokens}")
                    emit(
                        TokenState(
                            isSuccessful = true,
                            tokens = apiResult.data.tokens
                        )
                    )
                }
            }
        }
    }
}
