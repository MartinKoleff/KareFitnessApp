package com.koleff.kare_android.ui.state

import com.koleff.kare_android.data.model.response.base_response.KareError

data class ExerciseStatisticsState (
    val getExercisePRState: ExercisePRState = ExercisePRState(),
    val getExercise1RepMaxState: ExercisePRState = ExercisePRState(),
    val getExerciseTotalRepsPerformedState: TotalRepsPerformedState = TotalRepsPerformedState(),
    val getExerciseTotalSetsPerformedState: TotalSetsPerformedState = TotalSetsPerformedState(),
    val getExerciseTotalWeightLiftedState: TotalWeightLiftedState = TotalWeightLiftedState(),
    override val isSuccessful: Boolean = false,
    override val isLoading: Boolean = false,
    override val isError: Boolean = false,
    override val error: KareError = KareError.GENERIC
): BaseState(isSuccessful, isLoading, isError, error)