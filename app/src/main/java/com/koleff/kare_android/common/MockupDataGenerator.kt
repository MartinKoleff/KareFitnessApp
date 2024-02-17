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
object MockupDataGenerator {

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
        "MuscleMania workout"
    )

    fun generateExercise(
        muscleGroup: MuscleGroup = MuscleGroup.NONE,
        isGenerateSetId: Boolean = true,
    ): ExerciseDto {
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
//            .copy(
//            snapshot = "exercise $exerciseId.png"
//        ) //Ruins assertion

        val generatedExerciseWithSets = generatedExercise.copy(
            sets = generateExerciseSetsList(isGenerateSetId = isGenerateSetId)
        )

        return generatedExerciseWithSets
    }

    fun generateExerciseList(
        n: Int = 5,
        muscleGroup: MuscleGroup = MuscleGroup.NONE,
        isDistinct: Boolean = false,
        isGenerateSetId: Boolean = true
    ): List<ExerciseDto> {
        val exercisesList: MutableList<ExerciseDto> = mutableListOf()

        repeat(n) {
            var exercise = generateExercise(muscleGroup, isGenerateSetId)

            //Generate until unique
            if (isDistinct) {
                while (exercisesList.map { it.exerciseId }.contains(exercise.exerciseId)) {
                    exercise = generateExercise(muscleGroup, isGenerateSetId)
                }
            }
            exercisesList.add(exercise)
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

    fun generateExerciseSetsList(
        n: Int = 3,
        isGenerateSetId: Boolean = true
    ): List<ExerciseSetDto> {
        val exerciseSetList = listOf(
            ExerciseSetDto(if (isGenerateSetId) UUID.randomUUID() else null, 1, 12, 50f),
            ExerciseSetDto(if (isGenerateSetId) UUID.randomUUID() else null, 2, 10, 55.5f),
            ExerciseSetDto(if (isGenerateSetId) UUID.randomUUID() else null, 3, 8, 60f)
        )

        return exerciseSetList
    }

    fun generateExerciseSet(): ExerciseSetDto {
        return ExerciseSetDto(UUID.randomUUID(), 1, 12, 50f)
    }

    fun generateWorkout(): WorkoutDto {
        val workoutId = Random.nextInt(1, 100)
        val totalExercises = Random.nextInt(4, 12)
        val muscleGroup = MuscleGroup.getSupportedMuscleGroups().random()
        val isSelected = Random.nextBoolean()
        val name = workoutNames.random()

        val workout =
            WorkoutDto(
                workoutId = workoutId,
                name = "$name $workoutId",
                muscleGroup = muscleGroup,
                snapshot = "snapshot$workoutId.png",
                totalExercises = totalExercises,
                isSelected = isSelected
            )

        return workout
    }

    fun generateWorkoutDetails(isGenerateSetId: Boolean = true): WorkoutDetailsDto {
        val workoutId = Random.nextInt(1, 100)
        val muscleGroup = ExerciseGenerator.SUPPORTED_MUSCLE_GROUPS.random()
        val isSelected = Random.nextBoolean()
        val exercises = generateExerciseList(
            muscleGroup = muscleGroup,
            isDistinct = true,
            isGenerateSetId = isGenerateSetId
        ).sortedBy { it.exerciseId } as MutableList<ExerciseDto>
        val name = workoutNames.random()

        val workoutDetails =
            WorkoutDetailsDto(
                workoutId = workoutId,
                name = "$name $workoutId",
                description = "Description",
                muscleGroup = muscleGroup,
                exercises = exercises,
                isSelected = isSelected,
            )

        return workoutDetails
    }

    fun generateWorkoutAndWorkoutDetails(isGenerateSetId: Boolean = true): Pair<WorkoutDto, WorkoutDetailsDto> {
        val workoutDetails = generateWorkoutDetails(isGenerateSetId)

        val workout = WorkoutDto(
            workoutId = workoutDetails.workoutId,
            name = workoutDetails.name,
            muscleGroup = workoutDetails.muscleGroup,
            snapshot = "snapshot ${workoutDetails.workoutId}.png",
            totalExercises = workoutDetails.exercises.size,
            isSelected = workoutDetails.isSelected
        )

        return Pair(workout, workoutDetails)
    }
}