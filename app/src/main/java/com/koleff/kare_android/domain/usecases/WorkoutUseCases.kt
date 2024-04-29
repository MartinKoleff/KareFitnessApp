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
    val submitExerciseUseCase: SubmitExerciseUseCase,
    val deleteWorkoutUseCase: DeleteWorkoutUseCase,
    val selectWorkoutUseCase: SelectWorkoutUseCase,
    val deselectWorkoutUseCase: DeselectWorkoutUseCase,
    val getSelectedWorkoutUseCase: GetSelectedWorkoutUseCase,
    val createNewWorkoutUseCase: CreateNewWorkoutUseCase,
    val createCustomWorkoutUseCase: CreateCustomWorkoutUseCase,
    val createCustomWorkoutDetailsUseCase: CreateCustomWorkoutDetailsUseCase,
    val getWorkoutConfigurationUseCase: GetWorkoutConfigurationUseCase,
    val createWorkoutConfigurationUseCase: CreateWorkoutConfigurationUseCase,
    val updateWorkoutConfigurationUseCase: UpdateWorkoutConfigurationUseCase,
    val deleteWorkoutConfigurationUseCase: DeleteWorkoutConfigurationUseCase
)