package com.koleff.kare_android.data.extension
import com.koleff.kare_android.data.model.dto.WorkoutDto

interface WorkoutDetailsExtended {

    fun toWorkout(): WorkoutDto
}