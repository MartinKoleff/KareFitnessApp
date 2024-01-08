package com.koleff.kare_android.data.room.entity.relations

import androidx.room.Entity
import java.util.UUID

@Entity(
    tableName = "exercise_set_cross_ref",
    primaryKeys = ["exerciseId", "setId"]
)
data class ExerciseSetCrossRef(
    val exerciseId: Int,
    val setId: UUID
)