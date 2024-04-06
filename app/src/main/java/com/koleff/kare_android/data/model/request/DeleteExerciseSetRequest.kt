package com.koleff.kare_android.data.model.request

import com.squareup.moshi.Json
import java.util.UUID

data class DeleteExerciseSetRequest(
    @field:Json(name = "exercise_id")
    val exerciseId: Int,
    @field:Json(name = "exercise_set_id")
    val setId: UUID,
)
