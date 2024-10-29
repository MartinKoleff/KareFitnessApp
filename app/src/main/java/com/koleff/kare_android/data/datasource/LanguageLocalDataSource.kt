package com.koleff.kare_android.data.datasource

import android.content.Context
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.common.LanguageManager
import com.koleff.kare_android.data.model.dto.KareLanguage
import com.koleff.kare_android.data.model.response.GetLanguagesResponse
import com.koleff.kare_android.data.model.response.base_response.BaseResponse
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.wrapper.KareLanguagesWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.ServerResponseData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LanguageLocalDataSource : LanguageDataSource {
    override suspend fun getSupportedLanguages(): Flow<ResultWrapper<KareLanguagesWrapper>> = flow {
        emit(ResultWrapper.Loading())
        delay(Constants.fakeDelay)

        val result = KareLanguagesWrapper(
            GetLanguagesResponse(
                LanguageManager.getSupportedLanguages()
            )
        )

        emit(ResultWrapper.Success(result))
    }

    override suspend fun changeLanguage(
        context: Context,
        selectedLanguage: KareLanguage
    ): Flow<ResultWrapper<ServerResponseData>> = flow {
        emit(ResultWrapper.Loading())
        delay(Constants.fakeDelay)

        try {
            LanguageManager.changeLanguage(context, selectedLanguage) //TODO: add error throwing / check if error handling is needed

            val result = ServerResponseData(
                BaseResponse()
            )

            emit(ResultWrapper.Success(result))
        } catch (e: Exception) {
            emit(ResultWrapper.ApiError(KareError.CHANGE_LANGUAGE_FAILED))
        }
    }
}