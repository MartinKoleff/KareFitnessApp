package com.koleff.kare_android.common

import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.room.entity.Workout
import com.koleff.kare_android.data.room.entity.WorkoutDetails
import com.koleff.kare_android.data.room.entity.relations.WorkoutDetailsExerciseCrossRef
import com.koleff.kare_android.data.room.entity.relations.WorkoutDetailsWithExercises
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
                    isSelected = false
                ),
                exercises = ExerciseGenerator.loadExercises(MuscleGroup.CHEST, isWorkout = true)
            ),
            WorkoutDetailsWithExercises(
                workoutDetails = WorkoutDetails(
                    workoutDetailsId = 2,
                    name = "Chavdo destroy back workout",
                    description = "Blow your back with me4ka",
                    muscleGroup = MuscleGroup.BACK,
                    isSelected = false
                ),
                exercises = ExerciseGenerator.loadExercises(MuscleGroup.BACK, isWorkout = true)
            ),
            WorkoutDetailsWithExercises(
                workoutDetails = WorkoutDetails(
                    workoutDetailsId = 3,
                    name = "Blow your arms workout",
                    description = "Blow your arms with curls",
                    muscleGroup = MuscleGroup.ARMS,
                    isSelected = true
                ), exercises = ExerciseGenerator.loadExercises(MuscleGroup.ARMS, isWorkout = true)
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
                isSelected = false
            ),
            Workout(
                workoutId = 2,
                name = "Chavdo destroy back workout",
                muscleGroup = MuscleGroup.BACK,
                snapshot = "",
                totalExercises = ExerciseGenerator.getTotalExercisesForMuscleGroup(MuscleGroup.BACK),
                isSelected = false
            ),
            Workout(
                workoutId = 3,
                name = "Blow your arms workout",
                muscleGroup = MuscleGroup.ARMS,
                snapshot = "",
                totalExercises = ExerciseGenerator.getTotalExercisesForMuscleGroup(MuscleGroup.ARMS),
                isSelected = true
            )
        )
    }
}