package com.koleff.kare_android.domain.wrapper

import com.koleff.kare_android.data.model.response.GetExerciseResponse

class GetExerciseWrapper(getExerciseResponse: GetExerciseResponse) :
    ServerResponseData(getExerciseResponse) {
    val exercise = getExerciseResponse.exercise
}