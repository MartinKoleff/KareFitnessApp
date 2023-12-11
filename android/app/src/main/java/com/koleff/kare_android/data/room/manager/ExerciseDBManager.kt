package com.koleff.kare_android.data.room.manager

import com.koleff.kare_android.common.preferences.Preferences
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.MachineType
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.room.dao.ExerciseDao
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

    private fun loadExercises(muscleGroup: MuscleGroup): List<ExerciseDto> {
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

    private fun getLegsExercises(): List<ExerciseDto> { //TODO: video for all...
        return listOf(
            ExerciseDto(
                51,
                "Squat",
                MuscleGroup.LEGS,
                MachineType.BARBELL,
                ""
            ),
            ExerciseDto(
                52,
                "Bulgarian split squad",
                MuscleGroup.LEGS,
                MachineType.DUMBBELL,
                ""
            ),
            ExerciseDto(
                53,
                "Smith machine squad",
                MuscleGroup.LEGS,
                MachineType.MACHINE,
                ""
            ),
            ExerciseDto(
                54,
                "Leg extension",
                MuscleGroup.LEGS,
                MachineType.MACHINE,
                ""
            ),
            ExerciseDto(
                55,
                "Kettlebell walking lunges",
                MuscleGroup.LEGS,
                MachineType.DUMBBELL,
                ""
            ),
            ExerciseDto(
                56,
                "Leg press",
                MuscleGroup.LEGS,
                MachineType.MACHINE,
                ""
            ),
            ExerciseDto(
                57,
                "Prone leg curl",
                MuscleGroup.LEGS,
                MachineType.MACHINE,
                ""
            ),
            ExerciseDto(
                58,
                "Seated calf raises",
                MuscleGroup.LEGS,
                MachineType.MACHINE,
                ""
            ),
            ExerciseDto(
                59,
                "Standing calf raises",
                MuscleGroup.LEGS,
                MachineType.CALISTHENICS,
                ""
            ),
            ExerciseDto(
                60,
                "Barbell standing calf raises",
                MuscleGroup.LEGS,
                MachineType.BARBELL,
                ""
            ),
        )
    }

    private fun getShoulderExercises(): List<ExerciseDto> {
        return listOf(
            ExerciseDto(
                41,
                "Barbell upright row",
                MuscleGroup.SHOULDERS,
                MachineType.BARBELL,
                ""
            ),
            ExerciseDto(
                42,
                "Dumbbell front raises",
                MuscleGroup.SHOULDERS,
                MachineType.DUMBBELL,
                ""
            ),
            ExerciseDto(
                43,
                "Dumbbell lateral raises",
                MuscleGroup.SHOULDERS,
                MachineType.DUMBBELL,
                ""
            ),
            ExerciseDto(
                44,
                "Seated dumbbell shoulder press",
                MuscleGroup.SHOULDERS,
                MachineType.DUMBBELL,
                ""
            ),
            ExerciseDto(
                45,
                "Barbell shoulder press",
                MuscleGroup.SHOULDERS,
                MachineType.BARBELL,
                ""
            ),
            ExerciseDto(
                46,
                "Face pull",
                MuscleGroup.SHOULDERS,
                MachineType.MACHINE,
                ""
            ),
            ExerciseDto(
                47,
                "Front plate raise",
                MuscleGroup.SHOULDERS,
                MachineType.CALISTHENICS,
                ""
            ),
            ExerciseDto(
                48,
                "One arm lateral raises at the low pulley cable",
                MuscleGroup.SHOULDERS,
                MachineType.MACHINE,
                ""
            ),
            ExerciseDto(
                49,
                "Reverse pec deck",
                MuscleGroup.SHOULDERS,
                MachineType.MACHINE,
                ""
            ),
            ExerciseDto(
                50,
                "Dumbbell behind the back press", //TODO: video...
                MuscleGroup.SHOULDERS,
                MachineType.DUMBBELL,
                ""
            )
        )
    }

    private fun getBicepsExercises(): List<ExerciseDto> {
       return listOf(
           ExerciseDto(
               32,
               "Standing dumbbell biceps curl",
               MuscleGroup.BICEPS,
               MachineType.DUMBBELL,
               ""
           ),
           ExerciseDto(
               33,
               "Sitting dumbbell biceps curl",
               MuscleGroup.BICEPS,
               MachineType.DUMBBELL,
               ""
           ),
           ExerciseDto(
               34,
               "Barbell biceps curl",
               MuscleGroup.BICEPS,
               MachineType.BARBELL,
               ""
           ),
           ExerciseDto(
               35,
               "Dumbbell concentrated curl",
               MuscleGroup.BICEPS,
               MachineType.DUMBBELL,
               ""
           ),
           ExerciseDto(
               36,
               "Dumbbell hammer curl",
               MuscleGroup.BICEPS,
               MachineType.DUMBBELL,
               ""
           ),
           ExerciseDto(
               37,
               "Dumbbell hammer curl",
               MuscleGroup.BICEPS,
               MachineType.DUMBBELL,
               ""
           ),
           ExerciseDto(
               38,
               "One arm dumbbell preacher curl",
               MuscleGroup.BICEPS,
               MachineType.DUMBBELL,
               ""
           ),
           ExerciseDto(
               39,
               "Barbell preacher curl",
               MuscleGroup.BICEPS,
               MachineType.BARBELL,
               ""
           ),
           ExerciseDto(
               40,
               "Reverse grip biceps curl at the low pulley cable",
               MuscleGroup.BICEPS,
               MachineType.MACHINE,
               ""
           )
       )
    }

    private fun getTricepsExercises(): List<ExerciseDto> {
        return listOf(
            ExerciseDto(
                22,
                "Triceps cable pushdown",
                MuscleGroup.TRICEPS,
                MachineType.MACHINE,
                ""
            ),
            ExerciseDto(
                23,
                "Dumbbell triceps kickback",
                MuscleGroup.TRICEPS,
                MachineType.DUMBBELL,
                ""
            ),
            ExerciseDto(
                24,
                "Skull crushers",
                MuscleGroup.TRICEPS,
                MachineType.BARBELL,
                ""
            ),
            ExerciseDto(
                25,
                "Dips",
                MuscleGroup.TRICEPS,
                MachineType.CALISTHENICS,
                ""
            ),
            ExerciseDto(
                26,
                "Machine triceps dips",
                MuscleGroup.TRICEPS,
                MachineType.MACHINE,
                ""
            ),
            ExerciseDto(
                27,
                "Dumbbell triceps extension",
                MuscleGroup.TRICEPS,
                MachineType.DUMBBELL,
                ""
            ),
            ExerciseDto(
                28,
                "Cable rope triceps pushdown",
                MuscleGroup.TRICEPS,
                MachineType.MACHINE,
                ""
            ),
            ExerciseDto(
                29,
                "Bench dip", //TODO: video...
                MuscleGroup.TRICEPS,
                MachineType.CALISTHENICS,
                ""
            ),
            ExerciseDto(
                30,
                "Barbell standing french press", //TODO: video...
                MuscleGroup.TRICEPS,
                MachineType.BARBELL,
                ""
            ),
            ExerciseDto(
                31,
                "Triceps cable rope extension",
                MuscleGroup.TRICEPS,
                MachineType.MACHINE,
                ""
            )
        )
    }

    private fun getBackExercises(): List<ExerciseDto> {
        return listOf(
            ExerciseDto(
                11,
                "Seated cable rows",
                MuscleGroup.BACK,
                MachineType.MACHINE,
                ""
            ),
            ExerciseDto(
                12,
                "Lat pulldown (Wide grip)",
                MuscleGroup.BACK,
                MachineType.MACHINE,
                ""
            ),
            ExerciseDto(
                13,
                "Pull ups",
                MuscleGroup.BACK,
                MachineType.CALISTHENICS,
                ""
            ),
            ExerciseDto(
                14,
                "Bent over barbell row",
                MuscleGroup.BACK,
                MachineType.BARBELL,
                ""
            ),
            ExerciseDto(
                15,
                "Deadlift", //TODO: video...
                MuscleGroup.BACK,
                MachineType.BARBELL,
                ""
            ),
            ExerciseDto(
                16,
                "Bent over dumbbell row",
                MuscleGroup.BACK,
                MachineType.DUMBBELL,
                ""
            ),
            ExerciseDto(
                17,
                "Standing lat pulldown",
                MuscleGroup.BACK,
                MachineType.MACHINE,
                ""
            ),
            ExerciseDto(
                18,
                "T-bar row", //Mechkata
                MuscleGroup.BACK,
                MachineType.BARBELL,
                ""
            ),
            ExerciseDto(
                19,
                "Dumbbell Shrugs",
                MuscleGroup.BACK,
                MachineType.DUMBBELL,
                ""
            ),
            ExerciseDto(
                20,
                "Behind the neck lat pulldown", //TODO: video...
                MuscleGroup.BACK,
                MachineType.DUMBBELL,
                ""
            ),
            ExerciseDto(
                21,
                "Romanian deadlift", //TODO: video...
                MuscleGroup.BACK,
                MachineType.BARBELL,
                ""
            )
        )
    }

    private fun getChestExercises(): List<ExerciseDto>  {
        return listOf(
            ExerciseDto(
                1,
                "Flat barbell bench press",
                MuscleGroup.CHEST,
                MachineType.BARBELL,
                ""
            ),
            ExerciseDto(
                2,
                "Incline barbell bench press",
                MuscleGroup.CHEST,
                MachineType.BARBELL,
                ""
            ),
            ExerciseDto(
                3,
                "Incline barbell bench press",
                MuscleGroup.CHEST,
                MachineType.BARBELL,
                ""
            ),
            ExerciseDto(
                4,
                "Incline dumbbell bench press",
                MuscleGroup.CHEST,
                MachineType.DUMBBELL,
                ""
            ),
            ExerciseDto(
                5,
                "Flat dumbbell bench press",
                MuscleGroup.CHEST,
                MachineType.DUMBBELL,
                ""
            ),
            ExerciseDto(
                6,
                "Pec deck fly",
                MuscleGroup.CHEST,
                MachineType.MACHINE,
                ""
            ),
            ExerciseDto(
                7,
                "Cable chest fly",
                MuscleGroup.CHEST,
                MachineType.MACHINE,
                ""
            ),
            ExerciseDto(
                8,
                "Hammer strength",
                MuscleGroup.CHEST,
                MachineType.MACHINE,
                ""
            ),
            ExerciseDto(
                9,
                "Dips",
                MuscleGroup.CHEST,
                MachineType.CALISTHENICS,
                ""
            ),
            ExerciseDto(
                10,
                "Push ups", //TODO: video...
                MuscleGroup.CHEST,
                MachineType.CALISTHENICS,
                ""
            )
        )
    }
}