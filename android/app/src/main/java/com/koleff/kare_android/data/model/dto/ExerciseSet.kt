package com.koleff.kare_android.data.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExerciseSet(
    val number: Int,
    var reps: Int,
    var weight: Float
) : Parcelable
