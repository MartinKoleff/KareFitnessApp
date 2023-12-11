package com.koleff.kare_android.data.room.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.squareup.moshi.Json

@Entity
data class Workout(
    @PrimaryKey(autoGenerate = true)
    val workoutId: Int,
    val name: String,
    val muscleGroup: MuscleGroup,
    val snapshot: String,
    val totalExercises: Int,
    val isSelected: Boolean,
)