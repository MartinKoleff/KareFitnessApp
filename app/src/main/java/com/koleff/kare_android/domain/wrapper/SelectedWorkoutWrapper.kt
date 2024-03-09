package com.koleff.kare_android.domain.wrapper

import com.koleff.kare_android.data.model.response.SelectedWorkoutResponse

class SelectedWorkoutWrapper(getSelectedWorkoutResponse: SelectedWorkoutResponse) :
    ServerResponseData(getSelectedWorkoutResponse) {
    val workout = getSelectedWorkoutResponse.workout
}