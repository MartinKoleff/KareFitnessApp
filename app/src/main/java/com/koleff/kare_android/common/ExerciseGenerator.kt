package com.koleff.kare_android.common

import com.koleff.kare_android.data.model.dto.ExerciseDetailsDto
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.MachineType
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.room.entity.Exercise
import com.koleff.kare_android.data.room.entity.ExerciseDetails
import com.koleff.kare_android.data.room.entity.ExerciseSet
import com.koleff.kare_android.data.room.entity.relations.ExerciseDetailsExerciseCrossRef
import com.koleff.kare_android.data.room.entity.relations.ExerciseSetCrossRef
import java.util.UUID
import kotlin.random.Random

object ExerciseGenerator {

    const val TOTAL_EXERCISES = 60

    //Used for testing
    private const val description =
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc interdum nibh nec pharetra iaculis. Aenean ultricies egestas leo at ultricies. Quisque suscipit, purus ut congue porta, eros eros tincidunt sem, sed commodo magna metus eu nibh. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Vestibulum quis velit eget eros malesuada luctus. Suspendisse iaculis ullamcorper condimentum. Sed metus augue, dapibus eu venenatis vitae, ornare non turpis. Donec suscipit iaculis dolor, id fermentum mauris interdum in. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas."
    private const val videoUrl = "dQw4w9WgXcQ" //https://www.youtube.com/watch?v=


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

    fun loadExerciseDetails(muscleGroup: MuscleGroup, isWorkout: Boolean, workoutId: Int = 1): List<ExerciseDetails> {
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
            val exerciseSets = loadExerciseSets()
            val generatedExercises =
                loadExercises(muscleGroup, isWorkout)
                    .map { exercise ->
                        exercise.toExerciseDto(exerciseSets)
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
                    .map(ExerciseDetails::toExerciseDetailsDto)
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

    fun loadExerciseSets(): List<ExerciseSet> {
        return listOf(
            ExerciseSet(UUID.randomUUID(), 1, 12, 0.0f),
            ExerciseSet(UUID.randomUUID(), 2, 10, 0.0f),
            ExerciseSet(UUID.randomUUID(), 3, 8, 0.0f),
            ExerciseSet(UUID.randomUUID(), 4, 1, 50f),
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
                MuscleGroup.LEGS,
                MachineType.BARBELL,
                ""
            ),
            Exercise(
                52,
                workoutId = customWorkoutId,
                "Bulgarian split squad",
                MuscleGroup.LEGS,
                MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                53,
                workoutId = customWorkoutId,
                "Smith machine squad",
                MuscleGroup.LEGS,
                MachineType.MACHINE,
                ""
            ),
            Exercise(
                54,
                workoutId = customWorkoutId,
                "Leg extension",
                MuscleGroup.LEGS,
                MachineType.MACHINE,
                ""
            ),
            Exercise(
                55,
                workoutId = customWorkoutId,
                "Kettlebell walking lunges",
                MuscleGroup.LEGS,
                MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                56,
                workoutId = customWorkoutId,
                "Leg press",
                MuscleGroup.LEGS,
                MachineType.MACHINE,
                ""
            ),
            Exercise(
                57,
                workoutId = customWorkoutId,
                "Prone leg curl",
                MuscleGroup.LEGS,
                MachineType.MACHINE,
                ""
            ),
            Exercise(
                58,
                workoutId = customWorkoutId,
                "Seated calf raises",
                MuscleGroup.LEGS,
                MachineType.MACHINE,
                ""
            ),
            Exercise(
                59,
                workoutId = customWorkoutId,
                "Standing calf raises",
                MuscleGroup.LEGS,
                MachineType.CALISTHENICS,
                ""
            ),
            Exercise(
                60,
                workoutId = customWorkoutId,
                "Barbell standing calf raises",
                MuscleGroup.LEGS,
                MachineType.BARBELL,
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
                MuscleGroup.SHOULDERS,
                MachineType.BARBELL,
                ""
            ),
            Exercise(
                42,
                workoutId = customWorkoutId,
                "Dumbbell front raises",
                MuscleGroup.SHOULDERS,
                MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                43,
                workoutId = customWorkoutId,
                "Dumbbell lateral raises",
                MuscleGroup.SHOULDERS,
                MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                44,
                workoutId = customWorkoutId,
                "Seated dumbbell shoulder press",
                MuscleGroup.SHOULDERS,
                MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                45,
                workoutId = customWorkoutId,
                "Barbell shoulder press",
                MuscleGroup.SHOULDERS,
                MachineType.BARBELL,
                ""
            ),
            Exercise(
                46,
                workoutId = customWorkoutId,
                "Face pull",
                MuscleGroup.SHOULDERS,
                MachineType.MACHINE,
                ""
            ),
            Exercise(
                47,
                workoutId = customWorkoutId,
                "Front plate raise",
                MuscleGroup.SHOULDERS,
                MachineType.CALISTHENICS,
                ""
            ),
            Exercise(
                48,
                workoutId = customWorkoutId,
                "One arm lateral raises at the low pulley cable",
                MuscleGroup.SHOULDERS,
                MachineType.MACHINE,
                ""
            ),
            Exercise(
                49,
                workoutId = customWorkoutId,
                "Reverse pec deck",
                MuscleGroup.SHOULDERS,
                MachineType.MACHINE,
                ""
            ),
            Exercise(
                50,
                workoutId = customWorkoutId,
                "Dumbbell behind the back press", //TODO: video...
                MuscleGroup.SHOULDERS,
                MachineType.DUMBBELL,
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
                MuscleGroup.BICEPS,
                MachineType.DUMBBELL,
                ""
            ),
            ExerciseDetails(
                33,
                workoutId = customWorkoutId,
                "Sitting dumbbell biceps curl",
                description = description,
                MuscleGroup.BICEPS,
                MachineType.DUMBBELL,
                ""
            ),
            ExerciseDetails(
                34,
                workoutId = customWorkoutId,
                "Barbell biceps curl",
                description = description,
                MuscleGroup.BICEPS,
                MachineType.BARBELL,
                ""
            ),
            ExerciseDetails(
                35,
                workoutId = customWorkoutId,
                "Dumbbell concentrated curl",
                description = description,
                MuscleGroup.BICEPS,
                MachineType.DUMBBELL,
                ""
            ),
            ExerciseDetails(
                36,
                workoutId = customWorkoutId,
                "Dumbbell hammer curl",
                description = description,
                MuscleGroup.BICEPS,
                MachineType.DUMBBELL,
                ""
            ),
            ExerciseDetails(
                37,
                workoutId = customWorkoutId,
                "Dumbbell hammer curl",
                description = description,
                MuscleGroup.BICEPS,
                MachineType.DUMBBELL,
                ""
            ),
            ExerciseDetails(
                38,
                workoutId = customWorkoutId,
                "One arm dumbbell preacher curl",
                description = description,
                MuscleGroup.BICEPS,
                MachineType.DUMBBELL,
                ""
            ),
            ExerciseDetails(
                39,
                workoutId = customWorkoutId,
                "Barbell preacher curl",
                description = description,
                MuscleGroup.BICEPS,
                MachineType.BARBELL,
                ""
            ),
            ExerciseDetails(
                40,
                workoutId = customWorkoutId,
                "Reverse grip biceps curl at the low pulley cable",
                description = description,
                MuscleGroup.BICEPS,
                MachineType.MACHINE,
                ""
            )
        )
    }

    private fun getBicepsExercises(customWorkoutId: Int): List<Exercise> {
        return listOf(
            Exercise(
                32,
                workoutId = customWorkoutId,
                "Standing dumbbell biceps curl",
                MuscleGroup.BICEPS,
                MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                33,
                workoutId = customWorkoutId,
                "Sitting dumbbell biceps curl",
                MuscleGroup.BICEPS,
                MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                34,
                workoutId = customWorkoutId,
                "Barbell biceps curl",
                MuscleGroup.BICEPS,
                MachineType.BARBELL,
                ""
            ),
            Exercise(
                35,
                workoutId = customWorkoutId,
                "Dumbbell concentrated curl",
                MuscleGroup.BICEPS,
                MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                36,
                workoutId = customWorkoutId,
                "Dumbbell hammer curl",
                MuscleGroup.BICEPS,
                MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                37,
                workoutId = customWorkoutId,
                "Dumbbell hammer curl",
                MuscleGroup.BICEPS,
                MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                38,
                workoutId = customWorkoutId,
                "One arm dumbbell preacher curl",
                MuscleGroup.BICEPS,
                MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                39,
                workoutId = customWorkoutId,
                "Barbell preacher curl",
                MuscleGroup.BICEPS,
                MachineType.BARBELL,
                ""
            ),
            Exercise(
                40,
                workoutId = customWorkoutId,
                "Reverse grip biceps curl at the low pulley cable",
                MuscleGroup.BICEPS,
                MachineType.MACHINE,
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
                MuscleGroup.TRICEPS,
                MachineType.MACHINE,
                ""
            ),
            ExerciseDetails(
                23,
                workoutId = customWorkoutId,
                "Dumbbell triceps kickback",
                description = description,
                MuscleGroup.TRICEPS,
                MachineType.DUMBBELL,
                ""
            ),
            ExerciseDetails(
                24,
                workoutId = customWorkoutId,
                "Skull crushers",
                description = description,
                MuscleGroup.TRICEPS,
                MachineType.BARBELL,
                ""
            ),
            ExerciseDetails(
                25,
                workoutId = customWorkoutId,
                "Dips",
                description = description,
                MuscleGroup.TRICEPS,
                MachineType.CALISTHENICS,
                ""
            ),
            ExerciseDetails(
                26,
                workoutId = customWorkoutId,
                "Machine triceps dips",
                description = description,
                MuscleGroup.TRICEPS,
                MachineType.MACHINE,
                ""
            ),
            ExerciseDetails(
                27,
                workoutId = customWorkoutId,
                "Dumbbell triceps extension",
                description = description,
                MuscleGroup.TRICEPS,
                MachineType.DUMBBELL,
                ""
            ),
            ExerciseDetails(
                28,
                workoutId = customWorkoutId,
                "Cable rope triceps pushdown",
                description = description,
                MuscleGroup.TRICEPS,
                MachineType.MACHINE,
                ""
            ),
            ExerciseDetails(
                29,
                workoutId = customWorkoutId,
                "Bench dip",
                description = description,
                MuscleGroup.TRICEPS,
                MachineType.CALISTHENICS,
                ""
            ),
            ExerciseDetails(
                30,
                workoutId = customWorkoutId,
                "Barbell standing french press",
                description = description,
                MuscleGroup.TRICEPS,
                MachineType.BARBELL,
                ""
            ),
            ExerciseDetails(
                31,
                workoutId = customWorkoutId,
                "Triceps cable rope extension",
                description = description,
                MuscleGroup.TRICEPS,
                MachineType.MACHINE,
                ""
            )
        )
    }

    private fun getTricepsExercises(customWorkoutId: Int): List<Exercise> {
        return listOf(
            Exercise(
                22,
                workoutId = customWorkoutId,
                "Triceps cable pushdown",
                MuscleGroup.TRICEPS,
                MachineType.MACHINE,
                ""
            ),
            Exercise(
                23,
                workoutId = customWorkoutId,
                "Dumbbell triceps kickback",
                MuscleGroup.TRICEPS,
                MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                24,
                workoutId = customWorkoutId,
                "Skull crushers",
                MuscleGroup.TRICEPS,
                MachineType.BARBELL,
                ""
            ),
            Exercise(
                25,
                workoutId = customWorkoutId,
                "Dips",
                MuscleGroup.TRICEPS,
                MachineType.CALISTHENICS,
                ""
            ),
            Exercise(
                26,
                workoutId = customWorkoutId,
                "Machine triceps dips",
                MuscleGroup.TRICEPS,
                MachineType.MACHINE,
                ""
            ),
            Exercise(
                27,
                workoutId = customWorkoutId,
                "Dumbbell triceps extension",
                MuscleGroup.TRICEPS,
                MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                28,
                workoutId = customWorkoutId,
                "Cable rope triceps pushdown",
                MuscleGroup.TRICEPS,
                MachineType.MACHINE,
                ""
            ),
            Exercise(
                29,
                workoutId = customWorkoutId,
                "Bench dip", //TODO: video...
                MuscleGroup.TRICEPS,
                MachineType.CALISTHENICS,
                ""
            ),
            Exercise(
                30,
                workoutId = customWorkoutId,
                "Barbell standing french press", //TODO: video...
                MuscleGroup.TRICEPS,
                MachineType.BARBELL,
                ""
            ),
            Exercise(
                31,
                workoutId = customWorkoutId,
                "Triceps cable rope extension",
                MuscleGroup.TRICEPS,
                MachineType.MACHINE,
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
                MuscleGroup.BACK,
                MachineType.MACHINE,
                ""
            ),
            ExerciseDetails(
                12,
                workoutId = customWorkoutId,
                "Lat pulldown (Wide grip)",
                description = description,
                MuscleGroup.BACK,
                MachineType.MACHINE,
                ""
            ),
            ExerciseDetails(
                13,
                workoutId = customWorkoutId,
                "Pull ups",
                description = description,
                MuscleGroup.BACK,
                MachineType.CALISTHENICS,
                ""
            ),
            ExerciseDetails(
                14,
                workoutId = customWorkoutId,
                "Bent over barbell row",
                description = description,
                MuscleGroup.BACK,
                MachineType.BARBELL,
                ""
            ),
            ExerciseDetails(
                15,
                workoutId = customWorkoutId,
                "Deadlift",
                description = description,
                MuscleGroup.BACK,
                MachineType.BARBELL,
                ""
            ),
            ExerciseDetails(
                16,
                workoutId = customWorkoutId,
                "Bent over dumbbell row",
                description = description,
                MuscleGroup.BACK,
                MachineType.DUMBBELL,
                ""
            ),
            ExerciseDetails(
                17,
                workoutId = customWorkoutId,
                "Standing lat pulldown",
                description = description,
                MuscleGroup.BACK,
                MachineType.MACHINE,
                ""
            ),
            ExerciseDetails(
                18,
                workoutId = customWorkoutId,
                "T-bar row", //Mechkata
                description = description,
                MuscleGroup.BACK,
                MachineType.BARBELL,
                ""
            ),
            ExerciseDetails(
                19,
                workoutId = customWorkoutId,
                "Dumbbell Shrugs",
                description = description,
                MuscleGroup.BACK,
                MachineType.DUMBBELL,
                ""
            ),
            ExerciseDetails(
                20,
                workoutId = customWorkoutId,
                "Behind the neck lat pulldown",
                description = description,
                MuscleGroup.BACK,
                MachineType.DUMBBELL,
                ""
            ),
            ExerciseDetails(
                21,
                workoutId = customWorkoutId,
                "Romanian deadlift",
                description = description,
                MuscleGroup.BACK,
                MachineType.BARBELL,
                ""
            )
        )
    }

    private fun getBackExercises(customWorkoutId: Int): List<Exercise> {
        return listOf(
            Exercise(
                11,
                workoutId = customWorkoutId,
                "Seated cable rows",
                MuscleGroup.BACK,
                MachineType.MACHINE,
                ""
            ),
            Exercise(
                12,
                workoutId = customWorkoutId,
                "Lat pulldown (Wide grip)",
                MuscleGroup.BACK,
                MachineType.MACHINE,
                ""
            ),
            Exercise(
                13,
                workoutId = customWorkoutId,
                "Pull ups",
                MuscleGroup.BACK,
                MachineType.CALISTHENICS,
                ""
            ),
            Exercise(
                14,
                workoutId = customWorkoutId,
                "Bent over barbell row",
                MuscleGroup.BACK,
                MachineType.BARBELL,
                ""
            ),
            Exercise(
                15,
                workoutId = customWorkoutId,
                "Deadlift", //TODO: video...
                MuscleGroup.BACK,
                MachineType.BARBELL,
                ""
            ),
            Exercise(
                16,
                workoutId = customWorkoutId,
                "Bent over dumbbell row",
                MuscleGroup.BACK,
                MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                17,
                workoutId = customWorkoutId,
                "Standing lat pulldown",
                MuscleGroup.BACK,
                MachineType.MACHINE,
                ""
            ),
            Exercise(
                18,
                workoutId = customWorkoutId,
                "T-bar row", //Mechkata
                MuscleGroup.BACK,
                MachineType.BARBELL,
                ""
            ),
            Exercise(
                19,
                workoutId = customWorkoutId,
                "Dumbbell Shrugs",
                MuscleGroup.BACK,
                MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                20,
                workoutId = customWorkoutId,
                "Behind the neck lat pulldown", //TODO: video...
                MuscleGroup.BACK,
                MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                21,
                workoutId = customWorkoutId,
                "Romanian deadlift", //TODO: video...
                MuscleGroup.BACK,
                MachineType.BARBELL,
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
                MuscleGroup.CHEST,
                MachineType.BARBELL,
                ""
            ),
            ExerciseDetails(
                2,
                workoutId = customWorkoutId,
                "Incline barbell bench press",
                description = description,
                MuscleGroup.CHEST,
                MachineType.BARBELL,
                ""
            ),
            ExerciseDetails(
                3,
                workoutId = customWorkoutId,
                "Incline barbell bench press",
                description = description,
                MuscleGroup.CHEST,
                MachineType.BARBELL,
                ""
            ),
            ExerciseDetails(
                4,
                workoutId = customWorkoutId,
                "Incline dumbbell bench press",
                description = description,
                MuscleGroup.CHEST,
                MachineType.DUMBBELL,
                ""
            ),
            ExerciseDetails(
                5,
                workoutId = customWorkoutId,
                "Flat dumbbell bench press",
                description = description,
                MuscleGroup.CHEST,
                MachineType.DUMBBELL,
                ""
            ),
            ExerciseDetails(
                6,
                workoutId = customWorkoutId,
                "Pec deck fly",
                description = description,
                MuscleGroup.CHEST,
                MachineType.MACHINE,
                ""
            ),
            ExerciseDetails(
                7,
                workoutId = customWorkoutId,
                "Cable chest fly",
                description = description,
                MuscleGroup.CHEST,
                MachineType.MACHINE,
                ""
            ),
            ExerciseDetails(
                8,
                workoutId = customWorkoutId,
                "Hammer strength",
                description = description,
                MuscleGroup.CHEST,
                MachineType.MACHINE,
                ""
            ),
            ExerciseDetails(
                9,
                workoutId = customWorkoutId,
                "Dips",
                description = description,
                MuscleGroup.CHEST,
                MachineType.CALISTHENICS,
                ""
            ),
            ExerciseDetails(
                10,
                workoutId = customWorkoutId,
                "Push ups",
                description = description,
                MuscleGroup.CHEST,
                MachineType.CALISTHENICS,
                ""
            )
        )
    }

    private fun getChestExercises(customWorkoutId: Int): List<Exercise> {
        return listOf(
            Exercise(
                1,
                workoutId = customWorkoutId,
                "Flat barbell bench press",
                MuscleGroup.CHEST,
                MachineType.BARBELL,
                ""
            ),
            Exercise(
                2,
                workoutId = customWorkoutId,
                "Incline barbell bench press",
                MuscleGroup.CHEST,
                MachineType.BARBELL,
                ""
            ),
            Exercise(
                3,
                workoutId = customWorkoutId,
                "Incline barbell bench press",
                MuscleGroup.CHEST,
                MachineType.BARBELL,
                ""
            ),
            Exercise(
                4,
                workoutId = customWorkoutId,
                "Incline dumbbell bench press",
                MuscleGroup.CHEST,
                MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                5,
                workoutId = customWorkoutId,
                "Flat dumbbell bench press",
                MuscleGroup.CHEST,
                MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                6,
                workoutId = customWorkoutId,
                "Pec deck fly",
                MuscleGroup.CHEST,
                MachineType.MACHINE,
                ""
            ),
            Exercise(
                7,
                workoutId = customWorkoutId,
                "Cable chest fly",
                MuscleGroup.CHEST,
                MachineType.MACHINE,
                ""
            ),
            Exercise(
                8,
                workoutId = customWorkoutId,
                "Hammer strength",
                MuscleGroup.CHEST,
                MachineType.MACHINE,
                ""
            ),
            Exercise(
                9,
                workoutId = customWorkoutId,
                "Dips",
                MuscleGroup.CHEST,
                MachineType.CALISTHENICS,
                ""
            ),
            Exercise(
                10,
                workoutId = customWorkoutId,
                "Push ups", //TODO: video...
                MuscleGroup.CHEST,
                MachineType.CALISTHENICS,
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