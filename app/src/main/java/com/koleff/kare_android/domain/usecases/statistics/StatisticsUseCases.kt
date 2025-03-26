package com.koleff.kare_android.domain.usecases.statistics

data class StatisticsUseCases(
    val exerciseStatisticsUseCases: ExerciseStatisticsUseCases
)

data class ExerciseStatisticsUseCases (
    val getExercisePRUseCase: GetExercisePRUseCase,
    val getExerciseTotalRepsPerformedUseCase: GetExerciseTotalRepsPerformedUseCase,
    val getExerciseTotalSetsPerformedUseCase: GetExerciseTotalSetsPerformedUseCase,
    val getExerciseTotalWeightLiftedUseCase: GetExerciseTotalWeightLiftedUseCase
)