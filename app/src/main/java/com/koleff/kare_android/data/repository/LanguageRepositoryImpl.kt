package com.koleff.kare_android.data.repository

import com.koleff.kare_android.data.datasource.LanguageDataSource
import com.koleff.kare_android.data.model.dto.KareLanguage
import com.koleff.kare_android.domain.repository.LanguageRepository
import com.koleff.kare_android.domain.wrapper.KareLanguagesWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.ServerResponseData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LanguageRepositoryImpl @Inject constructor(
    private val languageDataSource: LanguageDataSource
): LanguageRepository {

    override suspend fun getSupportedLanguages(): Flow<ResultWrapper<KareLanguagesWrapper>> {
        return languageDataSource.getSupportedLanguages()
    }

    override suspend fun changeLanguage(selectedLanguage: KareLanguage): Flow<ResultWrapper<ServerResponseData>> {
        TODO("Not yet implemented") //TODO: not needed? (use use case only)
    }
}