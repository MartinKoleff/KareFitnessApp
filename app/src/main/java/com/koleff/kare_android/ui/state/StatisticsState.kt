package com.koleff.kare_android.ui.state

import com.koleff.kare_android.data.model.response.base_response.KareError

data class StatisticsState(
    val generalStatisticsState: GeneralStatisticsState = GeneralStatisticsState(),
    val workoutStatisticsState: WorkoutStatisticsState = WorkoutStatisticsState(),
    val exerciseStatisticsState: ExerciseStatisticsState = ExerciseStatisticsState(),
    override val isSuccessful: Boolean = false,
    override val isLoading: Boolean = false,
    override val isError: Boolean = false,
    override val error: KareError = KareError.GENERIC
) : BaseState(isSuccessful, isLoading, isError, error)