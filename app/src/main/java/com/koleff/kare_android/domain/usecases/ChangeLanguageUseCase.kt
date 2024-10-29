package com.koleff.kare_android.domain.usecases

import com.koleff.kare_android.data.model.dto.KareLanguage
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.repository.LanguageRepository
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.ui.state.BaseState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ChangeLanguageUseCase(
    private val languageRepository: LanguageRepository
) {
    suspend operator fun invoke(selectedLanguage: KareLanguage): Flow<BaseState> =
        languageRepository.changeLanguage(selectedLanguage).map { apiResult ->
            when(apiResult){
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
                    BaseState(
                        isSuccessful = true
                    )
                }
            }
        }
    }
