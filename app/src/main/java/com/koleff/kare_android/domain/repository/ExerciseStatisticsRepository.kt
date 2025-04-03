package com.koleff.kare_android.domain.repository

import com.koleff.kare_android.domain.wrapper.ExercisePRWrapper
import com.koleff.kare_android.domain.wrapper.TotalRepsWrapper
import com.koleff.kare_android.domain.wrapper.TotalSetsWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.TotalWeightLiftedWrapper
import kotlinx.coroutines.flow.Flow

interface ExerciseStatisticsRepository {

    suspend fun getPR(exerciseId: Int): Flow<ResultWrapper<ExercisePRWrapper>>

    suspend fun get1RepMax(exerciseId: Int): Flow<ResultWrapper<ExercisePRWrapper>>

    suspend fun getTotalRepsPerformed(exerciseId: Int): Flow<ResultWrapper<TotalRepsWrapper>>

    suspend fun getTotalSetsPerformed(exerciseId: Int): Flow<ResultWrapper<TotalSetsWrapper>>

    suspend fun getTotalWeightLifted(exerciseId: Int): Flow<ResultWrapper<TotalWeightLiftedWrapper>>
}