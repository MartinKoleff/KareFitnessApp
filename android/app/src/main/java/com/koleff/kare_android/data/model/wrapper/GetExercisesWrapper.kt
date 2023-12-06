package com.koleff.kare_android.data.model.wrapper

import com.koleff.kare_android.data.model.response.GetExercisesResponse
import com.koleff.kare_android.data.model.response.base_response.ServerResponseData

class GetExercisesWrapper(getExercisesResponse: GetExercisesResponse) :
    ServerResponseData(getExercisesResponse) {
    val exercisesList = getExercisesResponse.exercisesList
//    val paginationData = getExercisesResponse.paginationData
}