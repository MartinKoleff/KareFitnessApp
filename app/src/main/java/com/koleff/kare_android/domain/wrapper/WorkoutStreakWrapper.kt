package com.koleff.kare_android.domain.wrapper

import com.koleff.kare_android.data.model.response.WorkoutStreakResponse


class WorkoutStreakWrapper(workoutStreakResponse: WorkoutStreakResponse):
    ServerResponseData(workoutStreakResponse) {
    val streak = workoutStreakResponse.streak
}