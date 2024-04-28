package com.koleff.kare_android.data.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExerciseProgressDto(
    val exerciseId: Int,
    val workoutId: Int,
    val name: String,
    val muscleGroup: MuscleGroup,
    val machineType: MachineType,
    val snapshot: String,
    val sets: List<ExerciseSetProgressDto>
) : Parcelable