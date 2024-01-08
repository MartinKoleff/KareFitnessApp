package com.koleff.kare_android.domain.usecases


data class WorkoutUseCases(
    val getWorkoutsUseCase: GetWorkoutsUseCase,
    val getWorkoutUseCase: GetWorkoutUseCase,
    val getWorkoutDetailsUseCase: GetWorkoutsDetailsUseCase,
    val updateWorkoutUseCase: UpdateWorkoutUseCase,
    val onSearchWorkoutUseCase: OnSearchWorkoutUseCase,
    val deleteExerciseUseCase: DeleteExerciseUseCase,
    val deleteWorkoutUseCase: DeleteWorkoutUseCase,
    val selectWorkoutUseCase: SelectWorkoutUseCase,
    val getSelectedWorkoutUseCase: GetSelectedWorkoutUseCase,
    val createWorkoutUseCase: CreateWorkoutUseCase
)