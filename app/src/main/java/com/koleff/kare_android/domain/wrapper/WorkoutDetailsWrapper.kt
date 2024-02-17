package com.koleff.kare_android.domain.wrapper

import com.koleff.kare_android.data.model.response.GetWorkoutDetailsResponse

class WorkoutDetailsWrapper(getWorkoutDetailsResponse: GetWorkoutDetailsResponse) :
    ServerResponseData(getWorkoutDetailsResponse) {
    val workoutDetails = getWorkoutDetailsResponse.workoutDetails
}