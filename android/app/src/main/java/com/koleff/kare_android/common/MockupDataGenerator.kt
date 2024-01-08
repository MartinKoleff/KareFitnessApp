package com.koleff.kare_android.common

import com.koleff.kare_android.data.model.dto.ExerciseDetailsDto
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.ExerciseSet
import com.koleff.kare_android.data.model.dto.MachineType
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.dto.WorkoutDto
import java.util.UUID

object MockupDataGenerator {
    fun generateExercise(): ExerciseDto {
        val exerciseId = 1

        return ExerciseDto(
            exerciseId,
            "BARBELL BENCH PRESS $exerciseId",
            MuscleGroup.CHEST,
            MachineType.BARBELL,
            "",
            sets = generateExerciseSetsList()
        )
    }

    fun generateExerciseList(): List<ExerciseDto> {
        val n = 5
        val exercisesList: MutableList<ExerciseDto> = mutableListOf()

        repeat(n) { index ->
            val currentExercise =
                ExerciseDto(
                    index,
                    "BARBELL BENCH PRESS $index",
                    MuscleGroup.fromId(index + 1),
                    MachineType.BARBELL,
                    ""
                )
            exercisesList.add(currentExercise)
            exercisesList.add(currentExercise)
        }

        return exercisesList
    }

    fun generateExerciseDetails(): ExerciseDetailsDto {
        return ExerciseDetailsDto(
            id = 1,
            name = "BARBELL BENCH PRESS",
            description = "",
            muscleGroup = MuscleGroup.CHEST,
            machineType = MachineType.BARBELL,
            videoUrl = ""
        )
    }

    fun generateWorkoutList(): List<WorkoutDto> {
        val n = 5
        val workoutList: MutableList<WorkoutDto> = mutableListOf()

        repeat(n) { index ->
            val currentWorkout =
                WorkoutDto(
                    workoutId = index,
                    name = "Epic workout $index",
                    muscleGroup = MuscleGroup.fromId(index + 1),
                    snapshot = "",
                    totalExercises = index,
                    isSelected = false
                )
            workoutList.add(currentWorkout)
            workoutList.add(currentWorkout)
        }

        return workoutList
    }

    fun generateExerciseSetsList(): List<ExerciseSet> {
        val exerciseSetList = listOf(
            ExerciseSet(UUID.randomUUID(),1, 12, 50f),
            ExerciseSet(UUID.randomUUID(),2, 10, 55.5f),
            ExerciseSet(UUID.randomUUID(),3, 8, 60f)
        )

        return exerciseSetList
    }

    fun generateExerciseSet(): ExerciseSet {
        return ExerciseSet(UUID.randomUUID(),1, 12, 50f)
    }

    fun generateWorkout(): WorkoutDto {
        val workoutId = 1
        val totalExercises = 5
        val workout =
            WorkoutDto(
                workoutId = workoutId,
                name = "Epic workout $workoutId",
                muscleGroup = MuscleGroup.fromId(workoutId),
                snapshot = "",
                totalExercises = totalExercises,
                isSelected = false
            )

        return workout
    }
}