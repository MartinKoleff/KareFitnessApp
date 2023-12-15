package com.koleff.kare_android.common

import com.koleff.kare_android.data.model.dto.ExerciseDetailsDto
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.MachineType
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.dto.WorkoutDto

object MockupDataGenerator {
    fun generateExercise(): ExerciseDto {
        return ExerciseDto(
            1,
            "BARBELL BENCH PRESS 1",
            MuscleGroup.CHEST,
            MachineType.BARBELL,
            ""
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
}