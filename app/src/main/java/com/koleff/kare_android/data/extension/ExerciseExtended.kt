package com.koleff.kare_android.data.extension
import com.koleff.kare_android.data.model.dto.ExerciseDetailsDto

interface ExerciseExtended {

    fun toExerciseDetails(): ExerciseDetailsDto
}