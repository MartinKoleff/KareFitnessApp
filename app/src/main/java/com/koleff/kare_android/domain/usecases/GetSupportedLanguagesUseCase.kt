package com.koleff.kare_android.domain.usecases

import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.repository.LanguageRepository
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.ui.state.SupportedLanguagesState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetSupportedLanguagesUseCase(
    private val languageRepository: LanguageRepository
) {
    suspend operator fun invoke(): Flow<SupportedLanguagesState> =
        languageRepository.getSupportedLanguages().map { apiResult ->
            when(apiResult){
                is ResultWrapper.ApiError -> {
                    SupportedLanguagesState(
                        isError = true,
                        error = apiResult.error ?: KareError.GENERIC
                    )
                }
                is ResultWrapper.Loading -> {
                    SupportedLanguagesState(isLoading = true)
                }
                is ResultWrapper.Success -> {
                    SupportedLanguagesState(
                        isSuccessful = true,
                        supportedLanguages = apiResult.data.languages
                    )
                }
            }
        }
    }
}