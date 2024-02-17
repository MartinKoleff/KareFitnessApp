package com.koleff.kare_android.domain.wrapper

import com.koleff.kare_android.data.model.response.WorkoutDetailsListResponse

class WorkoutDetailsListWrapper(getAllWorkoutDetailsResponse: WorkoutDetailsListResponse) :
    ServerResponseData(getAllWorkoutDetailsResponse) {
    val workoutDetailsList = getAllWorkoutDetailsResponse.workoutDetailsList
}