package com.koleff.kare_android.data.model.request

import com.koleff.kare_android.data.model.dto.DoWorkoutExerciseSetDto
import com.koleff.kare_android.data.model.dto.OnboardingDataDto
import com.squareup.moshi.Json

data class SaveOnboardingDataRequest(
    @field:Json(name = "onboarding_data")
    val onboardingData: OnboardingDataDto
)