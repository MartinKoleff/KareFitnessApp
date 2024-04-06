package com.koleff.kare_android.data.model.dto

import com.koleff.kare_android.R
import com.koleff.kare_android.common.ExerciseGenerator

enum class MuscleGroup(
    val muscleGroupId: Int,
    val muscleGroupName: String,
    val description: String,
    val imageId: Int
) {
    CHEST(1, "Chest", "", R.drawable.ic_chest),
    BACK(2, "Back", "", R.drawable.ic_back),
    TRICEPS(3, "Triceps", "", R.drawable.background_triceps),
    BICEPS(4, "Biceps", "", R.drawable.ic_biceps),
    SHOULDERS(5, "Shoulders", "", R.drawable.background_shoulders),
    LEGS(6, "Legs", "", R.drawable.background_legs),
    ABS(7, "Abs", "", -1),
    CARDIO(8, "Cardio", "", -1),
    FULL_BODY(9, "Full Body", "", -1),
    PUSH_PULL_LEGS(10, "Push Pull Legs", "", -1),
    UPPER_LOWER_BODY(11, "Upper Lower Body", "", -1),
    ARMS(12, "Arms", "", -1),
    OTHER(13, "Other", "", -1),
    ALL(14, "All", "", -1),
    NONE(-1, "None", "", -1);

    companion object {
        fun getSupportedMuscleGroups(): List<MuscleGroup> = listOf(
            CHEST,
            BACK,
            TRICEPS,
            BICEPS,
            SHOULDERS,
            LEGS
        )


        fun fromId(id: Int): MuscleGroup =
            entries.find { it.muscleGroupId == id } ?: NONE

        fun getImage(muscleGroup: MuscleGroup): Int {
            return if (muscleGroup.imageId != -1) muscleGroup.imageId else R.drawable.ic_default
        }

        fun getTotalExercises(muscleGroup: MuscleGroup): Int {
            return ExerciseGenerator.getTotalExercisesForMuscleGroup(muscleGroup)
        }

        fun toDescription(muscleGroup: MuscleGroup): String {
            return when (muscleGroup) {
                CHEST, BACK, TRICEPS, BICEPS, SHOULDERS, LEGS, ABS, CARDIO, FULL_BODY  -> {
                    muscleGroup.muscleGroupName
                }
                UPPER_LOWER_BODY -> {
                    "Upper Body ○ Lower Body"
                }
                ARMS -> {
                    "Triceps ○ Biceps ○ Forearms"
                }
                PUSH_PULL_LEGS -> {
                    muscleGroup.muscleGroupName.replace(" ", " ○ ")
                }
                else -> { //OTHER, ALL, NONE
                    ""
                }
            }
        }
    }
}

