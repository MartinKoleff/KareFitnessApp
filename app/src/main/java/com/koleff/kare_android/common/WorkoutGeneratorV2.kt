package com.koleff.kare_android.common

import com.koleff.kare_android.common.manager.data.ExerciseGenerator
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.dto.WorkoutConfigurationDto
import com.koleff.kare_android.data.model.dto.WorkoutFullData
import com.koleff.kare_android.data.room.entity.Workout
import com.koleff.kare_android.data.room.entity.WorkoutDetails

object WorkoutGeneratorV2 {

    const val TOTAL_WORKOUTS = 3

    fun getWorkoutFullData(): List<WorkoutFullData> {
        val workoutDetailsList = generateWorkoutDetails()
        val workoutList = generateWorkouts()

        return listOf(
            WorkoutFullData(
                workout = workoutList[0].toDto(),
                workoutDetails = workoutDetailsList[0].toDto(),
                exercises = ExerciseGenerator.loadExercisesWithSets(
                    MuscleGroup.CHEST,
                    isWorkout = true,
                    workoutId = 1
                ).map { it.toDto() },
                exerciseDetails = ExerciseGenerator.loadExerciseDetails(
                    MuscleGroup.CHEST,
                    isWorkout = true,
                    workoutId = 1
                ).map { it.toDto() },
                configuration = WorkoutConfigurationDto(workoutId = 1)
            ),
            WorkoutFullData(
                workout = workoutList[1].toDto(),
                workoutDetails = workoutDetailsList[1].toDto(),
                exercises = ExerciseGenerator.loadExercisesWithSets(
                    MuscleGroup.BACK,
                    isWorkout = true,
                    workoutId = 2
                ).map { it.toDto() },
                exerciseDetails = ExerciseGenerator.loadExerciseDetails(
                    MuscleGroup.BACK,
                    isWorkout = true,
                    workoutId = 2
                ).map { it.toDto() },
                configuration = WorkoutConfigurationDto(workoutId = 2)
            ),
            WorkoutFullData(
                workout = workoutList[2].toDto(),
                workoutDetails = workoutDetailsList[2].toDto(),
                exercises = ExerciseGenerator.loadExercisesWithSets(
                    MuscleGroup.ARMS,
                    isWorkout = true,
                    workoutId = 3
                ).map { it.toDto() },
                exerciseDetails = ExerciseGenerator.loadExerciseDetails(
                    MuscleGroup.ARMS,
                    isWorkout = true,
                    workoutId = 3
                ).map { it.toDto() },
                configuration = WorkoutConfigurationDto(workoutId = 3)
            )
        )
    }

    fun generateWorkouts(): List<Workout> {
        return listOf(
            Workout(
                workoutId = 1,
                name = "Arnold chest workout",
                muscleGroup = MuscleGroup.CHEST,
                snapshot = "",
                totalExercises = ExerciseGenerator.getTotalExercisesForMuscleGroup(MuscleGroup.CHEST),
                isFavorite = false
            ),
            Workout(
                workoutId = 2,
                name = "Chavdo destroy back workout",
                muscleGroup = MuscleGroup.BACK,
                snapshot = "",
                totalExercises = ExerciseGenerator.getTotalExercisesForMuscleGroup(MuscleGroup.BACK),
                isFavorite = false
            ),
            Workout(
                workoutId = 3,
                name = "Blow your arms workout",
                muscleGroup = MuscleGroup.ARMS,
                snapshot = "",
                totalExercises = ExerciseGenerator.getTotalExercisesForMuscleGroup(MuscleGroup.ARMS),
                isFavorite = true
            )
        )
    }

    fun generateWorkoutDetails(): List<WorkoutDetails> {
        return listOf(
            WorkoutDetails(
                workoutDetailsId = 1,
                name = "Arnold chest workout",
                description = "Blow your chest",
                muscleGroup = MuscleGroup.CHEST,
                isFavorite = false
            ),
            WorkoutDetails(
                workoutDetailsId = 2,
                name = "Chavdo destroy back workout",
                description = "Blow your back with me4ka",
                muscleGroup = MuscleGroup.BACK,
                isFavorite = false
            ),
            WorkoutDetails(
                workoutDetailsId = 3,
                name = "Blow your arms workout",
                description = "Blow your arms with curls",
                muscleGroup = MuscleGroup.ARMS,
                isFavorite = true
            )
        )
    }
}