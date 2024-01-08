package com.koleff.kare_android.domain.usecases


data class ExerciseUseCases(
    val onSearchExerciseUseCase: OnSearchExerciseUseCase,
    val onFilterExercisesUseCase: OnFilterExercisesUseCase,
    val getExercisesUseCase: GetExercisesUseCase,
    val getExerciseDetailsUseCase: GetExerciseDetailsUseCase,
    val getExerciseUseCase: GetExerciseUseCase
)