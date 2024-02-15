package com.koleff.kare_android.common

import com.koleff.kare_android.data.model.dto.ExerciseDetailsDto
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.ExerciseSetDto
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.dto.WorkoutDto
import java.util.UUID
import kotlin.random.Random

//TODO: move to unit test directory...
object MockupDataGenerator{

    private val exercises = ExerciseGenerator.getAllExercises()
    private val exerciseDetails = ExerciseGenerator.getAllExerciseDetails()
    val workoutNames = listOf(
        "Epic workout",
        "Koleff destroy your back workout",
        "Emil Krustev full body workout",
        "Blow your arms workout",
        "Upper body gods workout",
        "Leg day = taxi day",
        "Calisthenics workout",
        "Powerlifters workout",
        "Military workout",
        "Strongman workout",
        "Push workout",
        "Pull workout",
        "Arms workout",
        "Chest and shoulders workout",
        "Chest, shoulders and triceps workout",
        "Legs workout",
        "Back workout",
        "Musclemania workout"
    )

    fun generateExercise(muscleGroup: MuscleGroup = MuscleGroup.NONE): ExerciseDto {
        val exerciseId = if (muscleGroup != MuscleGroup.NONE) Random.nextInt(
            1,
            ExerciseGenerator.TOTAL_EXERCISES
        ) else {
            Random.nextInt(
                ExerciseGenerator.getMuscleGroupRange(muscleGroup).first,
                ExerciseGenerator.getMuscleGroupRange(muscleGroup).second
            )
        }

        val generatedExercise = exercises[exerciseId - 1]

        val generatedExerciseWithSets = generatedExercise.copy(
            sets = generateExerciseSetsList()
        )

        return generatedExerciseWithSets
    }

    fun generateExerciseList(
        n: Int = 5,
        muscleGroup: MuscleGroup = MuscleGroup.NONE
    ): List<ExerciseDto> {
        val exercisesList: MutableList<ExerciseDto> = mutableListOf()

        repeat(n) {
            exercisesList.add(generateExercise(muscleGroup))
        }

        return exercisesList
    }

    fun generateExerciseDetails(muscleGroup: MuscleGroup = MuscleGroup.NONE): ExerciseDetailsDto {
        val exerciseId = if (muscleGroup != MuscleGroup.NONE) Random.nextInt(
            1,
            ExerciseGenerator.TOTAL_EXERCISES
        ) else {
            Random.nextInt(
                ExerciseGenerator.getMuscleGroupRange(muscleGroup).first,
                ExerciseGenerator.getMuscleGroupRange(muscleGroup).second
            )
        }

        val generatedExercise = exerciseDetails[exerciseId - 1]
        return generatedExercise
    }

    fun generateExerciseDetailsList(
        n: Int = 5,
        muscleGroup: MuscleGroup = MuscleGroup.NONE
    ): List<ExerciseDetailsDto> {
        val exerciseDetailsList: MutableList<ExerciseDetailsDto> = mutableListOf()

        repeat(n) {
            exerciseDetailsList.add(generateExerciseDetails(muscleGroup))
        }

        return exerciseDetailsList
    }

    fun generateWorkoutList(n: Int = 5): List<WorkoutDto> {
        val workoutList: MutableList<WorkoutDto> = mutableListOf()

        repeat(n) {
            val workout = generateWorkout()
            workoutList.add(workout)
        }

        return workoutList
    }

    fun generateExerciseSetsList(n: Int = 3): List<ExerciseSetDto> {
        val exerciseSetList = listOf(
            ExerciseSetDto(UUID.randomUUID(), 1, 12, 50f),
            ExerciseSetDto(UUID.randomUUID(), 2, 10, 55.5f),
            ExerciseSetDto(UUID.randomUUID(), 3, 8, 60f)
        )

        return exerciseSetList
    }

    fun generateExerciseSet(): ExerciseSetDto {
        return ExerciseSetDto(UUID.randomUUID(), 1, 12, 50f)
    }

    fun generateWorkout(): WorkoutDto {
        val workoutId = Random.nextInt(1, 100)
        val totalExercises = Random.nextInt(4, 12)
        val muscleGroupId = Random.nextInt(1, 14)
        val isSelected = Random.nextBoolean()
        val name = workoutNames.random()

        val workout =
            WorkoutDto(
                workoutId = workoutId,
                name = "$name $workoutId",
                muscleGroup = MuscleGroup.fromId(muscleGroupId),
                snapshot = "snapshot$workoutId.png",
                totalExercises = totalExercises,
                isSelected = isSelected
            )

        return workout
    }

    fun generateWorkoutDetails(): WorkoutDetailsDto {
        val workoutId = Random.nextInt(1, 100)
        val muscleGroup = ExerciseGenerator.SUPPORTED_MUSCLE_GROUPS.random()
        val isSelected = Random.nextBoolean()
        val exercises = generateExerciseList(muscleGroup = muscleGroup) as MutableList<ExerciseDto>
        val name = workoutNames.random()

        val workoutDetails =
            WorkoutDetailsDto(
                workoutId = workoutId,
                name = "$name $workoutId",
                description = "Description",
                muscleGroup = muscleGroup,
                exercises = exercises,
                isSelected = isSelected
            )

        return workoutDetails
    }
}