package com.koleff.kare_android.domain.usecases

import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.data.model.dto.MachineType
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.ui.event.OnFilterExerciseEvent
import com.koleff.kare_android.ui.state.ExerciseListState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class OnFilterExercisesUseCase() {

    operator fun invoke(event: OnFilterExerciseEvent): Flow<ExerciseListState> = flow {
        emit(ExerciseListState(isLoading = true))
        delay(Constants.fakeDelay)

        when (event) {
            is OnFilterExerciseEvent.DumbbellFilter -> {
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

            is OnFilterExerciseEvent.BarbellFilter -> {
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

            is OnFilterExerciseEvent.MachineFilter -> {
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

            is OnFilterExerciseEvent.CalisthenicsFilter -> {
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

            is OnFilterExerciseEvent.NoFilter -> {
                emit(
                    ExerciseListState(
                        exerciseList = event.exercises,
                        isLoading = false,
                        isSuccessful = true
                    )
                )
            }

            is OnFilterExerciseEvent.MuscleGroupFilter -> {
                val filteredExerciseList = if (event.muscleGroup == MuscleGroup.ALL) {
                    event.exercises
                } else {
                    event.exercises.filter {
                        it.muscleGroup == event.muscleGroup
                    }
                }

                emit(
                    ExerciseListState(
                        exerciseList = filteredExerciseList,
                        isLoading = false,
                        isSuccessful = true
                    )
                )
            }
        }
    }
}