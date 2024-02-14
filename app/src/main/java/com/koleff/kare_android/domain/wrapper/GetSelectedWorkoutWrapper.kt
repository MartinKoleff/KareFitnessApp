package com.koleff.kare_android.domain.wrapper

import com.koleff.kare_android.data.model.response.GetSelectedWorkoutResponse

class GetSelectedWorkoutWrapper(getSelectedWorkoutResponse: GetSelectedWorkoutResponse) :
    ServerResponseData(getSelectedWorkoutResponse) {
    val workout = getSelectedWorkoutResponse.workout
}