package com.koleff.kare_android.data.model.request

import com.squareup.moshi.Json

data class FetchExerciseRequest(
    @field:Json(name = "exercise_id")
    val exerciseId: Int = -1,
)