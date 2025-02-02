package com.koleff.kare_android.domain.usecases

import android.util.Log
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.repository.OnboardingRepository
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.ui.state.OnboardingState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetOnboardingUseCase(private val repository: OnboardingRepository) {

    suspend operator fun invoke(id: Long): Flow<OnboardingState> =
        repository.getOnboardingData(id).map { apiResult ->
            when (apiResult) {
                is ResultWrapper.ApiError -> {
                    OnboardingState(
                        isError = true,
                        error = apiResult.error ?: KareError.GENERIC
                    )
                }

                is ResultWrapper.Loading -> {
                    OnboardingState(isLoading = true)
                }

                is ResultWrapper.Success -> {
                    Log.d("GetWorkoutUseCase", "Onboarding data with id $id fetched.")

                    OnboardingState(
                        isSuccessful = true,
                        onboardingData = apiResult.data.onboardingData
                    )
                }
            }
        }
}

