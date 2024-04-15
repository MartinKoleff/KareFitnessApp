package com.koleff.kare_android.data.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.koleff.kare_android.data.model.dto.ExerciseSetDto
import java.util.UUID

//TODO: rename to exerciseSetTemplate -> this is the structure used to describe the workout. After workout starts it can be changed...
@Entity(
    tableName = "exercise_set_table",
    foreignKeys = [
        ForeignKey(
            entity = Exercise::class,
            parentColumns = ["exerciseId", "workoutId"],
            childColumns = ["exerciseId", "workoutId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ExerciseSet(
    @PrimaryKey(autoGenerate = false)
    var setId: UUID,
    val workoutId: Int,
    val exerciseId: Int,
    val number: Int,
    val reps: Int,
    val weight: Float,
) {
    fun toExerciseSetDto(): ExerciseSetDto {
        return ExerciseSetDto(
            setId = setId,
            workoutId = workoutId,
            exerciseId = exerciseId,
            number = number,
            reps = reps,
            weight = weight
        )
    }
}
