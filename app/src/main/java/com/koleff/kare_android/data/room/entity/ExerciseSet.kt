package com.koleff.kare_android.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.koleff.kare_android.data.model.dto.ExerciseSetDto
import java.util.UUID

@Entity(
    tableName = "exercise_set_table"
)
data class ExerciseSet(
    @PrimaryKey(autoGenerate = false)
    var setId: UUID,
    val number: Int,
    val reps: Int,
    val weight: Float,
) {
    fun toExerciseSetDto(): ExerciseSetDto {
        return ExerciseSetDto(
            setId = setId,
            number = number,
            reps = reps,
            weight = weight
        )
    }
}
