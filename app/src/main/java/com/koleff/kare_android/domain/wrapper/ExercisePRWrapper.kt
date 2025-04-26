package com.koleff.kare_android.domain.wrapper

import com.koleff.kare_android.data.model.response.ExercisePRResponse


class ExercisePRWrapper(exercisePRResponse: ExercisePRResponse):
    ServerResponseData(exercisePRResponse) {
    val pr = exercisePRResponse.pr
}