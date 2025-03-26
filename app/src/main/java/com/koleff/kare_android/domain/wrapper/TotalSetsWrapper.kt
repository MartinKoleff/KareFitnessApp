package com.koleff.kare_android.domain.wrapper

import com.koleff.kare_android.data.model.response.TotalSetsResponse


class TotalSetsWrapper(totalSetsResponse: TotalSetsResponse):
    ServerResponseData(totalSetsResponse) {
    val totalSets = totalSetsResponse.totalSets
}