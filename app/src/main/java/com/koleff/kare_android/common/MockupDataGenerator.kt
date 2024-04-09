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

    private val exercises = ExerciseGenerator.getAllExercises(isWorkout = true)
    private val exerciseDetails = ExerciseGenerator.getAllExerciseDetails(isWorkout = true)
    private val catalogExercises = ExerciseGenerator.getAllExercises(isWorkout = false)
    private val catalogExerciseDetails = ExerciseGenerator.getAllExerciseDetails(isWorkout = false)
    private val workoutNames = listOf(
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

    private val motivationalQuotes = listOf(
        "Keep going!",
        "Good job!",
        "The end is near!",
        "Almost done!",
        "You are tough!",
        "Wonderful!",
        "Don't give up!",
        "Hard work pays off!",
        "I already see the progress you have made!",
        "Amazing work!"
    )

    fun generateExercise(
        muscleGroup: MuscleGroup = MuscleGroup.getSupportedMuscleGroups().random(),
        excludedIds: List<Int> = emptyList(),
        isGenerateSetId: Boolean = true,
        isWorkout: Boolean = false,
        workoutId: Int = Random.nextInt()
    ): ExerciseDto {

        var exerciseId =
            Random.nextInt(
                ExerciseGenerator.getMuscleGroupRange(muscleGroup).first,
                ExerciseGenerator.getMuscleGroupRange(muscleGroup).second
            )

        //Generate unique exercise that its id is not contained in the excludedIds list
        if (excludedIds.isNotEmpty()) {
            while (excludedIds.contains(exerciseId)) {
                exerciseId =
                    Random.nextInt(
                        ExerciseGenerator.getMuscleGroupRange(muscleGroup).first,
                        ExerciseGenerator.getMuscleGroupRange(muscleGroup).second
                    )
            }
        }

        val generatedExercise =
            if (isWorkout) catalogExercises[exerciseId - 1] else exercises[exerciseId - 1]

        val generatedWorkoutId = if (isWorkout) workoutId else generatedExercise.workoutId
        val generatedExerciseWithSets = generatedExercise.copy(
            workoutId = generatedWorkoutId,
            sets = generateExerciseSetsList(isGenerateSetId = isGenerateSetId)
        )

        return generatedExerciseWithSets
    }

    fun generateExerciseList(
        n: Int = 5,
        muscleGroup: MuscleGroup = MuscleGroup.NONE,
        isDistinct: Boolean = false,
        isGenerateSetId: Boolean = true,
        isWorkout: Boolean = false,
        workoutId: Int = Random.nextInt(),
        containIds: List<Int> = emptyList()
    ): List<ExerciseDto> {
        val exercisesList: MutableList<ExerciseDto> = mutableListOf()

        val generatePreSelectedExercise: Boolean = containIds.isNotEmpty()
        val totalPreSelectedExercises: Int =
            if(containIds.isNotEmpty() && containIds.size < n) containIds.size else 0

        if(generatePreSelectedExercise) {
            repeat(totalPreSelectedExercises) { currentIndex ->
                val generatedExercise= catalogExercises[containIds[currentIndex] - 1] //Not added in exercises[]

                val generatedWorkoutId = if (isWorkout) workoutId else generatedExercise.workoutId
                val generatedExerciseUpdated = generatedExercise.copy(
                    workoutId = generatedWorkoutId
                )
                exercisesList.add(generatedExerciseUpdated)
            }
        }

        repeat(n - totalPreSelectedExercises) {
            var exercise =
                generateExercise(
                    muscleGroup = muscleGroup,
                    isGenerateSetId = isGenerateSetId,
                    isWorkout = isWorkout,
                    workoutId = workoutId
                )

            //Generate until unique
            if (isDistinct) {
                while (exercisesList.map { it.exerciseId }.contains(exercise.exerciseId)) {
                    exercise = generateExercise(
                        muscleGroup = muscleGroup,
                        isGenerateSetId = isGenerateSetId,
                        isWorkout = isWorkout,
                        workoutId = workoutId
                    )
                }
            }
            exercisesList.add(exercise)
        }

        return exercisesList
    }

    fun generateExerciseDetails(
        muscleGroup: MuscleGroup = MuscleGroup.NONE,
        isWorkout: Boolean = false,
        workoutId: Int
    ): ExerciseDetailsDto {
        val exerciseId = if (muscleGroup != MuscleGroup.NONE) Random.nextInt(
            1,
            ExerciseGenerator.TOTAL_EXERCISES
        ) else {
            Random.nextInt(
                ExerciseGenerator.getMuscleGroupRange(muscleGroup).first,
                ExerciseGenerator.getMuscleGroupRange(muscleGroup).second
            )
        }

        val generatedExercise =
            if (isWorkout) catalogExerciseDetails[exerciseId - 1] else exerciseDetails[exerciseId - 1]
        val generatedWorkoutId = if (isWorkout) workoutId else generatedExercise.workoutId

        return generatedExercise.copy(workoutId = generatedWorkoutId)
    }

    fun generateExerciseDetailsList(
        n: Int = 5,
        muscleGroup: MuscleGroup = MuscleGroup.NONE,
        isWorkout: Boolean = true,
        workoutId: Int = Random.nextInt()
    ): List<ExerciseDetailsDto> {
        val exerciseDetailsList: MutableList<ExerciseDetailsDto> = mutableListOf()

        repeat(n) {
            exerciseDetailsList.add(generateExerciseDetails(muscleGroup, isWorkout, workoutId))
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
        n: Int = 4,
        isGenerateSetId: Boolean = true
    ): List<ExerciseSetDto> {
        val exerciseSetList = listOf(
            generateExerciseSet(isGenerateSetId, 1, 12, 50f),
            generateExerciseSet(isGenerateSetId, 2, 10, 55.5f),
            generateExerciseSet(isGenerateSetId, 3, 8, 60f),
            generateExerciseSet(isGenerateSetId, 4, 1, 80f),
        )

        return exerciseSetList
    }

    fun generateExerciseSet(
        isGenerateSetId: Boolean = true,
        number: Int = 1,
        reps: Int = 12,
        weight: Float = 50f
    ): ExerciseSetDto {
        return ExerciseSetDto(
            if (isGenerateSetId) UUID.randomUUID() else null,
            number,
            reps,
            weight
        )
    }

    fun generateWorkout(
        isSelected: Boolean = Random.nextBoolean(),
        excludedIds: List<Int> = emptyList()
    ): WorkoutDto {
        var workoutId = Random.nextInt(1, 100)

        //No duplicate IDs
        if (excludedIds.isNotEmpty()) {
            while (excludedIds.contains(workoutId)) {
                workoutId = Random.nextInt(1, 100)
            }
        }
        val totalExercises = Random.nextInt(4, 12)
        val muscleGroup = MuscleGroup.getSupportedMuscleGroups().random()
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

    fun generateWorkoutDetails(
        isGenerateSetId: Boolean = true,
        containIds: List<Int> = emptyList()
    ): WorkoutDetailsDto {
        val workoutId = Random.nextInt(1, 100)
        val muscleGroup = ExerciseGenerator.SUPPORTED_MUSCLE_GROUPS.random()
        val isSelected = Random.nextBoolean()
        val exercises = generateExerciseList(
            muscleGroup = muscleGroup,
            isDistinct = true,
            isGenerateSetId = isGenerateSetId,
            isWorkout = true,
            workoutId = workoutId,
            containIds = containIds
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

    fun generateMotivationalQuote(): String {
        return motivationalQuotes.random()
    }
}