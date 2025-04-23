package com.koleff.kare_android.data.model.dto

data class WorkoutFullData(
    val workout: WorkoutDto = WorkoutDto(),
    val workoutDetails: WorkoutDetailsDto = WorkoutDetailsDto(),
    val configuration: WorkoutConfigurationDto = WorkoutConfigurationDto(),
    val exercises: List<ExerciseDto> = emptyList(),
    val exerciseDetails: List<ExerciseDetailsDto> = emptyList()
)

