package com.koleff.kare_android.domain.wrapper

import com.koleff.kare_android.data.model.response.WorkoutsListResponse

class WorkoutListWrapper(getAllWorkoutsResponse: WorkoutsListResponse) :
    ServerResponseData(getAllWorkoutsResponse) {
    val workouts = getAllWorkoutsResponse.workouts
}