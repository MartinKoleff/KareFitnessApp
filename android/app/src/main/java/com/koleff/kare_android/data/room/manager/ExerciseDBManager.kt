package com.koleff.kare_android.data.room.manager

import com.koleff.kare_android.common.preferences.Preferences
import com.koleff.kare_android.data.model.dto.MachineType
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.room.dao.ExerciseDao
import com.koleff.kare_android.data.room.entity.Exercise
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExerciseDBManager @Inject constructor(
    private val preferences: Preferences
) {
    val hasInitializedExerciseTableRoomDB = preferences.hasInitializedExerciseTableRoomDB()

    suspend fun initializeExerciseTableRoomDB(exerciseDao: ExerciseDao) = withContext(Dispatchers.IO){
        for (muscleGroup in MuscleGroup.values()) {
            val exercisesList = loadExercises(muscleGroup)
            exerciseDao.insertAll(exercisesList)
        }
        preferences.initializeExerciseTableRoomDB()
    }

    private fun loadExercises(muscleGroup: MuscleGroup): List<Exercise> {
        return when (muscleGroup) {
            MuscleGroup.CHEST -> getChestExercises()
            MuscleGroup.BACK -> getBackExercises()
            MuscleGroup.TRICEPS -> getTricepsExercises()
            MuscleGroup.BICEPS -> getBicepsExercises()
            MuscleGroup.SHOULDERS -> getShoulderExercises()
            MuscleGroup.LEGS -> getLegsExercises()
            MuscleGroup.ARMS, MuscleGroup.ABS, MuscleGroup.CARDIO, MuscleGroup.FULL_BODY, MuscleGroup.PUSH_PULL_LEGS, MuscleGroup.UPPER_LOWER_BODY, MuscleGroup.OTHER, MuscleGroup.NONE -> {
                emptyList()
            }
        }
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

    private fun getChestExercises(): List<Exercise>  {
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