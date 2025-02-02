package com.koleff.kare_android.data.model.request

import com.squareup.moshi.Json
import java.util.Date

data class FetchOnboardingDataByIdRequest(
    @field:Json(name = "onboarding_id")
    val id: Long,
)