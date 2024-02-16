package com.koleff.kare_android.domain.usecases


data class WorkoutUseCases(
    val getWorkoutsUseCase: GetWorkoutsUseCase,
    val getWorkoutUseCase: GetWorkoutUseCase,
    val getWorkoutDetailsUseCase: GetWorkoutsDetailsUseCase,
    val updateWorkoutUseCase: UpdateWorkoutUseCase,
    val updateWorkoutDetailsUseCase: UpdateWorkoutDetailsUseCase,
    val onSearchWorkoutUseCase: OnSearchWorkoutUseCase,
    val deleteExerciseUseCase: DeleteExerciseUseCase, //TODO: add addExerciseUseCase?
    val deleteWorkoutUseCase: DeleteWorkoutUseCase,
    val selectWorkoutUseCase: SelectWorkoutUseCase,
    val getSelectedWorkoutUseCase: GetSelectedWorkoutUseCase,
    val createNewWorkoutUseCase: CreateNewWorkoutUseCase,
    val createCustomWorkoutUseCase: CreateCustomWorkoutUseCase,
    val createCustomWorkoutDetailsUseCase: CreateCustomWorkoutDetailsUseCase,
)