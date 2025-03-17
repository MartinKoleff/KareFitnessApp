package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.common.network.ApiAuthorizationCallWrapper
import com.koleff.kare_android.data.model.dto.OnboardingDataDto
import com.koleff.kare_android.data.model.request.FetchOnboardingDataByIdRequest
import com.koleff.kare_android.data.model.request.SaveOnboardingDataRequest
import com.koleff.kare_android.data.remote.OnboardingApi
import com.koleff.kare_android.domain.wrapper.OnboardingWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.ServerResponseData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OnboardingRemoteDataSource @Inject constructor(
    private val onboardingApi: OnboardingApi,
    private val apiAuthorizationCallWrapper: ApiAuthorizationCallWrapper,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : OnboardingDataSource {

    override suspend fun saveOnboardingData(onboardingData: OnboardingDataDto): Flow<ResultWrapper<ServerResponseData>> {
        val body = SaveOnboardingDataRequest(onboardingData)

        return apiAuthorizationCallWrapper.executeApiCall(
            dispatcher,
            { ServerResponseData(onboardingApi.saveOnboardingData(body)) }
        )
    }

    override suspend fun getOnboardingData(id: Long): Flow<ResultWrapper<OnboardingWrapper>> {
        val body = FetchOnboardingDataByIdRequest(id)

        return apiAuthorizationCallWrapper.executeApiCall(
            dispatcher,
            { OnboardingWrapper(onboardingApi.getOnboardingData(body)) }
        )
    }
}
