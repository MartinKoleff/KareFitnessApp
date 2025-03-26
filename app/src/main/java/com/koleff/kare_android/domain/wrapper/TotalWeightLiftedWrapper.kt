package com.koleff.kare_android.domain.wrapper

import com.koleff.kare_android.data.model.response.TotalWeightLiftedResponse


class TotalWeightLiftedWrapper(totalWeightLiftedResponse: TotalWeightLiftedResponse):
    ServerResponseData(totalWeightLiftedResponse) {
    val totalWeight = totalWeightLiftedResponse.totalWeight
}