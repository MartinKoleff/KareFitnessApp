package com.koleff.kare_android.common

import com.koleff.kare_android.data.model.dto.ExerciseDetailsDto
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.ExerciseSetDto
import com.koleff.kare_android.data.model.dto.MachineType
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.dto.WorkoutDto
import java.util.UUID

//TODO: integrate with mockk() to be random...
object MockupDataGenerator { //TODO: move to unit test directory...
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

    fun generateExerciseList(muscleGroupId: Int = 0): List<ExerciseDto>{
        val n = 5
        val exercisesList: MutableList<ExerciseDto> = mutableListOf()

        repeat(n) { index ->
            val currentExercise =
                ExerciseDto(
                    index,
                    "BARBELL BENCH PRESS $index",
                    MuscleGroup.fromId(muscleGroupId),
                    MachineType.BARBELL,
                    ""
                )
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

    fun generateExerciseSetsList(): List<ExerciseSetDto> {
        val exerciseSetList = listOf(
            ExerciseSetDto(UUID.randomUUID(),1, 12, 50f),
            ExerciseSetDto(UUID.randomUUID(),2, 10, 55.5f),
            ExerciseSetDto(UUID.randomUUID(),3, 8, 60f)
        )

        return exerciseSetList
    }

    fun generateExerciseSet(): ExerciseSetDto {
        return ExerciseSetDto(UUID.randomUUID(),1, 12, 50f)
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

    fun generateWorkoutDetails(): WorkoutDetailsDto {
        val workoutId = 1
        val exercises = generateExerciseList() as MutableList<ExerciseDto>

        val workoutDetails =
            WorkoutDetailsDto(
                workoutId = workoutId,
                name = "Epic workout $workoutId",
                description = "Description",
                muscleGroup = MuscleGroup.fromId(workoutId),
                exercises = exercises,
                isSelected = false
            )

        return workoutDetails
    }
}