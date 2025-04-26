package com.koleff.kare_android.domain.usecases.statistics

import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.repository.ExerciseStatisticsRepository
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.ui.state.ExercisePRState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetExercisePRUseCase(private val exerciseStatisticsRepository: ExerciseStatisticsRepository) {
    suspend operator fun invoke(exerciseId: Int): Flow<ExercisePRState> =
        exerciseStatisticsRepository.getPR(exerciseId).map { apiResult ->
            when (apiResult) {
                is ResultWrapper.ApiError -> {
                    ExercisePRState(
                        isError = true,
                        error = apiResult.error ?: KareError.GENERIC
                    )
                }

                is ResultWrapper.Loading -> {
                    ExercisePRState(isLoading = true)
                }

                is ResultWrapper.Success -> {
                    ExercisePRState(
                        isSuccessful = true,
                        pr = apiResult.data.pr
                    )
                }
            }
        }
}
