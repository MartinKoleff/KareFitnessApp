package com.koleff.kare_android.domain.wrapper

import com.koleff.kare_android.data.model.response.DuplicateExercisesResponse

class DuplicateExercisesWrapper(duplicateExercisesResponse: DuplicateExercisesResponse):
    ServerResponseData(duplicateExercisesResponse) {
    val containsDuplicates = duplicateExercisesResponse.containsDuplicates
}