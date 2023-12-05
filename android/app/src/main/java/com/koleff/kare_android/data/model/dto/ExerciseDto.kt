package com.koleff.kare_android.data.model.dto

import com.squareup.moshi.Json

data class ExerciseDto(
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "muscle_group")
    val muscleGroup: String,
){
    fun toExerciseData(): ExerciseData {
        return ExerciseData(this)
    }
}
