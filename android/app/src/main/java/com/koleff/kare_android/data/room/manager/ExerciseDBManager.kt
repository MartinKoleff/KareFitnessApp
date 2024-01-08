package com.koleff.kare_android.data.room.manager

import com.koleff.kare_android.common.preferences.Preferences
import com.koleff.kare_android.data.model.dto.MachineType
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.room.dao.ExerciseDao
import com.koleff.kare_android.data.room.dao.ExerciseDetailsDao
import com.koleff.kare_android.data.room.dao.ExerciseSetDao
import com.koleff.kare_android.data.room.entity.Exercise
import com.koleff.kare_android.data.room.entity.ExerciseDetails
import com.koleff.kare_android.data.room.entity.SetEntity
import com.koleff.kare_android.data.room.entity.relations.ExerciseDetailsExerciseCrossRef
import com.koleff.kare_android.data.room.entity.relations.ExerciseSetCrossRef
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class ExerciseDBManager @Inject constructor(
    private val preferences: Preferences,
    private val exerciseDao: ExerciseDao,
    private val exerciseDetailsDao: ExerciseDetailsDao,
    private val exerciseSetDao: ExerciseSetDao
) {
    private val hasInitializedExerciseTableRoomDB = preferences.hasInitializedExerciseTableRoomDB()

    suspend fun initializeExerciseTableRoomDB() = withContext(Dispatchers.IO) {
        if (hasInitializedExerciseTableRoomDB) return@withContext

        for (muscleGroup in MuscleGroup.values()) {
            val exercisesList = loadExercises(muscleGroup)
            val exerciseDetailsList = loadExerciseDetails(muscleGroup)
            val exerciseDetailsExerciseCrossRef = loadAllCrossRefs()

            val exerciseSetsCrossRef = loadExerciseSetsCrossRefs(exercisesList)

            exerciseDao.insertAll(exercisesList)
            exerciseDetailsDao.insertAll(exerciseDetailsList)

            exerciseDao.insertAllExerciseDetailsExerciseCrossRefs(exerciseDetailsExerciseCrossRef)
            exerciseDao.insertAllExerciseSetCrossRef(exerciseSetsCrossRef)
        }
        preferences.initializeExerciseTableRoomDB()
    }

    private fun loadExerciseSets(): List<SetEntity> {
        return listOf(
            SetEntity(UUID.randomUUID(),1, 12, 25f),
            SetEntity(UUID.randomUUID(),2, 10, 30f),
            SetEntity(UUID.randomUUID(),3, 8, 35f)
        )
    }

    private suspend fun loadExerciseSetsCrossRefs(allExercises: List<Exercise>): List<ExerciseSetCrossRef> {
        val crossRefs: MutableList<ExerciseSetCrossRef> = mutableListOf()

        val exerciseSets: MutableList<SetEntity> = mutableListOf()
        var counter: Int = 0
        for (exercise in allExercises) {
            exerciseSets.addAll(loadExerciseSets()) //Generate new setId with same ExerciseSet data...

            crossRefs.add(ExerciseSetCrossRef(exercise.exerciseId, exerciseSets[counter + 0].setId))
            crossRefs.add(ExerciseSetCrossRef(exercise.exerciseId, exerciseSets[counter + 1].setId))
            crossRefs.add(ExerciseSetCrossRef(exercise.exerciseId, exerciseSets[counter + 2].setId))

            counter += 3
        }

        exerciseSetDao.insertAllExerciseSets(exerciseSets) //Insert ExerciseSet
        return crossRefs
    }

    private fun loadAllCrossRefs(): List<ExerciseDetailsExerciseCrossRef> {
        return listOf(
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 1, exerciseId = 1),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 2, exerciseId = 2),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 3, exerciseId = 3),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 4, exerciseId = 4),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 5, exerciseId = 5),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 6, exerciseId = 6),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 7, exerciseId = 7),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 8, exerciseId = 8),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 9, exerciseId = 9),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 10, exerciseId = 10),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 11, exerciseId = 11),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 12, exerciseId = 12),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 13, exerciseId = 13),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 14, exerciseId = 14),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 15, exerciseId = 15),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 16, exerciseId = 16),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 17, exerciseId = 17),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 18, exerciseId = 18),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 19, exerciseId = 19),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 20, exerciseId = 20),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 21, exerciseId = 21),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 22, exerciseId = 22),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 23, exerciseId = 23),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 24, exerciseId = 24),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 25, exerciseId = 25),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 26, exerciseId = 26),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 27, exerciseId = 27),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 28, exerciseId = 28),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 29, exerciseId = 29),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 30, exerciseId = 30),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 31, exerciseId = 31),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 32, exerciseId = 32),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 33, exerciseId = 33),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 34, exerciseId = 34),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 35, exerciseId = 35),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 36, exerciseId = 36),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 37, exerciseId = 37),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 38, exerciseId = 38),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 39, exerciseId = 39),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 40, exerciseId = 40),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 40, exerciseId = 40),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 41, exerciseId = 41),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 42, exerciseId = 42),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 43, exerciseId = 43),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 44, exerciseId = 44),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 45, exerciseId = 45),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 46, exerciseId = 46),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 47, exerciseId = 47),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 48, exerciseId = 48),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 49, exerciseId = 49),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 50, exerciseId = 50),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 51, exerciseId = 51),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 52, exerciseId = 52),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 53, exerciseId = 53),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 54, exerciseId = 54),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 55, exerciseId = 55),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 56, exerciseId = 56),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 57, exerciseId = 57),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 58, exerciseId = 58),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 59, exerciseId = 59),
            ExerciseDetailsExerciseCrossRef(exerciseDetailsId = 60, exerciseId = 60)
        )
    }

    private fun loadExerciseDetails(muscleGroup: MuscleGroup): List<ExerciseDetails> {
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

    private fun loadExercises(muscleGroup: MuscleGroup): List<Exercise> {
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