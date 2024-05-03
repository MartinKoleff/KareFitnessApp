package com.koleff.kare_android.data.room.entity.relations

import androidx.room.Entity
import java.util.UUID

@Deprecated("Not used")
@Entity(
    tableName = "exercise_set_cross_ref",
    primaryKeys = ["exerciseId", "workoutId", "setId"]
)
data class ExerciseSetCrossRef(
    val exerciseId: Int,
    val workoutId: Int,
    val setId: UUID
)