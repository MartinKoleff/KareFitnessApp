package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.domain.wrapper.KareLanguagesWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface LanguageDataSource {

    suspend fun getSupportedLanguages(): Flow<ResultWrapper<KareLanguagesWrapper>>
}