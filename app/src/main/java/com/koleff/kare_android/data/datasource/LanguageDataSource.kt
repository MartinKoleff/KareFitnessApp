package com.koleff.kare_android.data.datasource

import android.content.Context
import com.koleff.kare_android.data.model.dto.KareLanguage
import com.koleff.kare_android.domain.wrapper.KareLanguagesWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.ServerResponseData
import kotlinx.coroutines.flow.Flow

interface LanguageDataSource {

    suspend fun getSupportedLanguages(): Flow<ResultWrapper<KareLanguagesWrapper>>

    suspend fun changeLanguage(context: Context, selectedLanguage: KareLanguage): Flow<ResultWrapper<ServerResponseData>>
}