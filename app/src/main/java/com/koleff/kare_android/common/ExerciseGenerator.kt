package com.koleff.kare_android.common

import com.koleff.kare_android.data.model.dto.ExerciseDetailsDto
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.MachineType
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.room.entity.Exercise
import com.koleff.kare_android.data.room.entity.ExerciseDetails
import com.koleff.kare_android.data.room.entity.ExerciseSet
import java.util.UUID

object ExerciseGenerator {

    const val TOTAL_EXERCISES = 60

    fun loadExercises(muscleGroup: MuscleGroup): List<Exercise> {
        return when (muscleGroup) {
            MuscleGroup.CHEST -> getChestExercises()
            MuscleGroup.BACK -> getBackExercises()
            MuscleGroup.TRICEPS -> getTricepsExercises()
            MuscleGroup.BICEPS -> getBicepsExercises()
            MuscleGroup.SHOULDERS -> getShoulderExercises()
            MuscleGroup.LEGS -> getLegsExercises()
            else -> emptyList()
        }
    }

    fun loadExerciseDetails(muscleGroup: MuscleGroup): List<ExerciseDetails> {
        return when (muscleGroup) {
            MuscleGroup.CHEST -> getChestExerciseDetails()
            MuscleGroup.BACK -> getBackExerciseDetails()
            MuscleGroup.TRICEPS -> getTricepsExerciseDetails()
            MuscleGroup.BICEPS -> getBicepsExerciseDetails()
            MuscleGroup.SHOULDERS -> getShoulderExerciseDetails()
            MuscleGroup.LEGS -> getLegsExerciseDetails()
            else -> emptyList()
        }
    }

    fun getAllExercises(): List<ExerciseDto> {
        val exercisesList = mutableListOf<ExerciseDto>()

        for (muscleGroup in MuscleGroup.values()) {
            val generatedExercises =
                loadExercises(muscleGroup)
                    .map(Exercise::toExerciseDto)
                    .toList()

            exercisesList.addAll(generatedExercises)
        }

        return exercisesList
    }

    fun getAllExerciseDetails(): List<ExerciseDetailsDto> {
        val exercisesDetailsList = mutableListOf<ExerciseDetailsDto>()

        for (muscleGroup in MuscleGroup.values()) {
            val generatedExerciseDetails =
                loadExerciseDetails(muscleGroup)
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
            else -> throw NoSuchElementException("Muscle Group doesn't have exercises")
        }
    }

    fun loadExerciseSets(): List<ExerciseSet> {
        return listOf(
            ExerciseSet(UUID.randomUUID(), 1, 12, 25f),
            ExerciseSet(UUID.randomUUID(), 2, 10, 30f),
            ExerciseSet(UUID.randomUUID(), 3, 8, 35f)
        )
    }

    private fun getLegsExerciseDetails(): List<ExerciseDetails> {
        return listOf(
            ExerciseDetails(
                exerciseDetailsId = 51,
                name = "Squat",
                description = "",
                muscleGroup = MuscleGroup.LEGS,
                machineType = MachineType.BARBELL,
                videoUrl = ""
            ),
            ExerciseDetails(
                exerciseDetailsId = 52,
                name = "Bulgarian split squad",
                description = "",
                muscleGroup = MuscleGroup.LEGS,
                machineType = MachineType.DUMBBELL,
                videoUrl = ""
            ),
            ExerciseDetails(
                exerciseDetailsId = 53,
                name = "Smith machine squad",
                description = "",
                muscleGroup = MuscleGroup.LEGS,
                machineType = MachineType.MACHINE,
                videoUrl = ""
            ),
            ExerciseDetails(
                exerciseDetailsId = 54,
                name = "Leg extension",
                description = "",
                muscleGroup = MuscleGroup.LEGS,
                machineType = MachineType.MACHINE,
                videoUrl = ""
            ),
            ExerciseDetails(
                exerciseDetailsId = 55,
                name = "Kettlebell walking lunges",
                description = "",
                muscleGroup = MuscleGroup.LEGS,
                machineType = MachineType.DUMBBELL,
                videoUrl = ""
            ),
            ExerciseDetails(
                exerciseDetailsId = 56,
                name = "Leg press",
                description = "",
                muscleGroup = MuscleGroup.LEGS,
                machineType = MachineType.MACHINE,
                videoUrl = ""
            ),
            ExerciseDetails(
                exerciseDetailsId = 57,
                name = "Prone leg curl",
                description = "",
                muscleGroup = MuscleGroup.LEGS,
                machineType = MachineType.MACHINE,
                videoUrl = ""
            ),
            ExerciseDetails(
                exerciseDetailsId = 58,
                name = "Seated calf raises",
                description = "",
                muscleGroup = MuscleGroup.LEGS,
                machineType = MachineType.MACHINE,
                videoUrl = ""
            ),
            ExerciseDetails(
                exerciseDetailsId = 59,
                name = "Standing calf raises",
                description = "",
                muscleGroup = MuscleGroup.LEGS,
                machineType = MachineType.CALISTHENICS,
                videoUrl = ""
            ),
            ExerciseDetails(
                exerciseDetailsId = 60,
                name = "Barbell standing calf raises",
                description = "",
                muscleGroup = MuscleGroup.LEGS,
                machineType = MachineType.BARBELL,
                videoUrl = ""
            ),
        )
    }

    private fun getLegsExercises(): List<Exercise> { //TODO: video for all...
        return listOf(
            Exercise(
                51,
                "Squat",
                MuscleGroup.LEGS,
                MachineType.BARBELL,
                ""
            ),
            Exercise(
                52,
                "Bulgarian split squad",
                MuscleGroup.LEGS,
                MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                53,
                "Smith machine squad",
                MuscleGroup.LEGS,
                MachineType.MACHINE,
                ""
            ),
            Exercise(
                54,
                "Leg extension",
                MuscleGroup.LEGS,
                MachineType.MACHINE,
                ""
            ),
            Exercise(
                55,
                "Kettlebell walking lunges",
                MuscleGroup.LEGS,
                MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                56,
                "Leg press",
                MuscleGroup.LEGS,
                MachineType.MACHINE,
                ""
            ),
            Exercise(
                57,
                "Prone leg curl",
                MuscleGroup.LEGS,
                MachineType.MACHINE,
                ""
            ),
            Exercise(
                58,
                "Seated calf raises",
                MuscleGroup.LEGS,
                MachineType.MACHINE,
                ""
            ),
            Exercise(
                59,
                "Standing calf raises",
                MuscleGroup.LEGS,
                MachineType.CALISTHENICS,
                ""
            ),
            Exercise(
                60,
                "Barbell standing calf raises",
                MuscleGroup.LEGS,
                MachineType.BARBELL,
                ""
            ),
        )
    }

    private fun getShoulderExerciseDetails(): List<ExerciseDetails> {
        return listOf(
            ExerciseDetails(
                exerciseDetailsId = 41,
                name = "Barbell upright row",
                description = "",
                muscleGroup = MuscleGroup.SHOULDERS,
                machineType = MachineType.BARBELL,
                videoUrl = ""
            ),
            ExerciseDetails(
                exerciseDetailsId = 42,
                name = "Dumbbell front raises",
                description = "",
                muscleGroup = MuscleGroup.SHOULDERS,
                machineType = MachineType.DUMBBELL,
                videoUrl = ""
            ),
            ExerciseDetails(
                exerciseDetailsId = 43,
                name = "Dumbbell lateral raises",
                description = "",
                muscleGroup = MuscleGroup.SHOULDERS,
                machineType = MachineType.DUMBBELL,
                videoUrl = ""
            ),
            ExerciseDetails(
                exerciseDetailsId = 44,
                name = "Seated dumbbell shoulder press",
                description = "",
                muscleGroup = MuscleGroup.SHOULDERS,
                machineType = MachineType.DUMBBELL,
                videoUrl = ""
            ),
            ExerciseDetails(
                exerciseDetailsId = 45,
                name = "Barbell shoulder press",
                description = "",
                muscleGroup = MuscleGroup.SHOULDERS,
                machineType = MachineType.BARBELL,
                videoUrl = ""
            ),
            ExerciseDetails(
                exerciseDetailsId = 46,
                name = "Face pull",
                description = "",
                muscleGroup = MuscleGroup.SHOULDERS,
                machineType = MachineType.MACHINE,
                videoUrl = ""
            ),
            ExerciseDetails(
                exerciseDetailsId = 47,
                name = "Front plate raise",
                description = "",
                muscleGroup = MuscleGroup.SHOULDERS,
                machineType = MachineType.CALISTHENICS,
                videoUrl = ""
            ),
            ExerciseDetails(
                exerciseDetailsId = 48,
                name = "One arm lateral raises at the low pulley cable",
                description = "",
                muscleGroup = MuscleGroup.SHOULDERS,
                machineType = MachineType.MACHINE,
                videoUrl = ""
            ),
            ExerciseDetails(
                exerciseDetailsId = 49,
                name = "Reverse pec deck",
                description = "",
                muscleGroup = MuscleGroup.SHOULDERS,
                machineType = MachineType.MACHINE,
                videoUrl = ""
            ),
            ExerciseDetails(
                exerciseDetailsId = 50,
                name = "Dumbbell behind the back press",
                description = "",
                muscleGroup = MuscleGroup.SHOULDERS,
                machineType = MachineType.DUMBBELL,
                videoUrl = ""
            )
        )
    }

    private fun getShoulderExercises(): List<Exercise> {
        return listOf(
            Exercise(
                41,
                "Barbell upright row",
                MuscleGroup.SHOULDERS,
                MachineType.BARBELL,
                ""
            ),
            Exercise(
                42,
                "Dumbbell front raises",
                MuscleGroup.SHOULDERS,
                MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                43,
                "Dumbbell lateral raises",
                MuscleGroup.SHOULDERS,
                MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                44,
                "Seated dumbbell shoulder press",
                MuscleGroup.SHOULDERS,
                MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                45,
                "Barbell shoulder press",
                MuscleGroup.SHOULDERS,
                MachineType.BARBELL,
                ""
            ),
            Exercise(
                46,
                "Face pull",
                MuscleGroup.SHOULDERS,
                MachineType.MACHINE,
                ""
            ),
            Exercise(
                47,
                "Front plate raise",
                MuscleGroup.SHOULDERS,
                MachineType.CALISTHENICS,
                ""
            ),
            Exercise(
                48,
                "One arm lateral raises at the low pulley cable",
                MuscleGroup.SHOULDERS,
                MachineType.MACHINE,
                ""
            ),
            Exercise(
                49,
                "Reverse pec deck",
                MuscleGroup.SHOULDERS,
                MachineType.MACHINE,
                ""
            ),
            Exercise(
                50,
                "Dumbbell behind the back press", //TODO: video...
                MuscleGroup.SHOULDERS,
                MachineType.DUMBBELL,
                ""
            )
        )
    }

    private fun getBicepsExerciseDetails(): List<ExerciseDetails> {
        return listOf(
            ExerciseDetails(
                32,
                "Standing dumbbell biceps curl",
                description = "",
                MuscleGroup.BICEPS,
                MachineType.DUMBBELL,
                ""
            ),
            ExerciseDetails(
                33,
                "Sitting dumbbell biceps curl",
                description = "",
                MuscleGroup.BICEPS,
                MachineType.DUMBBELL,
                ""
            ),
            ExerciseDetails(
                34,
                "Barbell biceps curl",
                description = "",
                MuscleGroup.BICEPS,
                MachineType.BARBELL,
                ""
            ),
            ExerciseDetails(
                35,
                "Dumbbell concentrated curl",
                description = "",
                MuscleGroup.BICEPS,
                MachineType.DUMBBELL,
                ""
            ),
            ExerciseDetails(
                36,
                "Dumbbell hammer curl",
                description = "",
                MuscleGroup.BICEPS,
                MachineType.DUMBBELL,
                ""
            ),
            ExerciseDetails(
                37,
                "Dumbbell hammer curl",
                description = "",
                MuscleGroup.BICEPS,
                MachineType.DUMBBELL,
                ""
            ),
            ExerciseDetails(
                38,
                "One arm dumbbell preacher curl",
                description = "",
                MuscleGroup.BICEPS,
                MachineType.DUMBBELL,
                ""
            ),
            ExerciseDetails(
                39,
                "Barbell preacher curl",
                description = "",
                MuscleGroup.BICEPS,
                MachineType.BARBELL,
                ""
            ),
            ExerciseDetails(
                40,
                "Reverse grip biceps curl at the low pulley cable",
                description = "",
                MuscleGroup.BICEPS,
                MachineType.MACHINE,
                ""
            )
        )
    }

    private fun getBicepsExercises(): List<Exercise> {
        return listOf(
            Exercise(
                32,
                "Standing dumbbell biceps curl",
                MuscleGroup.BICEPS,
                MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                33,
                "Sitting dumbbell biceps curl",
                MuscleGroup.BICEPS,
                MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                34,
                "Barbell biceps curl",
                MuscleGroup.BICEPS,
                MachineType.BARBELL,
                ""
            ),
            Exercise(
                35,
                "Dumbbell concentrated curl",
                MuscleGroup.BICEPS,
                MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                36,
                "Dumbbell hammer curl",
                MuscleGroup.BICEPS,
                MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                37,
                "Dumbbell hammer curl",
                MuscleGroup.BICEPS,
                MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                38,
                "One arm dumbbell preacher curl",
                MuscleGroup.BICEPS,
                MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                39,
                "Barbell preacher curl",
                MuscleGroup.BICEPS,
                MachineType.BARBELL,
                ""
            ),
            Exercise(
                40,
                "Reverse grip biceps curl at the low pulley cable",
                MuscleGroup.BICEPS,
                MachineType.MACHINE,
                ""
            )
        )
    }

    private fun getTricepsExerciseDetails(): List<ExerciseDetails> {
        return listOf(
            ExerciseDetails(
                22,
                "Triceps cable pushdown",
                description = "",
                MuscleGroup.TRICEPS,
                MachineType.MACHINE,
                ""
            ),
            ExerciseDetails(
                23,
                "Dumbbell triceps kickback",
                description = "",
                MuscleGroup.TRICEPS,
                MachineType.DUMBBELL,
                ""
            ),
            ExerciseDetails(
                24,
                "Skull crushers",
                description = "",
                MuscleGroup.TRICEPS,
                MachineType.BARBELL,
                ""
            ),
            ExerciseDetails(
                25,
                "Dips",
                description = "",
                MuscleGroup.TRICEPS,
                MachineType.CALISTHENICS,
                ""
            ),
            ExerciseDetails(
                26,
                "Machine triceps dips",
                description = "",
                MuscleGroup.TRICEPS,
                MachineType.MACHINE,
                ""
            ),
            ExerciseDetails(
                27,
                "Dumbbell triceps extension",
                description = "",
                MuscleGroup.TRICEPS,
                MachineType.DUMBBELL,
                ""
            ),
            ExerciseDetails(
                28,
                "Cable rope triceps pushdown",
                description = "",
                MuscleGroup.TRICEPS,
                MachineType.MACHINE,
                ""
            ),
            ExerciseDetails(
                29,
                "Bench dip",
                description = "",
                MuscleGroup.TRICEPS,
                MachineType.CALISTHENICS,
                ""
            ),
            ExerciseDetails(
                30,
                "Barbell standing french press",
                description = "",
                MuscleGroup.TRICEPS,
                MachineType.BARBELL,
                ""
            ),
            ExerciseDetails(
                31,
                "Triceps cable rope extension",
                description = "",
                MuscleGroup.TRICEPS,
                MachineType.MACHINE,
                ""
            )
        )
    }

    private fun getTricepsExercises(): List<Exercise> {
        return listOf(
            Exercise(
                22,
                "Triceps cable pushdown",
                MuscleGroup.TRICEPS,
                MachineType.MACHINE,
                ""
            ),
            Exercise(
                23,
                "Dumbbell triceps kickback",
                MuscleGroup.TRICEPS,
                MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                24,
                "Skull crushers",
                MuscleGroup.TRICEPS,
                MachineType.BARBELL,
                ""
            ),
            Exercise(
                25,
                "Dips",
                MuscleGroup.TRICEPS,
                MachineType.CALISTHENICS,
                ""
            ),
            Exercise(
                26,
                "Machine triceps dips",
                MuscleGroup.TRICEPS,
                MachineType.MACHINE,
                ""
            ),
            Exercise(
                27,
                "Dumbbell triceps extension",
                MuscleGroup.TRICEPS,
                MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                28,
                "Cable rope triceps pushdown",
                MuscleGroup.TRICEPS,
                MachineType.MACHINE,
                ""
            ),
            Exercise(
                29,
                "Bench dip", //TODO: video...
                MuscleGroup.TRICEPS,
                MachineType.CALISTHENICS,
                ""
            ),
            Exercise(
                30,
                "Barbell standing french press", //TODO: video...
                MuscleGroup.TRICEPS,
                MachineType.BARBELL,
                ""
            ),
            Exercise(
                31,
                "Triceps cable rope extension",
                MuscleGroup.TRICEPS,
                MachineType.MACHINE,
                ""
            )
        )
    }

    private fun getBackExerciseDetails(): List<ExerciseDetails> {
        return listOf(
            ExerciseDetails(
                11,
                "Seated cable rows",
                description = "",
                MuscleGroup.BACK,
                MachineType.MACHINE,
                ""
            ),
            ExerciseDetails(
                12,
                "Lat pulldown (Wide grip)",
                description = "",
                MuscleGroup.BACK,
                MachineType.MACHINE,
                ""
            ),
            ExerciseDetails(
                13,
                "Pull ups",
                description = "",
                MuscleGroup.BACK,
                MachineType.CALISTHENICS,
                ""
            ),
            ExerciseDetails(
                14,
                "Bent over barbell row",
                description = "",
                MuscleGroup.BACK,
                MachineType.BARBELL,
                ""
            ),
            ExerciseDetails(
                15,
                "Deadlift",
                description = "",
                MuscleGroup.BACK,
                MachineType.BARBELL,
                ""
            ),
            ExerciseDetails(
                16,
                "Bent over dumbbell row",
                description = "",
                MuscleGroup.BACK,
                MachineType.DUMBBELL,
                ""
            ),
            ExerciseDetails(
                17,
                "Standing lat pulldown",
                description = "",
                MuscleGroup.BACK,
                MachineType.MACHINE,
                ""
            ),
            ExerciseDetails(
                18,
                "T-bar row", //Mechkata
                description = "",
                MuscleGroup.BACK,
                MachineType.BARBELL,
                ""
            ),
            ExerciseDetails(
                19,
                "Dumbbell Shrugs",
                description = "",
                MuscleGroup.BACK,
                MachineType.DUMBBELL,
                ""
            ),
            ExerciseDetails(
                20,
                "Behind the neck lat pulldown",
                description = "",
                MuscleGroup.BACK,
                MachineType.DUMBBELL,
                ""
            ),
            ExerciseDetails(
                21,
                "Romanian deadlift",
                description = "",
                MuscleGroup.BACK,
                MachineType.BARBELL,
                ""
            )
        )
    }

    private fun getBackExercises(): List<Exercise> {
        return listOf(
            Exercise(
                11,
                "Seated cable rows",
                MuscleGroup.BACK,
                MachineType.MACHINE,
                ""
            ),
            Exercise(
                12,
                "Lat pulldown (Wide grip)",
                MuscleGroup.BACK,
                MachineType.MACHINE,
                ""
            ),
            Exercise(
                13,
                "Pull ups",
                MuscleGroup.BACK,
                MachineType.CALISTHENICS,
                ""
            ),
            Exercise(
                14,
                "Bent over barbell row",
                MuscleGroup.BACK,
                MachineType.BARBELL,
                ""
            ),
            Exercise(
                15,
                "Deadlift", //TODO: video...
                MuscleGroup.BACK,
                MachineType.BARBELL,
                ""
            ),
            Exercise(
                16,
                "Bent over dumbbell row",
                MuscleGroup.BACK,
                MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                17,
                "Standing lat pulldown",
                MuscleGroup.BACK,
                MachineType.MACHINE,
                ""
            ),
            Exercise(
                18,
                "T-bar row", //Mechkata
                MuscleGroup.BACK,
                MachineType.BARBELL,
                ""
            ),
            Exercise(
                19,
                "Dumbbell Shrugs",
                MuscleGroup.BACK,
                MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                20,
                "Behind the neck lat pulldown", //TODO: video...
                MuscleGroup.BACK,
                MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                21,
                "Romanian deadlift", //TODO: video...
                MuscleGroup.BACK,
                MachineType.BARBELL,
                ""
            )
        )
    }

    private fun getChestExerciseDetails(): List<ExerciseDetails> {
        return listOf(
            ExerciseDetails(
                1,
                "Flat barbell bench press",
                description = "",
                MuscleGroup.CHEST,
                MachineType.BARBELL,
                ""
            ),
            ExerciseDetails(
                2,
                "Incline barbell bench press",
                description = "",
                MuscleGroup.CHEST,
                MachineType.BARBELL,
                ""
            ),
            ExerciseDetails(
                3,
                "Incline barbell bench press",
                description = "",
                MuscleGroup.CHEST,
                MachineType.BARBELL,
                ""
            ),
            ExerciseDetails(
                4,
                "Incline dumbbell bench press",
                description = "",
                MuscleGroup.CHEST,
                MachineType.DUMBBELL,
                ""
            ),
            ExerciseDetails(
                5,
                "Flat dumbbell bench press",
                description = "",
                MuscleGroup.CHEST,
                MachineType.DUMBBELL,
                ""
            ),
            ExerciseDetails(
                6,
                "Pec deck fly",
                description = "",
                MuscleGroup.CHEST,
                MachineType.MACHINE,
                ""
            ),
            ExerciseDetails(
                7,
                "Cable chest fly",
                description = "",
                MuscleGroup.CHEST,
                MachineType.MACHINE,
                ""
            ),
            ExerciseDetails(
                8,
                "Hammer strength",
                description = "",
                MuscleGroup.CHEST,
                MachineType.MACHINE,
                ""
            ),
            ExerciseDetails(
                9,
                "Dips",
                description = "",
                MuscleGroup.CHEST,
                MachineType.CALISTHENICS,
                ""
            ),
            ExerciseDetails(
                10,
                "Push ups",
                description = "",
                MuscleGroup.CHEST,
                MachineType.CALISTHENICS,
                ""
            )
        )
    }

    private fun getChestExercises(): List<Exercise> {
        return listOf(
            Exercise(
                1,
                "Flat barbell bench press",
                MuscleGroup.CHEST,
                MachineType.BARBELL,
                ""
            ),
            Exercise(
                2,
                "Incline barbell bench press",
                MuscleGroup.CHEST,
                MachineType.BARBELL,
                ""
            ),
            Exercise(
                3,
                "Incline barbell bench press",
                MuscleGroup.CHEST,
                MachineType.BARBELL,
                ""
            ),
            Exercise(
                4,
                "Incline dumbbell bench press",
                MuscleGroup.CHEST,
                MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                5,
                "Flat dumbbell bench press",
                MuscleGroup.CHEST,
                MachineType.DUMBBELL,
                ""
            ),
            Exercise(
                6,
                "Pec deck fly",
                MuscleGroup.CHEST,
                MachineType.MACHINE,
                ""
            ),
            Exercise(
                7,
                "Cable chest fly",
                MuscleGroup.CHEST,
                MachineType.MACHINE,
                ""
            ),
            Exercise(
                8,
                "Hammer strength",
                MuscleGroup.CHEST,
                MachineType.MACHINE,
                ""
            ),
            Exercise(
                9,
                "Dips",
                MuscleGroup.CHEST,
                MachineType.CALISTHENICS,
                ""
            ),
            Exercise(
                10,
                "Push ups", //TODO: video...
                MuscleGroup.CHEST,
                MachineType.CALISTHENICS,
                ""
            )
        )
    }
}