package com.koleff.kare_android.data.remote

import com.koleff.kare_android.data.model.request.ChangeLanguageRequest
import com.koleff.kare_android.data.model.response.LanguagesResponse
import com.koleff.kare_android.data.model.response.base_response.BaseResponse
import retrofit2.http.GET
import retrofit2.http.PUT

interface LanguageApi {
    @GET("api/v1/language/getsupportedlanguages")
    suspend fun getSupportedLanguages(): LanguagesResponse

    @PUT("api/v1/language/changelanguage") //TODO: change method type
    fun changeLanguage(body: ChangeLanguageRequest): BaseResponse
}