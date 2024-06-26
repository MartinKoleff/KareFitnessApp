package com.koleff.kare_android.data.model.dto

import android.os.Parcelable
import com.koleff.kare_android.data.KareEntity
import com.koleff.kare_android.data.room.entity.ExerciseSet
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class ExerciseSetDto(
    var setId: UUID? = null, //TODO: migrate to non nullable
    val workoutId: Int,
    val exerciseId: Int,
    val number: Int,
    var reps: Int,
    var weight: Float
) : Parcelable, KareEntity<ExerciseSet> {
    override fun toEntity(): ExerciseSet {
        return ExerciseSet(
            setId = setId ?: UUID.randomUUID(), // Generate new UUID if null
            number = number,
            workoutId = workoutId,
            exerciseId = exerciseId,
            reps = reps,
            weight = weight
        )
    }
}
