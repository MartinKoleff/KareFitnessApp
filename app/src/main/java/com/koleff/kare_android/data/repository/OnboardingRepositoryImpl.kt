package com.koleff.kare_android.data.repository

import com.koleff.kare_android.data.datasource.OnboardingDataSource
import com.koleff.kare_android.data.model.dto.OnboardingDataDto
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.repository.OnboardingRepository
import com.koleff.kare_android.domain.wrapper.OnboardingWrapper
import com.koleff.kare_android.domain.wrapper.ServerResponseData
import kotlinx.coroutines.flow.Flow

class OnboardingRepositoryImpl(private val onboardingDataSource: OnboardingDataSource) : OnboardingRepository {
    override suspend fun getOnboardingData(id: Long): Flow<ResultWrapper<OnboardingWrapper>> {
        return onboardingDataSource.getOnboardingData(id)
    }

    override suspend fun saveOnboardingData(onboardingData: OnboardingDataDto): Flow<ResultWrapper<ServerResponseData>> {
       return onboardingDataSource.saveOnboardingData(onboardingData)
    }
}