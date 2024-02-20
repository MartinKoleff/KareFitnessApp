package com.koleff.kare_android.domain.wrapper

import com.koleff.kare_android.data.model.response.WorkoutResponse

class WorkoutWrapper(getWorkoutResponse: WorkoutResponse) :
    ServerResponseData(getWorkoutResponse) {
    val workout = getWorkoutResponse.workout
}