package com.koleff.kare_android.data.model.response

import com.koleff.kare_android.data.model.dto.ExerciseTime
import com.koleff.kare_android.data.model.response.base_response.BaseResponse
import com.koleff.kare_android.ui.state.DoWorkoutData
import com.koleff.kare_android.ui.state.TimerState
import com.squareup.moshi.Json

data class TimerResponse(
    @Json(name = "time")
    val time: ExerciseTime
): BaseResponse()
