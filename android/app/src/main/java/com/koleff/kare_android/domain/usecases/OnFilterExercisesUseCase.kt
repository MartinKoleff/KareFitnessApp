package com.koleff.kare_android.domain.usecases

import android.util.Log
import com.koleff.kare_android.data.model.dto.MachineType
import com.koleff.kare_android.data.model.event.OnFilterExercisesEvent
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.data.model.state.ExercisesState
import com.koleff.kare_android.data.model.state.WorkoutState
import com.koleff.kare_android.data.model.wrapper.GetAllWorkoutsWrapper
import com.koleff.kare_android.data.model.wrapper.ResultWrapper
import com.koleff.kare_android.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class OnFilterExercisesUseCase() {

    suspend operator fun invoke(event: OnFilterExercisesEvent): Flow<ExercisesState> = flow {
        when (event) {
            is OnFilterExercisesEvent.DumbbellFilter -> {
                emit(
                    ExercisesState(
                        exerciseList = event.exercises.filter {
                            it.machineType == MachineType.DUMBBELL
                        },
                        isLoading = false
                    )
                )
            }

            is OnFilterExercisesEvent.BarbellFilter -> {
                emit(
                    ExercisesState(
                        exerciseList = event.exercises.filter {
                            it.machineType == MachineType.BARBELL
                        },
                        isLoading = false
                    )
                )
            }

            is OnFilterExercisesEvent.MachineFilter -> {
                emit(
                    ExercisesState(
                        exerciseList = event.exercises.filter {
                            it.machineType == MachineType.MACHINE
                        },
                        isLoading = false
                    )
                )
            }

            is OnFilterExercisesEvent.CalisthenicsFilter -> {
                emit(
                    ExercisesState(
                        exerciseList = event.exercises.filter {
                            it.machineType == MachineType.CALISTHENICS
                        },
                        isLoading = false
                    )
                )
            }

            is OnFilterExercisesEvent.NoFilter -> {
                emit(
                    ExercisesState(
                        exerciseList = event.exercises,
                        isLoading = false
                    )
                )
            }
        }
    }
}