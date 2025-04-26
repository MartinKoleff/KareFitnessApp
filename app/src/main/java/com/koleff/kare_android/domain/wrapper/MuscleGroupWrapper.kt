package com.koleff.kare_android.domain.wrapper

import com.koleff.kare_android.data.model.response.MuscleGroupResponse


class MuscleGroupWrapper(muscleGroupResponse: MuscleGroupResponse):
    ServerResponseData(muscleGroupResponse) {
    val muscleGroup = muscleGroupResponse.muscleGroup
}