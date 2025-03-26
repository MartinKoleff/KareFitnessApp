package com.koleff.kare_android.domain.repository

import com.koleff.kare_android.domain.wrapper.ExercisePRWrapper
import com.koleff.kare_android.domain.wrapper.ExerciseTotalRepsWrapper
import com.koleff.kare_android.domain.wrapper.ExerciseTotalSetsWrapper
import com.koleff.kare_android.domain.wrapper.ExerciseTotalWeightWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface ExerciseStatisticsRepository {

    suspend fun getPR(exerciseId: Int): Flow<ResultWrapper<ExercisePRWrapper>>

    suspend fun getTotalRepsPerformed(exerciseId: Int): Flow<ResultWrapper<ExerciseTotalRepsWrapper>>

    suspend fun getTotalSetsPerformed(exerciseId: Int): Flow<ResultWrapper<ExerciseTotalSetsWrapper>>

    suspend fun getTotalWeightLifted(exerciseId: Int): Flow<ResultWrapper<ExerciseTotalWeightWrapper>>
}