package com.koleff.kare_android.data.room.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.koleff.kare_android.data.model.dto.MachineType
import com.koleff.kare_android.data.model.dto.MuscleGroup

@Entity
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    val exerciseId: Int,
    val name: String,
    val muscleGroup: MuscleGroup,
    val machineType: MachineType,
    val snapshot: String,
)
