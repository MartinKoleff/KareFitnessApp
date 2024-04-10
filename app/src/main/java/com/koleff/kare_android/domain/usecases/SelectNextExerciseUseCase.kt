package com.koleff.kare_android.domain.usecases

import android.util.Log
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.repository.DoWorkoutRepository
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.ui.state.DoWorkoutData
import com.koleff.kare_android.ui.state.DoWorkoutState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SelectNextExerciseUseCase(private val doWorkoutRepository: DoWorkoutRepository) {

    suspend operator fun invoke(doWorkoutData: DoWorkoutData): Flow<DoWorkoutState> =
        doWorkoutRepository.selectNextExercise(doWorkoutData).map { apiResult ->
            when (apiResult) {
                is ResultWrapper.ApiError -> {
                    DoWorkoutState(
                        isError = true,
                        error = apiResult.error ?: KareError.GENERIC
                    )
                }

                is ResultWrapper.Loading -> {
                    DoWorkoutState(isLoading = true)
                }

                is ResultWrapper.Success -> {
                    Log.d("SelectNextExerciseUseCase", "Next exercise successfully selected!")

                    DoWorkoutState(
                        isSuccessful = true,
                        doWorkoutData = apiResult.data.data
                    )
                }
            }
        }
}