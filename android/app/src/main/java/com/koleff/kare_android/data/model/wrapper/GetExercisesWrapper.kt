package com.koleff.kare_android.data.model.wrapper

import com.koleff.kare_android.data.model.response.GetExercisesResponse

class GetExercisesWrapper(getExercisesResponse: GetExercisesResponse) :
    ServerResponseData(getExercisesResponse) {
    val exercises = getExercisesResponse.exercises
//    val paginationData = getExercisesResponse.paginationData
}