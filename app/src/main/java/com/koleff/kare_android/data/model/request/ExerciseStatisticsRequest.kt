package com.koleff.kare_android.data.model.request

import com.koleff.kare_android.data.model.dto.ExerciseSetDto
import com.koleff.kare_android.data.model.dto.KareLanguage
import com.squareup.moshi.Json

data class ExerciseStatisticsRequest(
    @field:Json(name = "exercise_id")
    val exerciseId: Int
)