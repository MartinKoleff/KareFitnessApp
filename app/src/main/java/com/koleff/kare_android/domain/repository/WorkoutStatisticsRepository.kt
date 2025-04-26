package com.koleff.kare_android.domain.repository

import com.koleff.kare_android.domain.wrapper.DatesOfCompletionWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.TotalRepsWrapper
import com.koleff.kare_android.domain.wrapper.TotalSetsWrapper
import com.koleff.kare_android.domain.wrapper.TotalWeightLiftedWrapper
import com.koleff.kare_android.domain.wrapper.TotalTimesCompletedWrapper
import kotlinx.coroutines.flow.Flow

interface WorkoutStatisticsRepository {

    suspend fun getTotalTimesCompleted(workoutId: Int): Flow<ResultWrapper<TotalTimesCompletedWrapper>>

    suspend fun getTotalWeightLifted(workoutId: Int): Flow<ResultWrapper<TotalWeightLiftedWrapper>>

    suspend fun getDatesOfCompletionForWorkout(workoutId: Int): Flow<ResultWrapper<DatesOfCompletionWrapper>>

    suspend fun getTotalRepsPerformed(workoutId: Int): Flow<ResultWrapper<TotalRepsWrapper>>

    suspend fun getTotalSetsPerformed(workoutId: Int): Flow<ResultWrapper<TotalSetsWrapper>>
}