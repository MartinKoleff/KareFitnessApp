package com.koleff.kare_android.domain.usecases


data class ExerciseUseCases(
    val onSearchExerciseUseCase: OnSearchExerciseUseCase,
    val onFilterExercisesUseCase: OnFilterExercisesUseCase,
    val getCatalogExercisesUseCase: GetCatalogExercisesUseCase,
    val getExerciseDetailsUseCase: GetExerciseDetailsUseCase,
    val getCatalogExerciseUseCase: GetCatalogExerciseUseCase,
    val deleteExerciseSetUseCase: DeleteExerciseSetUseCase,
    val addNewExerciseSetUseCase: AddNewExerciseSetUseCase,
    val getExerciseUseCase: GetExerciseUseCase,
    val getExercisesUseCase: GetExercisesUseCase
    
)