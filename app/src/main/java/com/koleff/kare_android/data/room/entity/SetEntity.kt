package com.koleff.kare_android.data.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.ExerciseSetDto
import com.koleff.kare_android.data.model.dto.MachineType
import com.koleff.kare_android.data.model.dto.MuscleGroup
import java.util.UUID

@Entity(
    tableName = "exercise_set_table"
)
data class SetEntity(
    @PrimaryKey(autoGenerate = false)
    var setId: UUID,
    val number: Int,
    val reps: Int,
    val weight: Float,
) {
    fun toExerciseSet(): ExerciseSetDto {
        return ExerciseSetDto(
            setId = setId,
            number = number,
            reps = reps,
            weight = weight
        )
    }
}
