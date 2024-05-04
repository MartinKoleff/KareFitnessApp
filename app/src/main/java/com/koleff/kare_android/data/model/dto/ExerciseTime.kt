package com.koleff.kare_android.data.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExerciseTime(
    val hours: Int,
    val minutes: Int,
    val seconds: Int
) : Parcelable {
    fun toSeconds(): Int{
        return (hours * 60 * 60) + (minutes * 60) + seconds
    }

    override fun toString(): String{
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    fun hasFinished(): Boolean {
        return this == ExerciseTime(0, 0, 0)
    }

    companion object{
        fun fromSeconds(seconds: Int): ExerciseTime {
            val hours = seconds / 3600
            val minutes = (seconds % 3600) / 60
            val secs = seconds % 60
            return ExerciseTime(hours, minutes, secs)
        }
    }
}
