package com.koleff.kare_android.data.model.response

import com.koleff.kare_android.data.model.dto.OnboardingDataDto
import com.koleff.kare_android.data.model.response.base_response.BaseResponse
import com.squareup.moshi.Json

data class OnboardingResponse(
    @Json(name = "data")
    val onboardingData: OnboardingDataDto
) : BaseResponse()