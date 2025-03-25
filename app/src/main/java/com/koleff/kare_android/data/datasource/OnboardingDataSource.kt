package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.data.model.dto.OnboardingDataDto
import com.koleff.kare_android.domain.wrapper.OnboardingWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface OnboardingDataSource {

    suspend fun saveOnboardingData(onboardingData: OnboardingDataDto): Flow<ResultWrapper<OnboardingWrapper>>

    suspend fun getOnboardingData(id: Long): Flow<ResultWrapper<OnboardingWrapper>>
}