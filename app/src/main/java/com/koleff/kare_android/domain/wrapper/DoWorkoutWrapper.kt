package com.koleff.kare_android.domain.wrapper

import com.koleff.kare_android.data.model.response.DoWorkoutResponse

class DoWorkoutWrapper(doWorkoutResponse: DoWorkoutResponse):
    ServerResponseData(doWorkoutResponse) {
    val data = doWorkoutResponse.data
}