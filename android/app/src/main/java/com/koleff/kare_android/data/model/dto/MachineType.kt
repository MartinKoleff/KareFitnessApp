package com.koleff.kare_android.data.model.dto

enum class MachineType(val machineId: Int) {
    DUMBBELL(1),
    BARBELL(2),
    MACHINE(3),
    NONE(-1);

    companion object {
        fun fromId(id: Int): MachineType =
            values().find { it.machineId == id } ?: NONE
    }
}