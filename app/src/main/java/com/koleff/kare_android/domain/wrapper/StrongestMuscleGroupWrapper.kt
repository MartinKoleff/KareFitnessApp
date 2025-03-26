package com.koleff.kare_android.domain.wrapper

import com.koleff.kare_android.data.model.response.StrongestMuscleGroupResponse


class StrongestMuscleGroupWrapper(strongestMuscleGroupResponse: StrongestMuscleGroupResponse):
    ServerResponseData(strongestMuscleGroupResponse) {
    val muscleGroup = strongestMuscleGroupResponse.data.first
    val weightLifted = strongestMuscleGroupResponse.data.second
}