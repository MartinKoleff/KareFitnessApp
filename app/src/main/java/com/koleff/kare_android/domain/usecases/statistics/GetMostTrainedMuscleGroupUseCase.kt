package com.koleff.kare_android.domain.usecases.statistics

import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.repository.GeneralStatisticsRepository
import com.koleff.kare_android.domain.repository.WorkoutStatisticsRepository
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.TotalTimesCompletedWrapper
import com.koleff.kare_android.ui.state.MuscleGroupState
import com.koleff.kare_android.ui.state.TotalRepsPerformedState
import com.koleff.kare_android.ui.state.TotalTimesCompletedState
import com.koleff.kare_android.ui.state.WorkoutStreakState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetMostTrainedMuscleGroupUseCase(private val generalStatisticsRepository: GeneralStatisticsRepository) {
    suspend operator fun invoke(): Flow<MuscleGroupState> =
        generalStatisticsRepository.getMostTrainedMuscleGroup().map { apiResult ->
            when (apiResult) {
                is ResultWrapper.ApiError -> {
                    MuscleGroupState(
                        isError = true,
                        error = apiResult.error ?: KareError.GENERIC
                    )
                }

                is ResultWrapper.Loading -> {
                    MuscleGroupState(isLoading = true)
                }

                is ResultWrapper.Success -> {
                    MuscleGroupState(
                        isSuccessful = true,
                        muscleGroup = apiResult.data.muscleGroup
                    )
                }
            }
        }
}
