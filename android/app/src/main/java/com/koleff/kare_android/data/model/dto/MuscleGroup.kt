package com.koleff.kare_android.data.model.dto

import com.koleff.kare_android.R

enum class MuscleGroup(val muscleGroupId: Int) {
    CHEST(1),
    BACK(2),
    TRICEPS(3),
    BICEPS(4),
    SHOULDERS(5),
    LEGS(6),
    ABS(7),
    CARDIO(8),
    FULL_BODY(9),
    PUSH_PULL_LEGS(10),
    UPPER_LOWER_BODY(11),
    ARMS(12),
    OTHER(13),
    ALL(14),
    NONE(-1);

    companion object {
        fun fromId(id: Int): MuscleGroup =
            values().find { it.muscleGroupId == id } ?: NONE

        fun getImage(muscleGroup: MuscleGroup): Int{
            return when (muscleGroup) {
                MuscleGroup.CHEST -> R.drawable.ic_chest
                MuscleGroup.BACK -> R.drawable.ic_back
                MuscleGroup.TRICEPS -> R.drawable.ic_triceps
                MuscleGroup.BICEPS, ARMS -> R.drawable.ic_biceps
                MuscleGroup.SHOULDERS -> R.drawable.ic_shoulder
                MuscleGroup.LEGS -> R.drawable.ic_legs
                else -> R.drawable.ic_default
            }
        }
    }
}

