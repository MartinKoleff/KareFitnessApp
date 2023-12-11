package com.koleff.kare_android.data.model.request

import com.squareup.moshi.Json

data class GetExercisesRequest(
    @field:Json(name = "muscle_group_id")
    val muscleGroupId: Int = -1,
)