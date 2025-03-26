package com.koleff.kare_android.domain.wrapper

import com.koleff.kare_android.data.model.response.GetExerciseTotalSetsResponse


class ExerciseTotalSetsWrapper(getExerciseTotalSetsResponse: GetExerciseTotalSetsResponse):
    ServerResponseData(getExerciseTotalSetsResponse) {
    val totalSets = getExerciseTotalSetsResponse.totalSets
}