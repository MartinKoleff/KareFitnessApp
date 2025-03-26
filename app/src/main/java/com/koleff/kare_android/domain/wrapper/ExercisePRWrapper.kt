package com.koleff.kare_android.domain.wrapper

import com.koleff.kare_android.data.model.response.GetExercisePRResponse


class ExercisePRWrapper(getExercisePRResponse: GetExercisePRResponse):
    ServerResponseData(getExercisePRResponse) {
    val pr = getExercisePRResponse.pr
}