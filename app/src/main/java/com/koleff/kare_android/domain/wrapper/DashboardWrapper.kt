package com.koleff.kare_android.domain.wrapper

import com.koleff.kare_android.data.model.response.DashboardMuscleGroupsResponse

class DashboardWrapper(getDashboardResponse: DashboardMuscleGroupsResponse):
    ServerResponseData(getDashboardResponse) {
    val muscleGroupList = getDashboardResponse.muscleGroupList
}