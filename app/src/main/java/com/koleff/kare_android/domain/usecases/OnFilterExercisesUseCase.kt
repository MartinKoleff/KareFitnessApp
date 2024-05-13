package com.koleff.kare_android.domain.usecases

import android.util.Log
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.data.model.dto.MachineType
import com.koleff.kare_android.ui.event.OnFilterExercisesEvent
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.state.ExerciseListState
import com.koleff.kare_android.ui.state.WorkoutListState
import com.koleff.kare_android.domain.wrapper.WorkoutListWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.repository.WorkoutRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class OnFilterExercisesUseCase() {

    suspend operator fun invoke(event: OnFilterExercisesEvent): Flow<ExerciseListState> = flow {
        emit(ExerciseListState(isLoading = true))
        delay(Constants.fakeDelay)

        when (event) {
            is OnFilterExercisesEvent.DumbbellFilter -> {
                emit(
                    ExerciseListState(
                        exerciseList = event.exercises.filter {
                            it.machineType == MachineType.DUMBBELL
                        },
                        isLoading = false,
                        isSuccessful = true
                    )
                )
            }

            is OnFilterExercisesEvent.BarbellFilter -> {
                emit(
                    ExerciseListState(
                        exerciseList = event.exercises.filter {
                            it.machineType == MachineType.BARBELL
                        },
                        isLoading = false,
                        isSuccessful = true
                    )
                )
            }

            is OnFilterExercisesEvent.MachineFilter -> {
                emit(
                    ExerciseListState(
                        exerciseList = event.exercises.filter {
                            it.machineType == MachineType.MACHINE
                        },
                        isLoading = false,
                        isSuccessful = true
                    )
                )
            }

            is OnFilterExercisesEvent.CalisthenicsFilter -> {
                emit(
                    ExerciseListState(
                        exerciseList = event.exercises.filter {
                            it.machineType == MachineType.CALISTHENICS
                        },
                        isLoading = false,
                        isSuccessful = true
                    )
                )
            }

            is OnFilterExercisesEvent.NoFilter -> {
                emit(
                    ExerciseListState(
                        exerciseList = event.exercises,
                        isLoading = false,
                        isSuccessful = true
                    )
                )
            }
        }
    }
}