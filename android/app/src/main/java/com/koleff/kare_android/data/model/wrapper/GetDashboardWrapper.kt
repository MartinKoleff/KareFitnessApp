package com.koleff.kare_android.data.model.wrapper

import com.koleff.kare_android.data.model.response.GetDashboardResponse

class GetDashboardWrapper(getDashboardResponse: GetDashboardResponse):
    ServerResponseData(getDashboardResponse) {
    val muscleGroupList = getDashboardResponse.muscleGroupList
}