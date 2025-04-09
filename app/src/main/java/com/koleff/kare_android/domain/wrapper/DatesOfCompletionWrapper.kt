package com.koleff.kare_android.domain.wrapper

import com.koleff.kare_android.data.model.response.DatesOfCompletionResponse

class DatesOfCompletionWrapper(datesOfCompletionResponse: DatesOfCompletionResponse) :
    ServerResponseData(datesOfCompletionResponse) {
    val dates = datesOfCompletionResponse.dates
}