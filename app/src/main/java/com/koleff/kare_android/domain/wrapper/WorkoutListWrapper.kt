package com.koleff.kare_android.domain.wrapper

import com.koleff.kare_android.data.model.response.GetAllWorkoutsResponse

class WorkoutListWrapper(getAllWorkoutsResponse: GetAllWorkoutsResponse) :
    ServerResponseData(getAllWorkoutsResponse) {
    val workouts = getAllWorkoutsResponse.workouts
}