package com.koleff.kare_android.domain.wrapper

import com.koleff.kare_android.data.model.response.OnboardingResponse

class OnboardingWrapper(onboardingResponse: OnboardingResponse) :
    ServerResponseData(onboardingResponse) {
    val onboardingData = onboardingResponse.onboardingData
}