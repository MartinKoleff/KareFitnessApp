package com.koleff.kare_android.data.datasource.language

import android.content.Context
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.common.network.ApiAuthorizationCallWrapper
import com.koleff.kare_android.data.model.dto.ExerciseSetDto
import com.koleff.kare_android.data.model.dto.KareLanguage
import com.koleff.kare_android.data.model.request.ChangeLanguageRequest
import com.koleff.kare_android.data.model.request.UpdateExerciseSetRequest
import com.koleff.kare_android.data.model.request.DeleteExerciseSetRequest
import com.koleff.kare_android.data.model.request.FetchExerciseRequest
import com.koleff.kare_android.data.model.request.FetchExercisesByMuscleGroupRequest
import com.koleff.kare_android.data.remote.ExerciseApi
import com.koleff.kare_android.data.remote.LanguageApi
import com.koleff.kare_android.domain.wrapper.ExerciseDetailsWrapper
import com.koleff.kare_android.domain.wrapper.ExerciseListWrapper
import com.koleff.kare_android.domain.wrapper.ExerciseWrapper
import com.koleff.kare_android.domain.wrapper.KareLanguagesWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.ServerResponseData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

class LanguageRemoteDataSource @Inject constructor(
    private val languageApi: LanguageApi,
    private val apiAuthorizationCallWrapper: ApiAuthorizationCallWrapper,
    @IoDispatcher val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : LanguageDataSource {
    override suspend fun getSupportedLanguages(): Flow<ResultWrapper<KareLanguagesWrapper>> {
        return apiAuthorizationCallWrapper.executeApiCall(
            dispatcher,
            { KareLanguagesWrapper(languageApi.getSupportedLanguages()) }
        )
    }

    override suspend fun changeLanguage(
        context: Context,
        selectedLanguage: KareLanguage
    ): Flow<ResultWrapper<ServerResponseData>> {
        val body = ChangeLanguageRequest(selectedLanguage)
        return apiAuthorizationCallWrapper.executeApiCall(
            dispatcher,
            { ServerResponseData(languageApi.changeLanguage(body)) }
        )
    }
}