package com.koleff.kare_android.domain.wrapper

import com.koleff.kare_android.data.model.response.TimerResponse

class TimerWrapper(private val timerResponse: TimerResponse) :
    ServerResponseData(timerResponse) {
    val time = timerResponse.time
}