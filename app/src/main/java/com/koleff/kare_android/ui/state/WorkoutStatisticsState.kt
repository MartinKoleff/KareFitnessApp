package com.koleff.kare_android.ui.state

import com.koleff.kare_android.data.model.response.base_response.KareError

data class WorkoutStatisticsState (
    val getWorkoutTotalTimesCompletedState: TotalTimesCompletedState = TotalTimesCompletedState(),
    val getWorkoutTotalRepsPerformedState: TotalRepsPerformedState = TotalRepsPerformedState(),
    val getWorkoutTotalSetsPerformedState: TotalSetsPerformedState = TotalSetsPerformedState(),
    val getWorkoutTotalWeightLiftedState: TotalWeightLiftedState = TotalWeightLiftedState(),
    val getDatesOfCompletionState: DatesOfCompletionState = DatesOfCompletionState(),
    override val isSuccessful: Boolean = false,
    override val isLoading: Boolean = false,
    override val isError: Boolean = false,
    override val error: KareError = KareError.GENERIC
): BaseState(isSuccessful, isLoading, isError, error)