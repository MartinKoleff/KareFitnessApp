package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.R
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.dto.OnboardingDataDto
import com.koleff.kare_android.data.model.response.DashboardMuscleGroupsResponse
import com.koleff.kare_android.data.model.response.OnboardingResponse
import com.koleff.kare_android.data.model.response.UserResponse
import com.koleff.kare_android.data.model.response.base_response.BaseResponse
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.data.room.dao.OnboardingDao
import com.koleff.kare_android.data.room.dao.UserDao
import com.koleff.kare_android.domain.wrapper.DashboardWrapper
import com.koleff.kare_android.domain.wrapper.OnboardingWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.ServerResponseData
import com.koleff.kare_android.domain.wrapper.UserWrapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OnboardingLocalDataSource @Inject constructor(
    private val onboardingDao: OnboardingDao
) : OnboardingDataSource {

    override suspend fun saveOnboardingData(onboardingData: OnboardingDataDto): Flow<ResultWrapper<ServerResponseData>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            onboardingDao.saveOnboardingData(onboardingData.toEntity())
            val result = ServerResponseData(
                BaseResponse()
            )

            emit(ResultWrapper.Success(result))

        }


    override suspend fun getOnboardingData(id: Long): Flow<ResultWrapper<OnboardingWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            try {
                val data = onboardingDao.getOnboardingDataById(id) ?: run {

                    //Onboarding data not found...
                    emit(
                        ResultWrapper.ApiError(
                            error = KareError.ONBOARDING_DATA_NOT_FOUND
                        )
                    )
                    return@flow
                }

                val result = OnboardingWrapper(
                    OnboardingResponse(data.toDto())
                )

                emit(ResultWrapper.Success(result))
            } catch (e: NoSuchElementException) {
                emit(
                    ResultWrapper.ApiError(
                        error = KareError.ONBOARDING_DATA_NOT_FOUND
                    )
                )
            }
        }
}
