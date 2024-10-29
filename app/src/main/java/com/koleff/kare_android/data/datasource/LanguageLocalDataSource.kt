package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.common.LanguageManager
import com.koleff.kare_android.data.model.response.GetLanguagesResponse
import com.koleff.kare_android.domain.wrapper.KareLanguagesWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
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
}