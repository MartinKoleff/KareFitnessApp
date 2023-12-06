package com.koleff.kare_android.data.model.dto

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
    OTHER(12),
    NONE(-1);

    companion object {
        fun fromId(id: Int): MuscleGroup =
            values().find { it.muscleGroupId == id } ?: NONE
    }
}