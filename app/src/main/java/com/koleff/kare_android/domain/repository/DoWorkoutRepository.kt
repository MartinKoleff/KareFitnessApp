package com.koleff.kare_android.domain.repository

import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.domain.wrapper.DoWorkoutWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.ui.state.DoWorkoutData
import kotlinx.coroutines.flow.Flow

interface DoWorkoutRepository {

    suspend fun initialSetup(workoutDetails: WorkoutDetailsDto): Flow<ResultWrapper<DoWorkoutWrapper>>

    suspend fun updateExerciseSetsAfterTimer(currentDoWorkoutData: DoWorkoutData): Flow<ResultWrapper<DoWorkoutWrapper>>
}