package com.koleff.kare_android.domain.usecases

import android.util.Log
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.state.WorkoutState
import com.koleff.kare_android.domain.wrapper.GetAllWorkoutsWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.repository.WorkoutRepository
import com.koleff.kare_android.ui.state.BaseState
import com.koleff.kare_android.ui.state.SelectedWorkoutState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SelectWorkoutUseCase(private val workoutRepository: WorkoutRepository) {

    suspend operator fun invoke(workoutId: Int): Flow<BaseState> =
        workoutRepository.selectWorkout(workoutId).map { apiResult ->
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
                    Log.d("SelectWorkoutUseCase", "Workout with id $workoutId is selected: ") //TODO: if workout is selected -> deselect

                    BaseState(
                        isSuccessful = true,
                    )
                }
            }
        }
}

