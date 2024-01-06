package com.koleff.kare_android.data.model.dto

import android.os.Parcelable
import androidx.compose.ui.Modifier
import com.koleff.kare_android.data.room.entity.Exercise
import com.koleff.kare_android.data.room.entity.SetEntity
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class ExerciseSet(
    var setId: UUID? = null,
    val number: Int,
    var reps: Int,
    var weight: Float
) : Parcelable{
    fun toSetEntity(): SetEntity {
        return SetEntity(
            setId = setId ?: UUID.randomUUID(), // Generate new UUID if null
            number = number,
            reps = reps,
            weight = weight
        )
    }
}
