package com.koleff.kare_android.data.model.dto

import java.util.Date

data class WorkoutTotalTimesCompleted(
    val totalTimesCompleted: Int = 0,
    val datesOfCompletion: List<Date> = emptyList(),
)