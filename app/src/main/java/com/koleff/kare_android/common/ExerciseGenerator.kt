package com.koleff.kare_android.common

import com.koleff.kare_android.data.model.dto.ExerciseDetailsDto
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.MachineType
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.room.entity.Exercise
import com.koleff.kare_android.data.room.entity.ExerciseDetails
import com.koleff.kare_android.data.room.entity.ExerciseSet
import com.koleff.kare_android.data.room.entity.ExerciseWithSets
import com.koleff.kare_android.data.room.entity.relations.ExerciseDetailsExerciseCrossRef
import com.koleff.kare_android.data.room.entity.relations.ExerciseSetCrossRef
import java.util.UUID
import kotlin.random.Random

object ExerciseGenerator {

    const val TOTAL_EXERCISES = 60

    //Used for testing
    private const val description =
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc interdum nibh nec pharetra iaculis. Aenean ultricies egestas leo at ultricies. Quisque suscipit, purus ut congue porta, eros eros tincidunt sem, sed commodo magna metus eu nibh. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Vestibulum quis velit eget eros malesuada luctus. Suspendisse iaculis ullamcorper condimentum. Sed metus augue, dapibus eu venenatis vitae, ornare non turpis. Donec suscipit iaculis dolor, id fermentum mauris interdum in. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas."
    private const val videoUrl = "_FkbD0FhgVE" //https://www.youtube.com/watch?v=
    private const val videoUrl2 = "8GEKQJcKTO8" //https://www.youtube.com/watch?v=


    //List of all muscle groups with exercises ranges setup in getMuscleGroupRange()
    val SUPPORTED_MUSCLE_GROUPS = listOf<MuscleGroup>(
        MuscleGroup.CHEST,
        MuscleGroup.BACK,
        MuscleGroup.TRICEPS,
        MuscleGroup.BICEPS,
        MuscleGroup.ARMS,
        MuscleGroup.SHOULDERS,
        MuscleGroup.LEGS,
        MuscleGroup.FULL_BODY,
    )

    fun loadExercises(
        muscleGroup: MuscleGroup,
        isWorkout: Boolean,
        workoutId: Int = -1
    ): List<Exercise> {
        val customWorkoutId =
            if (isWorkout) {
                if (workoutId != -1) {
                    workoutId
                } else {
                    Random.nextInt()
                }
            } else {
                Constants.CATALOG_EXERCISE_ID
            }

        return when (muscleGroup) {
            MuscleGroup.CHEST -> getChestExercises(customWorkoutId)
            MuscleGroup.BACK -> getBackExercises(customWorkoutId)
            MuscleGroup.TRICEPS -> getTricepsExercises(customWorkoutId)
            MuscleGroup.BICEPS -> getBicepsExercises(customWorkoutId)
            MuscleGroup.SHOULDERS -> getShoulderExercises(customWorkoutId)
            MuscleGroup.LEGS -> getLegsExercises(customWorkoutId)
            MuscleGroup.ARMS -> {
                val bicepsExercises = getBicepsExercises(customWorkoutId)
                val tricepsExercises = getTricepsExercises(customWorkoutId)

                ArrayList<Exercise>().apply {
                    addAll(bicepsExercises)
                    addAll(tricepsExercises)
                }
            }

            else -> emptyList()
        }
    }


    fun loadExercisesWithSets(
        muscleGroup: MuscleGroup,
        isWorkout: Boolean,
        workoutId: Int
    ): List<ExerciseWithSets> {
        val exercises = loadExercises(muscleGroup, isWorkout, workoutId)
        val exercisesWithSets = exercises.map { exercise ->
            val sets = loadExerciseSets(
                exerciseId = exercise.exerciseId,
                workoutId = exercise.workoutId
            )

            ExerciseWithSets(
                exercise = exercise,
                sets = sets
            )
        }

        return exercisesWithSets
    }

    fun loadExerciseDetails(
        muscleGroup: MuscleGroup,
        isWorkout: Boolean,
        workoutId: Int = 1
    ): List<ExerciseDetails> {
        val customWorkoutId =
            if (isWorkout) {
                if (workoutId != -1) {
                    workoutId
                } else {
                    Random.nextInt()
                }
            } else {
                Constants.CATALOG_EXERCISE_ID
            }

        return when (muscleGroup) {
            MuscleGroup.CHEST -> getChestExerciseDetails(customWorkoutId)
            MuscleGroup.BACK -> getBackExerciseDetails(customWorkoutId)
            MuscleGroup.TRICEPS -> getTricepsExerciseDetails(customWorkoutId)
            MuscleGroup.BICEPS -> getBicepsExerciseDetails(customWorkoutId)
            MuscleGroup.SHOULDERS -> getShoulderExerciseDetails(customWorkoutId)
            MuscleGroup.LEGS -> getLegsExerciseDetails(customWorkoutId)
            else -> emptyList()
        }
    }

    fun getAllExercises(isWorkout: Boolean = false): List<ExerciseDto> {
        val exercisesList = mutableListOf<ExerciseDto>()

        for (muscleGroup in MuscleGroup.entries) {
            val generatedExercises =
                loadExercises(muscleGroup, isWorkout)
                    .map { exercise ->
                        val exerciseSets = loadExerciseSets(
                            exerciseId = exercise.exerciseId,
                            workoutId = exercise.workoutId
                        )

                        exercise.toDto(exerciseSets)
                    }
                    .toList()

            exercisesList.addAll(generatedExercises)
        }

        return exercisesList
    }

    fun getAllExerciseDetails(isWorkout: Boolean = false): List<ExerciseDetailsDto> {
        val exercisesDetailsList = mutableListOf<ExerciseDetailsDto>()

        for (muscleGroup in MuscleGroup.entries) {
            val generatedExerciseDetails =
                loadExerciseDetails(muscleGroup, isWorkout)
                    .map(ExerciseDetails::toDto)
                    .toList()

            exercisesDetailsList.addAll(generatedExerciseDetails)
        }

        return exercisesDetailsList
    }

    fun getMuscleGroupRange(muscleGroup: MuscleGroup): Pair<Int, Int> {
        return when (muscleGroup) {
            MuscleGroup.CHEST -> Pair(1, 10)
            MuscleGroup.BACK -> Pair(11, 21)
            MuscleGroup.TRICEPS -> Pair(22, 31)
            MuscleGroup.BICEPS -> Pair(32, 40)
            MuscleGroup.ARMS -> Pair(22, 40)
            MuscleGroup.SHOULDERS -> Pair(41, 50)
            MuscleGroup.LEGS -> Pair(51, 60)
            MuscleGroup.FULL_BODY -> Pair(1, 60)
            else -> throw NoSuchElementException("Muscle Group $muscleGroup doesn't have exercises")
        }
    }

    fun getTotalExercisesForMuscleGroup(muscleGroup: MuscleGroup): Int {
        return when (muscleGroup) {
            MuscleGroup.CHEST,
            MuscleGroup.BACK,
            MuscleGroup.TRICEPS,
            MuscleGroup.BICEPS,
            MuscleGroup.ARMS,
            MuscleGroup.SHOULDERS,
            MuscleGroup.LEGS,
            MuscleGroup.FULL_BODY -> {
                val totalExercisesRange = getMuscleGroupRange(muscleGroup)

                totalExercisesRange.second - totalExercisesRange.first + 1
            }

            MuscleGroup.ALL -> TOTAL_EXERCISES
            else -> throw NoSuchElementException("Muscle Group $muscleGroup doesn't have exercises")
        }
    }

    fun loadExerciseSets(workoutId: Int, exerciseId: Int): List<ExerciseSet> {
        return listOf(
            ExerciseSet(
                setId = UUID.randomUUID(),
                exerciseId = exerciseId,
                workoutId = workoutId,
                number = 1,
                reps = 12,
                weight = 0.0f
            ),
            ExerciseSet(
                setId = UUID.randomUUID(),
                exerciseId = exerciseId,
                workoutId = workoutId,
                number = 2,
                reps = 10,
                weight = 0.0f
            ),
            ExerciseSet(
                setId = UUID.randomUUID(),
                exerciseId = exerciseId,
                workoutId = workoutId,
                number = 3,
                reps = 8,
                weight = 0.0f
            ),
            ExerciseSet(
                setId = UUID.randomUUID(),
                exerciseId = exerciseId,
                workoutId = workoutId,
                number = 4,
                reps = 1,
                weight = 50f
            ),
        )
    }

    private fun getLegsExerciseDetails(
        customWorkoutId: Int
    ): List<ExerciseDetails> {
        return listOf(
            ExerciseDetails(
                exerciseDetailsId = 51,
                workoutId = customWorkoutId,
                name = "Squat",
                description = description,
                muscleGroup = MuscleGroup.LEGS,
                machineType = MachineType.BARBELL,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                exerciseDetailsId = 52,
                workoutId = customWorkoutId,
                name = "Bulgarian split squad",
                description = description,
                muscleGroup = MuscleGroup.LEGS,
                machineType = MachineType.DUMBBELL,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                exerciseDetailsId = 53,
                workoutId = customWorkoutId,
                name = "Smith machine squad",
                description = description,
                muscleGroup = MuscleGroup.LEGS,
                machineType = MachineType.MACHINE,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                exerciseDetailsId = 54,
                workoutId = customWorkoutId,
                name = "Leg extension",
                description = description,
                muscleGroup = MuscleGroup.LEGS,
                machineType = MachineType.MACHINE,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                exerciseDetailsId = 55,
                workoutId = customWorkoutId,
                name = "Kettlebell walking lunges",
                description = description,
                muscleGroup = MuscleGroup.LEGS,
                machineType = MachineType.DUMBBELL,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                exerciseDetailsId = 56,
                workoutId = customWorkoutId,
                name = "Leg press",
                description = description,
                muscleGroup = MuscleGroup.LEGS,
                machineType = MachineType.MACHINE,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                exerciseDetailsId = 57,
                workoutId = customWorkoutId,
                name = "Prone leg curl",
                description = description,
                muscleGroup = MuscleGroup.LEGS,
                machineType = MachineType.MACHINE,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                exerciseDetailsId = 58,
                workoutId = customWorkoutId,
                name = "Seated calf raises",
                description = description,
                muscleGroup = MuscleGroup.LEGS,
                machineType = MachineType.MACHINE,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                exerciseDetailsId = 59,
                workoutId = customWorkoutId,
                name = "Standing calf raises",
                description = description,
                muscleGroup = MuscleGroup.LEGS,
                machineType = MachineType.CALISTHENICS,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                exerciseDetailsId = 60,
                workoutId = customWorkoutId,
                name = "Barbell standing calf raises",
                description = description,
                muscleGroup = MuscleGroup.LEGS,
                machineType = MachineType.BARBELL,
                videoUrl = videoUrl
            ),
        )
    }

    private fun getLegsExercises(customWorkoutId: Int): List<Exercise> { //TODO: video for all...
        return listOf(
            Exercise(
                51,
                workoutId = customWorkoutId,
                "Squat",
                muscleGroup = MuscleGroup.LEGS,
                machineType = MachineType.BARBELL,
                ""
            ),
            Exercise(
                52,
                workoutId = customWorkoutId,
                "Bulgarian split squad",
                muscleGroup = MuscleGroup.LEGS,
                machineType = MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                53,
                workoutId = customWorkoutId,
                "Smith machine squad",
                muscleGroup = MuscleGroup.LEGS,
                machineType = MachineType.MACHINE,
                ""
            ),
            Exercise(
                54,
                workoutId = customWorkoutId,
                "Leg extension",
                muscleGroup = MuscleGroup.LEGS,
                machineType = MachineType.MACHINE,
                ""
            ),
            Exercise(
                55,
                workoutId = customWorkoutId,
                "Kettlebell walking lunges",
                muscleGroup = MuscleGroup.LEGS,
                machineType = MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                56,
                workoutId = customWorkoutId,
                "Leg press",
                muscleGroup = MuscleGroup.LEGS,
                machineType = MachineType.MACHINE,
                ""
            ),
            Exercise(
                57,
                workoutId = customWorkoutId,
                "Prone leg curl",
                muscleGroup = MuscleGroup.LEGS,
                machineType = MachineType.MACHINE,
                ""
            ),
            Exercise(
                58,
                workoutId = customWorkoutId,
                "Seated calf raises",
                muscleGroup = MuscleGroup.LEGS,
                machineType = MachineType.MACHINE,
                ""
            ),
            Exercise(
                59,
                workoutId = customWorkoutId,
                "Standing calf raises",
                muscleGroup = MuscleGroup.LEGS,
                machineType = MachineType.CALISTHENICS,
                ""
            ),
            Exercise(
                60,
                workoutId = customWorkoutId,
                "Barbell standing calf raises",
                muscleGroup = MuscleGroup.LEGS,
                machineType = MachineType.BARBELL,
                ""
            ),
        )
    }

    private fun getShoulderExerciseDetails(customWorkoutId: Int): List<ExerciseDetails> {
        return listOf(
            ExerciseDetails(
                exerciseDetailsId = 41,
                workoutId = customWorkoutId,
                name = "Barbell upright row",
                description = description,
                muscleGroup = MuscleGroup.SHOULDERS,
                machineType = MachineType.BARBELL,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                exerciseDetailsId = 42,
                workoutId = customWorkoutId,
                name = "Dumbbell front raises",
                description = description,
                muscleGroup = MuscleGroup.SHOULDERS,
                machineType = MachineType.DUMBBELL,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                exerciseDetailsId = 43,
                workoutId = customWorkoutId,
                name = "Dumbbell lateral raises",
                description = description,
                muscleGroup = MuscleGroup.SHOULDERS,
                machineType = MachineType.DUMBBELL,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                exerciseDetailsId = 44,
                workoutId = customWorkoutId,
                name = "Seated dumbbell shoulder press",
                description = description,
                muscleGroup = MuscleGroup.SHOULDERS,
                machineType = MachineType.DUMBBELL,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                exerciseDetailsId = 45,
                workoutId = customWorkoutId,
                name = "Barbell shoulder press",
                description = description,
                muscleGroup = MuscleGroup.SHOULDERS,
                machineType = MachineType.BARBELL,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                exerciseDetailsId = 46,
                workoutId = customWorkoutId,
                name = "Face pull",
                description = description,
                muscleGroup = MuscleGroup.SHOULDERS,
                machineType = MachineType.MACHINE,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                exerciseDetailsId = 47,
                workoutId = customWorkoutId,
                name = "Front plate raise",
                description = description,
                muscleGroup = MuscleGroup.SHOULDERS,
                machineType = MachineType.CALISTHENICS,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                exerciseDetailsId = 48,
                workoutId = customWorkoutId,
                name = "One arm lateral raises at the low pulley cable",
                description = description,
                muscleGroup = MuscleGroup.SHOULDERS,
                machineType = MachineType.MACHINE,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                exerciseDetailsId = 49,
                workoutId = customWorkoutId,
                name = "Reverse pec deck",
                description = description,
                muscleGroup = MuscleGroup.SHOULDERS,
                machineType = MachineType.MACHINE,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                exerciseDetailsId = 50,
                workoutId = customWorkoutId,
                name = "Dumbbell behind the back press",
                description = description,
                muscleGroup = MuscleGroup.SHOULDERS,
                machineType = MachineType.DUMBBELL,
                videoUrl = videoUrl
            )
        )
    }

    private fun getShoulderExercises(customWorkoutId: Int): List<Exercise> {
        return listOf(
            Exercise(
                41,
                workoutId = customWorkoutId,
                "Barbell upright row",
                muscleGroup = MuscleGroup.SHOULDERS,
                machineType = MachineType.BARBELL,
                ""
            ),
            Exercise(
                42,
                workoutId = customWorkoutId,
                "Dumbbell front raises",
                muscleGroup = MuscleGroup.SHOULDERS,
                machineType = MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                43,
                workoutId = customWorkoutId,
                "Dumbbell lateral raises",
                muscleGroup = MuscleGroup.SHOULDERS,
                machineType = MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                44,
                workoutId = customWorkoutId,
                "Seated dumbbell shoulder press",
                muscleGroup = MuscleGroup.SHOULDERS,
                machineType = MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                45,
                workoutId = customWorkoutId,
                "Barbell shoulder press",
                muscleGroup = MuscleGroup.SHOULDERS,
                machineType = MachineType.BARBELL,
                ""
            ),
            Exercise(
                46,
                workoutId = customWorkoutId,
                "Face pull",
                muscleGroup = MuscleGroup.SHOULDERS,
                machineType = MachineType.MACHINE,
                ""
            ),
            Exercise(
                47,
                workoutId = customWorkoutId,
                "Front plate raise",
                muscleGroup = MuscleGroup.SHOULDERS,
                machineType = MachineType.CALISTHENICS,
                ""
            ),
            Exercise(
                48,
                workoutId = customWorkoutId,
                "One arm lateral raises at the low pulley cable",
                muscleGroup = MuscleGroup.SHOULDERS,
                machineType = MachineType.MACHINE,
                ""
            ),
            Exercise(
                49,
                workoutId = customWorkoutId,
                "Reverse pec deck",
                muscleGroup = MuscleGroup.SHOULDERS,
                machineType = MachineType.MACHINE,
                ""
            ),
            Exercise(
                50,
                workoutId = customWorkoutId,
                "Dumbbell behind the back press", //TODO: video...
                muscleGroup = MuscleGroup.SHOULDERS,
                machineType = MachineType.DUMBBELL,
                ""
            )
        )
    }

    private fun getBicepsExerciseDetails(customWorkoutId: Int): List<ExerciseDetails> {
        return listOf(
            ExerciseDetails(
                32,
                workoutId = customWorkoutId,
                "Standing dumbbell biceps curl",
                description = description,
                muscleGroup = MuscleGroup.BICEPS,
                machineType = MachineType.DUMBBELL,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                33,
                workoutId = customWorkoutId,
                "Sitting dumbbell biceps curl",
                description = description,
                muscleGroup = MuscleGroup.BICEPS,
                machineType = MachineType.DUMBBELL,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                34,
                workoutId = customWorkoutId,
                "Barbell biceps curl",
                description = description,
                muscleGroup = MuscleGroup.BICEPS,
                machineType = MachineType.BARBELL,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                35,
                workoutId = customWorkoutId,
                "Dumbbell concentrated curl",
                description = description,
                muscleGroup = MuscleGroup.BICEPS,
                machineType = MachineType.DUMBBELL,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                36,
                workoutId = customWorkoutId,
                "Dumbbell hammer curl",
                description = description,
                muscleGroup = MuscleGroup.BICEPS,
                machineType = MachineType.DUMBBELL,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                37,
                workoutId = customWorkoutId,
                "Dumbbell hammer curl",
                description = description,
                muscleGroup = MuscleGroup.BICEPS,
                machineType = MachineType.DUMBBELL,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                38,
                workoutId = customWorkoutId,
                "One arm dumbbell preacher curl",
                description = description,
                muscleGroup = MuscleGroup.BICEPS,
                machineType = MachineType.DUMBBELL,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                39,
                workoutId = customWorkoutId,
                "Barbell preacher curl",
                description = description,
                muscleGroup = MuscleGroup.BICEPS,
                machineType = MachineType.BARBELL,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                40,
                workoutId = customWorkoutId,
                "Reverse grip biceps curl at the low pulley cable",
                description = description,
                muscleGroup = MuscleGroup.BICEPS,
                machineType = MachineType.MACHINE,
                videoUrl = videoUrl
            )
        )
    }

    private fun getBicepsExercises(customWorkoutId: Int): List<Exercise> {
        return listOf(
            Exercise(
                32,
                workoutId = customWorkoutId,
                "Standing dumbbell biceps curl",
                muscleGroup = MuscleGroup.BICEPS,
                machineType = MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                33,
                workoutId = customWorkoutId,
                "Sitting dumbbell biceps curl",
                muscleGroup = MuscleGroup.BICEPS,
                machineType = MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                34,
                workoutId = customWorkoutId,
                "Barbell biceps curl",
                muscleGroup = MuscleGroup.BICEPS,
                machineType = MachineType.BARBELL,
                ""
            ),
            Exercise(
                35,
                workoutId = customWorkoutId,
                "Dumbbell concentrated curl",
                muscleGroup = MuscleGroup.BICEPS,
                machineType = MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                36,
                workoutId = customWorkoutId,
                "Dumbbell hammer curl",
                muscleGroup = MuscleGroup.BICEPS,
                machineType = MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                37,
                workoutId = customWorkoutId,
                "Dumbbell hammer curl",
                muscleGroup = MuscleGroup.BICEPS,
                machineType = MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                38,
                workoutId = customWorkoutId,
                "One arm dumbbell preacher curl",
                muscleGroup = MuscleGroup.BICEPS,
                machineType = MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                39,
                workoutId = customWorkoutId,
                "Barbell preacher curl",
                muscleGroup = MuscleGroup.BICEPS,
                machineType = MachineType.BARBELL,
                ""
            ),
            Exercise(
                40,
                workoutId = customWorkoutId,
                "Reverse grip biceps curl at the low pulley cable",
                muscleGroup = MuscleGroup.BICEPS,
                machineType = MachineType.MACHINE,
                ""
            )
        )
    }

    private fun getTricepsExerciseDetails(customWorkoutId: Int): List<ExerciseDetails> {
        return listOf(
            ExerciseDetails(
                22,
                workoutId = customWorkoutId,
                "Triceps cable pushdown",
                description = description,
                muscleGroup = MuscleGroup.TRICEPS,
                machineType = MachineType.MACHINE,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                23,
                workoutId = customWorkoutId,
                "Dumbbell triceps kickback",
                description = description,
                muscleGroup = MuscleGroup.TRICEPS,
                machineType = MachineType.DUMBBELL,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                24,
                workoutId = customWorkoutId,
                "Skull crushers",
                description = description,
                muscleGroup = MuscleGroup.TRICEPS,
                machineType = MachineType.BARBELL,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                25,
                workoutId = customWorkoutId,
                "Dips",
                description = description,
                muscleGroup = MuscleGroup.TRICEPS,
                machineType = MachineType.CALISTHENICS,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                26,
                workoutId = customWorkoutId,
                "Machine triceps dips",
                description = description,
                muscleGroup = MuscleGroup.TRICEPS,
                machineType = MachineType.MACHINE,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                27,
                workoutId = customWorkoutId,
                "Dumbbell triceps extension",
                description = description,
                muscleGroup = MuscleGroup.TRICEPS,
                machineType = MachineType.DUMBBELL,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                28,
                workoutId = customWorkoutId,
                "Cable rope triceps pushdown",
                description = description,
                muscleGroup = MuscleGroup.TRICEPS,
                machineType = MachineType.MACHINE,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                29,
                workoutId = customWorkoutId,
                "Bench dip",
                description = description,
                muscleGroup = MuscleGroup.TRICEPS,
                machineType = MachineType.CALISTHENICS,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                30,
                workoutId = customWorkoutId,
                "Barbell standing french press",
                description = description,
                muscleGroup = MuscleGroup.TRICEPS,
                machineType = MachineType.BARBELL,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                31,
                workoutId = customWorkoutId,
                "Triceps cable rope extension",
                description = description,
                muscleGroup = MuscleGroup.TRICEPS,
                machineType = MachineType.MACHINE,
                videoUrl = videoUrl
            )
        )
    }

    private fun getTricepsExercises(customWorkoutId: Int): List<Exercise> {
        return listOf(
            Exercise(
                22,
                workoutId = customWorkoutId,
                "Triceps cable pushdown",
                muscleGroup = MuscleGroup.TRICEPS,
                machineType = MachineType.MACHINE,
                ""
            ),
            Exercise(
                23,
                workoutId = customWorkoutId,
                "Dumbbell triceps kickback",
                muscleGroup = MuscleGroup.TRICEPS,
                machineType = MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                24,
                workoutId = customWorkoutId,
                "Skull crushers",
                muscleGroup = MuscleGroup.TRICEPS,
                machineType = MachineType.BARBELL,
                ""
            ),
            Exercise(
                25,
                workoutId = customWorkoutId,
                "Dips",
                muscleGroup = MuscleGroup.TRICEPS,
                machineType = MachineType.CALISTHENICS,
                ""
            ),
            Exercise(
                26,
                workoutId = customWorkoutId,
                "Machine triceps dips",
                muscleGroup = MuscleGroup.TRICEPS,
                machineType = MachineType.MACHINE,
                ""
            ),
            Exercise(
                27,
                workoutId = customWorkoutId,
                "Dumbbell triceps extension",
                muscleGroup = MuscleGroup.TRICEPS,
                machineType = MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                28,
                workoutId = customWorkoutId,
                "Cable rope triceps pushdown",
                muscleGroup = MuscleGroup.TRICEPS,
                machineType = MachineType.MACHINE,
                ""
            ),
            Exercise(
                29,
                workoutId = customWorkoutId,
                "Bench dip", //TODO: video...
                muscleGroup = MuscleGroup.TRICEPS,
                machineType = MachineType.CALISTHENICS,
                ""
            ),
            Exercise(
                30,
                workoutId = customWorkoutId,
                "Barbell standing french press", //TODO: video...
                muscleGroup = MuscleGroup.TRICEPS,
                machineType = MachineType.BARBELL,
                ""
            ),
            Exercise(
                31,
                workoutId = customWorkoutId,
                "Triceps cable rope extension",
                muscleGroup = MuscleGroup.TRICEPS,
                machineType = MachineType.MACHINE,
                ""
            )
        )
    }

    private fun getBackExerciseDetails(customWorkoutId: Int): List<ExerciseDetails> {
        return listOf(
            ExerciseDetails(
                11,
                workoutId = customWorkoutId,
                "Seated cable rows",
                description = description,
                muscleGroup = MuscleGroup.BACK,
                machineType = MachineType.MACHINE,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                12,
                workoutId = customWorkoutId,
                "Lat pulldown (Wide grip)",
                description = description,
                muscleGroup = MuscleGroup.BACK,
                machineType = MachineType.MACHINE,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                13,
                workoutId = customWorkoutId,
                "Pull ups",
                description = description,
                muscleGroup = MuscleGroup.BACK,
                machineType = MachineType.CALISTHENICS,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                14,
                workoutId = customWorkoutId,
                "Bent over barbell row",
                description = description,
                muscleGroup = MuscleGroup.BACK,
                machineType = MachineType.BARBELL,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                15,
                workoutId = customWorkoutId,
                "Deadlift",
                description = description,
                muscleGroup = MuscleGroup.BACK,
                machineType = MachineType.BARBELL,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                16,
                workoutId = customWorkoutId,
                "Bent over dumbbell row",
                description = description,
                muscleGroup = MuscleGroup.BACK,
                machineType = MachineType.DUMBBELL,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                17,
                workoutId = customWorkoutId,
                "Standing lat pulldown",
                description = description,
                muscleGroup = MuscleGroup.BACK,
                machineType = MachineType.MACHINE,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                18,
                workoutId = customWorkoutId,
                "T-bar row", //Mechkata
                description = description,
                muscleGroup = MuscleGroup.BACK,
                machineType = MachineType.BARBELL,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                19,
                workoutId = customWorkoutId,
                "Dumbbell Shrugs",
                description = description,
                muscleGroup = MuscleGroup.BACK,
                machineType = MachineType.DUMBBELL,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                20,
                workoutId = customWorkoutId,
                "Behind the neck lat pulldown",
                description = description,
                muscleGroup = MuscleGroup.BACK,
                machineType = MachineType.DUMBBELL,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                21,
                workoutId = customWorkoutId,
                "Romanian deadlift",
                description = description,
                muscleGroup = MuscleGroup.BACK,
                machineType = MachineType.BARBELL,
                videoUrl = videoUrl
            )
        )
    }

    private fun getBackExercises(customWorkoutId: Int): List<Exercise> {
        return listOf(
            Exercise(
                11,
                workoutId = customWorkoutId,
                "Seated cable rows",
                muscleGroup = MuscleGroup.BACK,
                machineType = MachineType.MACHINE,
                ""
            ),
            Exercise(
                12,
                workoutId = customWorkoutId,
                "Lat pulldown (Wide grip)",
                muscleGroup = MuscleGroup.BACK,
                machineType = MachineType.MACHINE,
                ""
            ),
            Exercise(
                13,
                workoutId = customWorkoutId,
                "Pull ups",
                muscleGroup = MuscleGroup.BACK,
                machineType = MachineType.CALISTHENICS,
                ""
            ),
            Exercise(
                14,
                workoutId = customWorkoutId,
                "Bent over barbell row",
                muscleGroup = MuscleGroup.BACK,
                machineType = MachineType.BARBELL,
                ""
            ),
            Exercise(
                15,
                workoutId = customWorkoutId,
                "Deadlift", //TODO: video...
                muscleGroup = MuscleGroup.BACK,
                machineType = MachineType.BARBELL,
                ""
            ),
            Exercise(
                16,
                workoutId = customWorkoutId,
                "Bent over dumbbell row",
                muscleGroup = MuscleGroup.BACK,
                machineType = MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                17,
                workoutId = customWorkoutId,
                "Standing lat pulldown",
                muscleGroup = MuscleGroup.BACK,
                machineType = MachineType.MACHINE,
                ""
            ),
            Exercise(
                18,
                workoutId = customWorkoutId,
                "T-bar row", //Mechkata
                muscleGroup = MuscleGroup.BACK,
                machineType = MachineType.BARBELL,
                ""
            ),
            Exercise(
                19,
                workoutId = customWorkoutId,
                "Dumbbell Shrugs",
                muscleGroup = MuscleGroup.BACK,
                machineType = MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                20,
                workoutId = customWorkoutId,
                "Behind the neck lat pulldown", //TODO: video...
                muscleGroup = MuscleGroup.BACK,
                machineType = MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                21,
                workoutId = customWorkoutId,
                "Romanian deadlift", //TODO: video...
                muscleGroup = MuscleGroup.BACK,
                machineType = MachineType.BARBELL,
                ""
            )
        )
    }

    private fun getChestExerciseDetails(customWorkoutId: Int): List<ExerciseDetails> {
        return listOf(
            ExerciseDetails(
                1,
                workoutId = customWorkoutId,
                "Flat barbell bench press",
                description = description,
                muscleGroup = MuscleGroup.CHEST,
                machineType = MachineType.BARBELL,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                2,
                workoutId = customWorkoutId,
                "Incline barbell bench press",
                description = description,
                muscleGroup = MuscleGroup.CHEST,
                machineType = MachineType.BARBELL,
                videoUrl = videoUrl2
            ),
            ExerciseDetails(
                3,
                workoutId = customWorkoutId,
                "Decline barbell bench press",
                description = description,
                muscleGroup = MuscleGroup.CHEST,
                machineType = MachineType.BARBELL,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                4,
                workoutId = customWorkoutId,
                "Incline dumbbell bench press",
                description = description,
                muscleGroup = MuscleGroup.CHEST,
                machineType = MachineType.DUMBBELL,
                videoUrl = videoUrl2
            ),
            ExerciseDetails(
                5,
                workoutId = customWorkoutId,
                "Flat dumbbell bench press",
                description = description,
                muscleGroup = MuscleGroup.CHEST,
                machineType = MachineType.DUMBBELL,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                6,
                workoutId = customWorkoutId,
                "Pec deck fly",
                description = description,
                muscleGroup = MuscleGroup.CHEST,
                machineType = MachineType.MACHINE,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                7,
                workoutId = customWorkoutId,
                "Cable chest fly",
                description = description,
                muscleGroup = MuscleGroup.CHEST,
                machineType = MachineType.MACHINE,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                8,
                workoutId = customWorkoutId,
                "Hammer strength",
                description = description,
                muscleGroup = MuscleGroup.CHEST,
                machineType = MachineType.MACHINE,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                9,
                workoutId = customWorkoutId,
                "Dips",
                description = description,
                muscleGroup = MuscleGroup.CHEST,
                machineType = MachineType.CALISTHENICS,
                videoUrl = videoUrl
            ),
            ExerciseDetails(
                10,
                workoutId = customWorkoutId,
                "Push ups",
                description = description,
                muscleGroup = MuscleGroup.CHEST,
                machineType = MachineType.CALISTHENICS,
                videoUrl = videoUrl
            )
        )
    }

    private fun getChestExercises(customWorkoutId: Int): List<Exercise> {
        return listOf(
            Exercise(
                1,
                workoutId = customWorkoutId,
                "Flat barbell bench press",
                muscleGroup = MuscleGroup.CHEST,
                machineType = MachineType.BARBELL,
                ""
            ),
            Exercise(
                2,
                workoutId = customWorkoutId,
                "Incline barbell bench press",
                muscleGroup = MuscleGroup.CHEST,
                machineType = MachineType.BARBELL,
                ""
            ),
            Exercise(
                3,
                workoutId = customWorkoutId,
                "Decline barbell bench press",
                muscleGroup = MuscleGroup.CHEST,
                machineType = MachineType.BARBELL,
                ""
            ),
            Exercise(
                4,
                workoutId = customWorkoutId,
                "Incline dumbbell bench press",
                muscleGroup = MuscleGroup.CHEST,
                machineType = MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                5,
                workoutId = customWorkoutId,
                "Flat dumbbell bench press",
                muscleGroup = MuscleGroup.CHEST,
                machineType = MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                6,
                workoutId = customWorkoutId,
                "Pec deck fly",
                muscleGroup = MuscleGroup.CHEST,
                machineType = MachineType.MACHINE,
                ""
            ),
            Exercise(
                7,
                workoutId = customWorkoutId,
                "Cable chest fly",
                muscleGroup = MuscleGroup.CHEST,
                machineType = MachineType.MACHINE,
                ""
            ),
            Exercise(
                8,
                workoutId = customWorkoutId,
                "Hammer strength",
                muscleGroup = MuscleGroup.CHEST,
                machineType = MachineType.MACHINE,
                ""
            ),
            Exercise(
                9,
                workoutId = customWorkoutId,
                "Dips",
                muscleGroup = MuscleGroup.CHEST,
                machineType = MachineType.CALISTHENICS,
                ""
            ),
            Exercise(
                10,
                workoutId = customWorkoutId,
                "Push ups", //TODO: video...
                muscleGroup = MuscleGroup.CHEST,
                machineType = MachineType.CALISTHENICS,
                ""
            )
        )
    }

    fun loadAllExerciseDetailsExerciseCrossRefs(): List<ExerciseDetailsExerciseCrossRef> {
        val crossRefs: MutableList<ExerciseDetailsExerciseCrossRef> = mutableListOf()

        for (i in 1..TOTAL_EXERCISES step 1) {
            crossRefs.add(
                ExerciseDetailsExerciseCrossRef(
                    exerciseId = i,
                    exerciseDetailsId = i,
                    workoutId = Constants.CATALOG_EXERCISE_ID
                )
            )
        }

        return crossRefs
    }

    fun loadExerciseSetsCrossRefs(
        exercise: Exercise,
        exerciseSets: List<ExerciseSet>,
        totalSets: Int = 4
    ): List<ExerciseSetCrossRef> {
        val crossRefs: MutableList<ExerciseSetCrossRef> = mutableListOf()

        repeat(totalSets) {
            crossRefs.add(
                ExerciseSetCrossRef(
                    exerciseId = exercise.exerciseId,
                    workoutId = Constants.CATALOG_EXERCISE_ID,
                    setId = exerciseSets[it].setId
                )
            )
        }

        return crossRefs
    }
}