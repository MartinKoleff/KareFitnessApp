package com.koleff.kare_android.common

import com.koleff.kare_android.data.model.dto.ExerciseDetailsDto
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.ExerciseSetDto
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.dto.WorkoutDto
import java.util.UUID
import kotlin.random.Random

object MockupDataGeneratorV2 {

    private val catalogExercises = ExerciseGenerator.getAllExercises(isWorkout = false)
    private val catalogExerciseDetails =
        ExerciseGenerator.getAllExerciseDetails(isWorkout = false)

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

    /**
     * Helper functions
     */

    private fun getRandomExerciseId(muscleGroup: MuscleGroup): Int {
        return if (muscleGroup != MuscleGroup.NONE) Random.nextInt(
            1,
            ExerciseGenerator.TOTAL_EXERCISES
        ) else {
            Random.nextInt(
                ExerciseGenerator.getMuscleGroupRange(muscleGroup).first,
                ExerciseGenerator.getMuscleGroupRange(muscleGroup).second
            )
        }
    }

    private fun isIdExcluded(exerciseId: Int, excludedIds: List<Int>): Boolean =
        excludedIds.isNotEmpty() && excludedIds.contains(exerciseId)

    private fun <T> generateList(n: Int, generator: (Int) -> T): List<T> {
        return List(n) { index ->
            generator(index)
        }
    }

    fun generateMotivationalQuote(): String {
        return motivationalQuotes.random()
    }

    /**
     * Exercises
     */
    fun generateExercise(
        muscleGroup: MuscleGroup = MuscleGroup.getSupportedMuscleGroups().random(),
        excludedIds: List<Int> = emptyList(),
        enableSetIdGeneration: Boolean = true,
        workoutId: Int = Random.nextInt()
    ): ExerciseDto {

        //Generate unique exercise that its id is not contained in the excludedIds list
        var exerciseId = getRandomExerciseId(muscleGroup)
        while (excludedIds.contains(exerciseId)) {
            exerciseId = getRandomExerciseId(muscleGroup)
        }

        val generatedExercise = catalogExercises[exerciseId - 1]
        val generatedExerciseWithSetss = generatedExercise.copy(
            workoutId = workoutId,
            sets = generateExerciseSetsList(
                enableSetIdGeneration = enableSetIdGeneration,
                workoutId = workoutId,
                exerciseId = generatedExercise.exerciseId
            )
        )

        return generatedExerciseWithSetss
    }

    fun generateExerciseList(
        n: Int = 5,
        muscleGroup: MuscleGroup = MuscleGroup.NONE,
        isDistinct: Boolean = false,
        enableSetIdGeneration: Boolean = true,
        workoutId: Int = Random.nextInt(),
        preSelectedExerciseIds: List<Int> = emptyList() //Specific exercises to include
    ): List<ExerciseDto> {
        val exercisesList: MutableList<ExerciseDto> = mutableListOf()

        //Handle pre-selected exercises
        preSelectedExerciseIds.take(n).forEach { exerciseId ->
            catalogExercises.getOrNull(exerciseId - 1)?.let { exercise ->
                val updatedSets = exercise.sets.map { set ->
                    set.copy(workoutId = workoutId)
                }

                val updatedExercise = exercise.copy(workoutId = workoutId, sets = updatedSets)
                exercisesList.add(updatedExercise)
            }
        }

        //Generate additional exercises if needed
        while (exercisesList.size < n) {
            val exercise = generateExercise(
                muscleGroup = muscleGroup,
                excludedIds = exercisesList.map { it.exerciseId },
                enableSetIdGeneration = enableSetIdGeneration,
                workoutId = workoutId
            )

            if (isDistinct && exercisesList.any { it.exerciseId == exercise.exerciseId }) {
                continue //Skip adding this exercise if distinct is required and it's already in the list
            }

            exercisesList.add(exercise)
        }

        return exercisesList
    }

    fun generateExerciseDetails(
        muscleGroup: MuscleGroup = MuscleGroup.NONE,
        excludedIds: List<Int> = emptyList(),
        workoutId: Int = Random.nextInt()
    ): ExerciseDetailsDto {

        //Generate unique exercise that its id is not contained in the excludedIds list
        var exerciseId = getRandomExerciseId(muscleGroup)
        while (excludedIds.contains(exerciseId)) {
            exerciseId = getRandomExerciseId(muscleGroup)
        }

        val generatedExercise = catalogExerciseDetails[exerciseId - 1]
        return generatedExercise.copy(
            workoutId = workoutId
        )
    }

    fun generateExerciseDetailsList(
        n: Int = 5,
        muscleGroup: MuscleGroup = MuscleGroup.NONE,
        workoutId: Int = Random.nextInt()
    ): List<ExerciseDetailsDto> {
        return generateList(n) { currentExerciseDetails ->
            generateExerciseDetails(
                workoutId = workoutId,
                muscleGroup = muscleGroup
            )
        }
    }

    fun getMuscleGroupExercises(
        muscleGroup: MuscleGroup = MuscleGroup.NONE,
        workoutId: Int = Random.nextInt()
    ): List<ExerciseDto> {
        return catalogExercises.filter { exercise ->
            exercise.muscleGroup == muscleGroup
        }
    }

    /**
     * Exercise sets
     */

    fun generateExerciseSet(
        enableSetIdGeneration: Boolean = true,
        workoutId: Int,
        exerciseId: Int,
        number: Int = 1,
        reps: Int = 12,
        weight: Float = 50f
    ): ExerciseSetDto {
        return ExerciseSetDto(
            setId = if (enableSetIdGeneration) UUID.randomUUID() else null,
            workoutId = workoutId,
            exerciseId = exerciseId,
            number = number,
            reps = reps,
            weight = weight
        )
    }

    fun generateExerciseSetsList(
        n: Int = 4,
        workoutId: Int,
        exerciseId: Int,
        enableSetIdGeneration: Boolean = true
    ): List<ExerciseSetDto> {
        return generateList(n) { currentNumber ->
            generateExerciseSet(
                workoutId = workoutId,
                exerciseId = exerciseId,
                number = currentNumber + 1,
                enableSetIdGeneration = enableSetIdGeneration
            )
        }
    }

    /**
     * Workouts
     */
    fun generateWorkout(
        name: String = workoutNames.random(),
        muscleGroup: MuscleGroup = MuscleGroup.getSupportedMuscleGroups().random(),
        totalExercises: Int = Random.nextInt(4, 12),
        isSelected: Boolean = Random.nextBoolean(),
        excludedIds: List<Int> = emptyList()
    ): WorkoutDto {

        //Generate a random workoutId that is not in the excludedIds list
        var workoutId = Random.nextInt(1, 100)
        while (excludedIds.contains(workoutId)) {
            workoutId = Random.nextInt(1, 100)
        }

        return WorkoutDto(
            workoutId = workoutId,
            name = "$name $workoutId",
            muscleGroup = muscleGroup,
            snapshot = "snapshot$workoutId.png",
            totalExercises = totalExercises,
            isSelected = isSelected
        )
    }

    fun generateWorkoutList(n: Int = 5): List<WorkoutDto> {
        return generateList(n) {
            generateWorkout()
        }
    }

    fun generateWorkoutDetails(
        enableSetIdGeneration: Boolean = true,
        excludedIds: List<Int> = emptyList(),
        preSelectedExerciseIds: List<Int> = emptyList()
    ): WorkoutDetailsDto {

        //Generate unique workoutId that is not contained in the excludedIds list
        var workoutId = Random.nextInt(1, 100)
        while (excludedIds.contains(workoutId)) {
            workoutId = Random.nextInt(1, 100)
        }

        val muscleGroup = MuscleGroup.getSupportedMuscleGroups().random()
        val isSelected = Random.nextBoolean()
        val name = workoutNames.random()

        val exercises = generateExerciseList(
            muscleGroup = muscleGroup,
            isDistinct = true,
            enableSetIdGeneration = enableSetIdGeneration,
            workoutId = workoutId,
            preSelectedExerciseIds = preSelectedExerciseIds
        ).sortedBy { it.exerciseId }.toMutableList()

        return WorkoutDetailsDto(
            workoutId = workoutId,
            name = "$name $workoutId",
            description = "Description of $name $workoutId",
            muscleGroup = muscleGroup,
            exercises = exercises,
            isSelected = isSelected
        )
    }

    fun generateWorkoutAndWorkoutDetails(enableSetIdGeneration: Boolean = true): Pair<WorkoutDto, WorkoutDetailsDto> {
        val workoutDetails = generateWorkoutDetails(
            enableSetIdGeneration = enableSetIdGeneration
        )

        val workout = WorkoutDto(
            workoutId = workoutDetails.workoutId,
            name = workoutDetails.name,
            muscleGroup = workoutDetails.muscleGroup,
            snapshot = "snapshot${workoutDetails.workoutId}.png",
            totalExercises = workoutDetails.exercises.size,
            isSelected = workoutDetails.isSelected
        )

        return Pair(workout, workoutDetails)
    }
}