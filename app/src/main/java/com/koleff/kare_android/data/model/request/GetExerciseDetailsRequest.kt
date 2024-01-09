package com.koleff.kare_android.data.model.request

import com.squareup.moshi.Json

data class GetExerciseDetailsRequest(
    @field:Json(name = "exercise_id")
    val exerciseId: Int = -1,
)