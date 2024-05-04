package com.koleff.kare_android.domain.usecases

data class DoWorkoutPerformanceMetricsUseCases(
    val deleteDoWorkoutPerformanceMetricsUseCase: DeleteDoWorkoutPerformanceMetricsUseCase,
    val updateDoWorkoutPerformanceMetricsUseCase: UpdateDoWorkoutPerformanceMetricsUseCase,
    val getAllDoWorkoutPerformanceMetricsUseCase: GetAllDoWorkoutPerformanceMetricsUseCase,
    val getDoWorkoutPerformanceMetricsUseCase: GetDoWorkoutPerformanceMetricsUseCase,
    val saveAllDoWorkoutExerciseSetUseCase: SaveAllDoWorkoutExerciseSetUseCase,
    val saveDoWorkoutExerciseSetUseCase: SaveDoWorkoutExerciseSetUseCase,
    val saveDoWorkoutPerformanceMetricsUseCase: SaveDoWorkoutPerformanceMetricsUseCase,
)