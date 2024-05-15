package com.koleff.kare_android.domain.wrapper

import com.koleff.kare_android.data.model.response.WorkoutsListResponse

class WorkoutListWrapper(workoutListResponse: WorkoutsListResponse) :
    ServerResponseData(workoutListResponse) {
    val workouts = workoutListResponse.workouts
}