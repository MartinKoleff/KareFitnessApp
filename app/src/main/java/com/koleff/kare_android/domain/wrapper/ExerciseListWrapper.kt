package com.koleff.kare_android.domain.wrapper

import com.koleff.kare_android.data.model.response.ExerciseListResponse

class ExerciseListWrapper(exerciseListResponse: ExerciseListResponse) :
    ServerResponseData(exerciseListResponse) {
    val exercises = exerciseListResponse.exercises
}