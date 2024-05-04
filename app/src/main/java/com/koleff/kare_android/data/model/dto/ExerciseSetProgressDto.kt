package com.koleff.kare_android.data.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class ExerciseSetProgressDto(
    val baseSet: ExerciseSetDto,
    var isDone: Boolean = false
) : Parcelable