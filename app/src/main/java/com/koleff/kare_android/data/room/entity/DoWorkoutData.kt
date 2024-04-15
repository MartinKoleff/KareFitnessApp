package com.koleff.kare_android.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import com.koleff.kare_android.data.model.dto.ExerciseTime
import java.util.*

@Entity(
    tableName = "do_workout_data",
)
data class DoWorkoutData(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val workoutId: Int,
    val timestamp: Date  //to record the exact time the workout was completed
)