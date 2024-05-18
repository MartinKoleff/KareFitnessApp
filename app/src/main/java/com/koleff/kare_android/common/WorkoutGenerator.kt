package com.koleff.kare_android.common

import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.dto.WorkoutConfigurationDto
import com.koleff.kare_android.data.room.entity.Workout
import com.koleff.kare_android.data.room.entity.WorkoutConfiguration
import com.koleff.kare_android.data.room.entity.WorkoutDetails
import com.koleff.kare_android.data.room.entity.relations.WorkoutDetailsExerciseCrossRef
import com.koleff.kare_android.data.room.entity.WorkoutDetailsWithExercises
import com.koleff.kare_android.data.room.entity.relations.WorkoutDetailsWorkoutCrossRef

object WorkoutGenerator {

    const val TOTAL_WORKOUTS = 3

     fun getAllWorkoutDetailsWorkoutCrossRefs(): List<WorkoutDetailsWorkoutCrossRef> {
        val workoutDetailsList = getAllWorkoutDetails()
        val workoutList = getAllWorkouts()

        val allIdsMatch = workoutList.all { workout ->
            workoutDetailsList.any { details ->
                details.workoutDetails.workoutDetailsId == workout.workoutId
            }
        }

        if (!allIdsMatch) {
            throw IllegalStateException("Workout IDs and Workout Details IDs do not match.")
        }

        val workoutDetailsWorkoutCrossRefs = workoutList.mapNotNull { workout ->
            workoutDetailsList.find { it.workoutDetails.workoutDetailsId == workout.workoutId }?.let { details ->
                WorkoutDetailsWorkoutCrossRef(workoutDetailsId = details.workoutDetails.workoutDetailsId, workoutId = workout.workoutId)
            }
        }

        return workoutDetailsWorkoutCrossRefs
    }

    fun getAllWorkoutDetailsExerciseCrossRefs(): List<WorkoutDetailsExerciseCrossRef> {
        val workoutDetailsList = getAllWorkoutDetails()

        val workoutDetailsExerciseCrossRefList = mutableListOf<WorkoutDetailsExerciseCrossRef>()
        workoutDetailsList.map {
            it.workoutDetails
        }.forEach { workoutDetails ->
            val muscleGroupRange = ExerciseGenerator.getMuscleGroupRange(workoutDetails.muscleGroup)

            for (i in muscleGroupRange.first..muscleGroupRange.second step 1) {
                workoutDetailsExerciseCrossRefList.add(
                    WorkoutDetailsExerciseCrossRef(
                        exerciseId = i,
                        workoutDetailsId = workoutDetails.workoutDetailsId
                    )
                )
            }
        }

        return workoutDetailsExerciseCrossRefList
    }

    fun getAllWorkoutDetails(): List<WorkoutDetailsWithExercises> {
        return listOf(
            WorkoutDetailsWithExercises(
                workoutDetails = WorkoutDetails(
                    workoutDetailsId = 1,
                    name = "Arnold chest workout",
                    description = "Blow your chest",
                    muscleGroup = MuscleGroup.CHEST,
                    isFavorite = false
                ),
                exercises = ExerciseGenerator.loadExercisesWithSets(MuscleGroup.CHEST, isWorkout = true, workoutId = 1),
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
                exercises = ExerciseGenerator.loadExercisesWithSets(MuscleGroup.BACK, isWorkout = true, workoutId = 2),
                configuration = WorkoutConfigurationDto(workoutId = 2).toEntity()
            ),
            WorkoutDetailsWithExercises(
                workoutDetails = WorkoutDetails(
                    workoutDetailsId = 3,
                    name = "Blow your arms workout",
                    description = "Blow your arms with curls",
                    muscleGroup = MuscleGroup.ARMS,
                    isFavorite = true
                ), exercises = ExerciseGenerator.loadExercisesWithSets(MuscleGroup.ARMS, isWorkout = true, workoutId = 3),
                configuration = WorkoutConfigurationDto(workoutId = 3).toEntity()
            )
        )
    }

    fun getAllWorkouts(): List<Workout> {
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
}