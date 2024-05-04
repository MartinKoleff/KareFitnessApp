package com.koleff.kare_android.domain.usecases

import android.util.Log
import com.koleff.kare_android.data.model.dto.DoWorkoutExerciseSetDto
import com.koleff.kare_android.data.model.dto.DoWorkoutPerformanceMetricsDto
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.data.room.entity.DoWorkoutExerciseSet
import com.koleff.kare_android.domain.repository.DoWorkoutPerformanceMetricsRepository
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.ui.state.BaseState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SaveDoWorkoutExerciseSetUseCase(private val repository: DoWorkoutPerformanceMetricsRepository) {
    suspend operator fun invoke(exerciseSet: DoWorkoutExerciseSetDto): Flow<BaseState> =
        repository.saveDoWorkoutExerciseSet(exerciseSet).map { apiResult ->
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
                        "SaveDoWorkoutExerciseSetUseCase",
                        "Do workout exercise set metrics saved! Exercise set: $exerciseSet"
                    )

                    BaseState(
                        isSuccessful = true,
                    )
                }
            }
        }
}
