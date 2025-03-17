package com.koleff.kare_android.domain.usecases

data class DoWorkoutUseCases(
    val doWorkoutInitialSetupUseCase: DoWorkoutInitialSetupUseCase,
    val skipNextSetUseCase: SkipNextSetUseCase,
    val skipNextExerciseUseCase: SkipNextExerciseUseCase,
    val addNewExerciseSetUseCase: AddNewExerciseSetUseCase,
    val deleteExerciseSetUseCase: DeleteExerciseSetUseCase,
    val startTimerUseCase: StartTimerUseCase,
    val resetTimerUseCase: ResetTimerUseCase
)
