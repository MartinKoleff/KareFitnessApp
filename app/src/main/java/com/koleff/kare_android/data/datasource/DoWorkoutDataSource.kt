package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.domain.wrapper.DoWorkoutWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.ui.state.DoWorkoutData
import kotlinx.coroutines.flow.Flow

interface DoWorkoutDataSource {

    suspend fun initialSetup(workoutDetailsDto: WorkoutDetailsDto): Flow<ResultWrapper<DoWorkoutWrapper>>

    suspend fun selectNextExercise(
        currentDoWorkoutData: DoWorkoutData
    ): Flow<ResultWrapper<DoWorkoutWrapper>>

    suspend fun skipNextSet(
        currentDoWorkoutData: DoWorkoutData
    ): Flow<ResultWrapper<DoWorkoutWrapper>>

    suspend fun updateExerciseSetsAfterTimer(currentDoWorkoutData: DoWorkoutData): Flow<ResultWrapper<DoWorkoutWrapper>>
}