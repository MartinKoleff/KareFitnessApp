package com.koleff.kare_android.domain.usecases

import android.util.Log
import com.koleff.kare_android.data.model.dto.OnboardingDataDto
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.repository.OnboardingRepository
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.ui.state.BaseState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SaveOnboardingUseCase(private val repository: OnboardingRepository) {
    suspend operator fun invoke(onboardingData: OnboardingDataDto): Flow<BaseState> =
        repository.saveOnboardingData(onboardingData).map { apiResult ->
            when (apiResult) {
                is ResultWrapper.ApiError -> {
                    BaseState(
                        isError = true,
                        error = apiResult.error ?: KareError.GENERIC
                    )
                }

                is ResultWrapper.Loading -> {
                    BaseState(isLoading = true)
                }

                is ResultWrapper.Success -> {
                    Log.d(
                        "SaveOnboardingUseCase", "Onboarding data saved!")

                    BaseState(isSuccessful = true)
                }
            }
        }
}
