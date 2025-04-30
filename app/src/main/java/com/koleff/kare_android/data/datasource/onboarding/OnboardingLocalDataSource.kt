package com.koleff.kare_android.data.datasource.onboarding

import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.data.model.dto.OnboardingDataDto
import com.koleff.kare_android.data.model.response.OnboardingResponse
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.data.room.dao.OnboardingDao
import com.koleff.kare_android.domain.wrapper.OnboardingWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OnboardingLocalDataSource @Inject constructor(
    private val onboardingDao: OnboardingDao
) : OnboardingDataSource {

    override suspend fun saveOnboardingData(onboardingData: OnboardingDataDto): Flow<ResultWrapper<OnboardingWrapper>> =

        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val id = onboardingDao.saveOnboardingData(onboardingData.toEntity())

            val result = OnboardingWrapper(
                OnboardingResponse(onboardingData.copy(id = id))
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
