package com.koleff.kare_android.domain.wrapper

import com.koleff.kare_android.data.model.response.GetExerciseTotalWeightResponse


class ExerciseTotalWeightWrapper(getExerciseTotalWeightResponse: GetExerciseTotalWeightResponse):
    ServerResponseData(getExerciseTotalWeightResponse) {
    val totalWeight = getExerciseTotalWeightResponse.totalWeight
}