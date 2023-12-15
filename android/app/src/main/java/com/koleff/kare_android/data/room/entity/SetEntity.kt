package com.koleff.kare_android.data.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.ExerciseSet
import com.koleff.kare_android.data.model.dto.MachineType
import com.koleff.kare_android.data.model.dto.MuscleGroup

@Entity(
    tableName = "exercise_set_table"
)
data class SetEntity(
    @PrimaryKey(autoGenerate = true)
    val setId: Int,
    val reps: Int,
    val weight: Float,
) {
    fun toExerciseSet(): ExerciseSet {
        return ExerciseSet(
            number = setId,
            reps = reps,
            weight = weight
        )
    }
}
