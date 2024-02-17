package com.koleff.kare_android.domain.wrapper

import com.koleff.kare_android.data.model.response.GetAllWorkoutDetailsResponse

class GetAllWorkoutDetailsWrapper(getAllWorkoutDetailsResponse: GetAllWorkoutDetailsResponse) :
    ServerResponseData(getAllWorkoutDetailsResponse) {
    val workoutDetailsList = getAllWorkoutDetailsResponse.workoutDetailsList
}