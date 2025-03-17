package com.koleff.kare_android.data.remote

import com.koleff.kare_android.data.model.request.FetchOnboardingDataByIdRequest
import com.koleff.kare_android.data.model.request.SaveOnboardingDataRequest
import com.koleff.kare_android.data.model.response.OnboardingResponse
import com.koleff.kare_android.data.model.response.base_response.BaseResponse
import retrofit2.http.Body
import retrofit2.http.GET

interface OnboardingApi {

    @GET("api/v1/onboarding/getonboardingdata")
    suspend fun getOnboardingData(
        @Body body: FetchOnboardingDataByIdRequest
    ): OnboardingResponse

    @GET("api/v1/onboarding/saveonboardingdata")
    suspend fun saveOnboardingData(
        @Body body: SaveOnboardingDataRequest
    ): BaseResponse

}