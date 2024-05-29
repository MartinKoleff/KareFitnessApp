package com.koleff.kare_android.data.model.request

import com.koleff.kare_android.ui.state.DoWorkoutData
import com.squareup.moshi.Json

data class UpdateExerciseSetsRequest(
    @field:Json(name = "do_workout_data")
    val doWorkoutData: DoWorkoutData,
)