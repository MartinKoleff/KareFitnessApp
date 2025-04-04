package com.koleff.kare_android.domain.wrapper

import com.koleff.kare_android.data.model.response.TotalTimesCompletedResponse


class TotalTimesCompletedWrapper(totalTimesCompletedResponse: TotalTimesCompletedResponse):
    ServerResponseData(totalTimesCompletedResponse) {
    val totalTimesCompleted = totalTimesCompletedResponse.data.totalTimesCompleted
    val datesOfCompletion = totalTimesCompletedResponse.data.datesOfCompletion
}