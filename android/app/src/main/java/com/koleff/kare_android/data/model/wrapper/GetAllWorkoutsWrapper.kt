package com.koleff.kare_android.data.model.wrapper

import com.koleff.kare_android.data.model.response.GetAllWorkoutsResponse

class GetAllWorkoutsWrapper(getAllWorkoutsResponse: GetAllWorkoutsResponse) :
    ServerResponseData(getAllWorkoutsResponse) {
    val workouts = getAllWorkoutsResponse.workouts
//    val paginationData = getExercisesResponse.paginationData
}