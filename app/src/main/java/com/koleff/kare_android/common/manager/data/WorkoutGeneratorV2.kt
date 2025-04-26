package com.koleff.kare_android.common

import com.koleff.kare_android.common.manager.data.ExerciseGenerator
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.dto.WorkoutConfigurationDto
import com.koleff.kare_android.data.model.dto.WorkoutFullData
import com.koleff.kare_android.data.room.entity.Workout
import com.koleff.kare_android.data.room.entity.WorkoutDetails
import com.koleff.kare_android.data.room.entity.WorkoutDetailsWithExercises

object WorkoutGeneratorV2 {

    const val TOTAL_WORKOUTS = 3
    private var workoutsList: List<Workout> = emptyList()
    private var workoutDetailsList: List<WorkoutDetailsWithExercises> = emptyList()
    private var workoutsFullDataList: List<WorkoutFullData> = emptyList()

    fun getWorkoutFullData(): List<WorkoutFullData> {
        workoutDetailsList = generateWorkoutDetails()
        workoutsList = generateWorkouts()

        if(workoutsFullDataList.isEmpty()) {
            workoutsFullDataList = listOf(
                WorkoutFullData(
                    workout = workoutsList[0].toDto().copy(workoutId = 1),
                    workoutDetails = workoutDetailsList[0].toDto().copy(workoutId = 1),
                    exercises = ExerciseGenerator.loadExercisesWithSets(
                        MuscleGroup.CHEST
                    ).map { it.toDto().copy(workoutId = 1) },
                    exerciseDetails = ExerciseGenerator.loadExerciseDetails(
                        MuscleGroup.CHEST
                    ).map { it.toDto().copy(workoutId = 1) },
                    configuration = WorkoutConfigurationDto(workoutId = 1)
                ),
                WorkoutFullData(
                    workout = workoutsList[1].toDto().copy(workoutId = 2),
                    workoutDetails = workoutDetailsList[1].toDto().copy(workoutId = 2),
                    exercises = ExerciseGenerator.loadExercisesWithSets(
                        MuscleGroup.BACK
                    ).map { it.toDto().copy(workoutId = 2) },
                    exerciseDetails = ExerciseGenerator.loadExerciseDetails(
                        MuscleGroup.BACK
                    ).map { it.toDto().copy(workoutId = 2) },
                    configuration = WorkoutConfigurationDto(workoutId = 2)
                ),
                WorkoutFullData(
                    workout = workoutsList[2].toDto().copy(workoutId = 3),
                    workoutDetails = workoutDetailsList[2].toDto().copy(workoutId = 3),
                    exercises = ExerciseGenerator.loadExercisesWithSets(
                        MuscleGroup.ARMS
                    ).map { it.toDto().copy(workoutId = 3) },
                    exerciseDetails = ExerciseGenerator.loadExerciseDetails(
                        MuscleGroup.ARMS
                    ).map { it.toDto().copy(workoutId = 3) },
                    configuration = WorkoutConfigurationDto(workoutId = 3)
                )
            )
        }

        return workoutsFullDataList
    }

    fun generateWorkouts(): List<Workout> {
        if(workoutsList.isEmpty()) {
            workoutsList = listOf(
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

        return workoutsList
    }

    fun generateWorkoutDetails(): List<WorkoutDetailsWithExercises> {
        if(workoutDetailsList.isEmpty()) {
            workoutDetailsList = listOf(
                WorkoutDetailsWithExercises(
                    workoutDetails = WorkoutDetails(
                        workoutDetailsId = 1,
                        name = "Arnold chest workout",
                        description = "Blow your chest",
                        muscleGroup = MuscleGroup.CHEST,
                        isFavorite = false
                    ),
                    exercises = ExerciseGenerator.loadExercisesWithSets(
                        MuscleGroup.CHEST,
                        workoutId = 1
                    ),
                    configuration = WorkoutConfigurationDto(workoutId = 1).toEntity()
                ),
                WorkoutDetailsWithExercises(
                    workoutDetails = WorkoutDetails(
                        workoutDetailsId = 2,
                        name = "Chavdo destroy back workout",
                        description = "Blow your back with me4ka",
                        muscleGroup = MuscleGroup.BACK,
                        isFavorite = false
                    ),
                    exercises = ExerciseGenerator.loadExercisesWithSets(
                        MuscleGroup.BACK,
                        workoutId = 2
                    ),
                    configuration = WorkoutConfigurationDto(workoutId = 2).toEntity()
                ),
                WorkoutDetailsWithExercises(
                    workoutDetails = WorkoutDetails(
                        workoutDetailsId = 3,
                        name = "Blow your arms workout",
                        description = "Blow your arms with curls",
                        muscleGroup = MuscleGroup.ARMS,
                        isFavorite = true
                    ), exercises = ExerciseGenerator.loadExercisesWithSets(
                        MuscleGroup.ARMS,
                        workoutId = 3
                    ),
                    configuration = WorkoutConfigurationDto(workoutId = 3).toEntity()
                )
            )
        }

        return workoutDetailsList
    }
}