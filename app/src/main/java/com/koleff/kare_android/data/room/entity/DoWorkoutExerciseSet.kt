package com.koleff.kare_android.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import com.koleff.kare_android.data.model.dto.ExerciseTime
import java.util.*

@Entity(
    tableName = "do_workout_exercise_set",
    foreignKeys = [
        ForeignKey(
            entity = DoWorkoutData::class,
            parentColumns = ["id"],
            childColumns = ["workoutDataId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ExerciseSet::class,
            parentColumns = ["setId"],
            childColumns = ["templateSetId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DoWorkoutExerciseSet(
    @PrimaryKey(autoGenerate = true)
    val instanceId: UUID = UUID.randomUUID(),
    val workoutDataId: Int,  //Link to DoWorkoutData
    val workoutId: Int,
    val exerciseId: Int,
    val templateSetId: UUID,  //Link to the ExerciseSet -> template
    val reps: Int,
    val weigh: Float?,
    val time: ExerciseTime?,
    val timestamp: Date  //to record the exact time the workout was completed
)