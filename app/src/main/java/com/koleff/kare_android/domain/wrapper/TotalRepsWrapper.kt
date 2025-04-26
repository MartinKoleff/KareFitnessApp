package com.koleff.kare_android.domain.wrapper

import com.koleff.kare_android.data.model.response.TotalRepsResponse


class TotalRepsWrapper(totalRepsResponse: TotalRepsResponse):
    ServerResponseData(totalRepsResponse) {
    val totalReps = totalRepsResponse.totalReps
}