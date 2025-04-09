package com.koleff.kare_android.domain.usecases.statistics

data class StatisticsUseCases(
    val exerciseStatisticsUseCases: ExerciseStatisticsUseCases,
    val workoutStatisticsUseCases: WorkoutStatisticsUseCases,
    val generalStatisticsUseCases: GeneralStatisticsUseCases
)

data class ExerciseStatisticsUseCases (
    val getExercisePRUseCase: GetExercisePRUseCase,
    val getExercise1RepMaxUseCase: GetExercise1RepMaxUseCase,
    val getExerciseTotalRepsPerformedUseCase: GetExerciseTotalRepsPerformedUseCase,
    val getExerciseTotalSetsPerformedUseCase: GetExerciseTotalSetsPerformedUseCase,
    val getExerciseTotalWeightLiftedUseCase: GetExerciseTotalWeightLiftedUseCase
)

data class WorkoutStatisticsUseCases(
    val getWorkoutTotalTimesCompletedUseCase: GetWorkoutTotalTimesCompletedUseCase,
    val getWorkoutTotalRepsPerformedUseCase: GetWorkoutTotalRepsPerformedUseCase,
    val getWorkoutTotalSetsPerformedUseCase: GetWorkoutTotalSetsPerformedUseCase,
    val getWorkoutTotalWeightLiftedUseCase: GetWorkoutTotalWeightLiftedUseCase,
    val getDatesOfCompletionUseCase: GetDatesOfCompletionUseCase
)

data class GeneralStatisticsUseCases(
    val getWorkoutsCompletedUseCase: GetWorkoutsCompletedUseCase,
    val getDistinctWorkoutsCompletedUseCase: GetDistinctWorkoutsCompletedUseCase,
    val getWorkoutStreakUseCase: GetWorkoutStreakUseCase,
    val getMostFrequentWorkoutUseCase: GetMostFrequentWorkoutUseCase,
    val getMostTrainedMuscleGroupUseCase: GetMostTrainedMuscleGroupUseCase,
    val getMostTrainedExerciseUseCase: GetMostTrainedExerciseUseCase,
    val getTotalWeightLiftedUseCase: GetTotalWeightLiftedUseCase,
    val getStrongestMuscleGroupUseCase: GetStrongestMuscleGroupUseCase
)