package com.koleff.kare_android.domain.wrapper

import com.koleff.kare_android.data.model.response.ExerciseResponse

class ExerciseWrapper(getExerciseResponse: ExerciseResponse) :
    ServerResponseData(getExerciseResponse) {
    val exercise = getExerciseResponse.exercise
}