package com.koleff.kare_android.domain.usecases


data class WorkoutUseCases(
    val getAllWorkoutsUseCase: GetAllWorkoutsUseCase,
    val getAllWorkoutDetailsUseCase: GetAllWorkoutDetailsUseCase,
    val getWorkoutUseCase: GetWorkoutUseCase,
    val getWorkoutDetailsUseCase: GetWorkoutsDetailsUseCase,
    val updateWorkoutUseCase: UpdateWorkoutUseCase,
    val updateWorkoutDetailsUseCase: UpdateWorkoutDetailsUseCase,
    val onSearchWorkoutUseCase: OnSearchWorkoutUseCase,
    val deleteExerciseUseCase: DeleteExerciseUseCase,
    val addExerciseUseCase: AddExerciseUseCase,
    val deleteWorkoutUseCase: DeleteWorkoutUseCase,
    val selectWorkoutUseCase: SelectWorkoutUseCase,
    val getSelectedWorkoutUseCase: GetSelectedWorkoutUseCase,
    val createNewWorkoutUseCase: CreateNewWorkoutUseCase,
    val createCustomWorkoutUseCase: CreateCustomWorkoutUseCase,
    val createCustomWorkoutDetailsUseCase: CreateCustomWorkoutDetailsUseCase,
)