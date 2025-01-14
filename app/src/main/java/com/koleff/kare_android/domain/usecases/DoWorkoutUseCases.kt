package com.koleff.kare_android.domain.usecases

data class DoWorkoutUseCases(
    val doWorkoutInitialSetupUseCase: DoWorkoutInitialSetupUseCase,
    val updateExerciseSetsAfterTimerUseCase: UpdateExerciseSetsAfterTimerUseCase,
    val addNewExerciseSetUseCase: AddNewExerciseSetUseCase,
    val deleteExerciseSetUseCase: DeleteExerciseSetUseCase,
    val startTimerUseCase: StartTimerUseCase,
    val resetTimerUseCase: ResetTimerUseCase,
    val resumeTimerUseCase: ResumeTimerUseCase,
    val pauseTimerUseCase: PauseTimerUseCase
)