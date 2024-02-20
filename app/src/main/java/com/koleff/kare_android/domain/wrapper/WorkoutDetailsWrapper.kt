package com.koleff.kare_android.domain.wrapper

import com.koleff.kare_android.data.model.response.WorkoutDetailsResponse

class WorkoutDetailsWrapper(getWorkoutDetailsResponse: WorkoutDetailsResponse) :
    ServerResponseData(getWorkoutDetailsResponse) {
    val workoutDetails = getWorkoutDetailsResponse.workoutDetails
}