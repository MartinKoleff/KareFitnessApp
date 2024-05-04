package com.koleff.kare_android.domain.wrapper

import com.koleff.kare_android.data.model.response.WorkoutConfigurationResponse

class WorkoutConfigurationWrapper(getWorkoutConfigurationResponse: WorkoutConfigurationResponse) :
    ServerResponseData(getWorkoutConfigurationResponse) {
    val workoutConfiguration = getWorkoutConfigurationResponse.workoutConfiguration
}