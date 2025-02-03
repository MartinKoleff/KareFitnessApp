package com.koleff.kare_android.data
import com.koleff.kare_android.data.model.dto.WorkoutDto

interface WorkoutDetailsExtended {

    fun toWorkout(): WorkoutDto
}