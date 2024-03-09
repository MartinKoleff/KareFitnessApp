package com.koleff.kare_android.domain.wrapper

import com.koleff.kare_android.data.model.response.GetExercisesResponse

class ExerciseListWrapper(getExercisesResponse: GetExercisesResponse) :
    ServerResponseData(getExercisesResponse) {
    val exercises = getExercisesResponse.exercises
}