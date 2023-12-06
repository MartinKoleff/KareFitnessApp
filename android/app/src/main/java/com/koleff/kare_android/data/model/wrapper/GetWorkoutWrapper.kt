package com.koleff.kare_android.data.model.wrapper

import com.koleff.kare_android.data.model.response.GetWorkoutResponse

class GetWorkoutWrapper(getWorkoutResponse: GetWorkoutResponse) :
    ServerResponseData(getWorkoutResponse) {
    val workout = getWorkoutResponse.workout
}