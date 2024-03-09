package com.koleff.kare_android.domain.wrapper

import com.koleff.kare_android.data.model.response.ExerciseDetailsResponse

class ExerciseDetailsWrapper(getExerciseDetailsResponse: ExerciseDetailsResponse):
    ServerResponseData(getExerciseDetailsResponse) {
    val exerciseDetails = getExerciseDetailsResponse.exerciseDetails
}