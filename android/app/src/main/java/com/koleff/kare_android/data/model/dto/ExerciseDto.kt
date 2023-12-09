package com.koleff.kare_android.data.model.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity //TODO: separate to Exercise...
data class ExerciseDto(
    @field:Json(name = "id")
    @PrimaryKey(autoGenerate = true)
    val exerciseId: Int,
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "muscle_group")
    val muscleGroup: MuscleGroup,
    @field:Json(name = "machine")
    val machineType: MachineType,
    @field:Json(name = "snapshot") //TODO: image...
    val snapshot: String,
)
