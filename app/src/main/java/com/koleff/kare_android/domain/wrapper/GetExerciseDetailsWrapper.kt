package com.koleff.kare_android.domain.wrapper

import com.koleff.kare_android.data.model.response.GetExerciseDetailsResponse

class GetExerciseDetailsWrapper(getExerciseDetailsResponse: GetExerciseDetailsResponse):
    ServerResponseData(getExerciseDetailsResponse) {
    val exerciseDetails = getExerciseDetailsResponse.exerciseDetails
}