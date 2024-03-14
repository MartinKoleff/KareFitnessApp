package com.koleff.kare_android.data.model.dto

data class ExerciseTime(
    val hours: Int,
    val minutes: Int,
    val seconds: Int
){
    fun toSeconds(): Int{
        return (hours * 60 * 60) + (minutes * 60) + seconds
    }

    fun toTime(): String{
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}
