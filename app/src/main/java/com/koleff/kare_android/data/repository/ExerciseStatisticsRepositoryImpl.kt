package com.koleff.kare_android.data.repository

import com.koleff.kare_android.data.datasource.ExerciseStatisticsDataSource
import com.koleff.kare_android.domain.repository.ExerciseStatisticsRepository
import com.koleff.kare_android.domain.wrapper.ExercisePRWrapper
import com.koleff.kare_android.domain.wrapper.ExerciseTotalRepsWrapper
import com.koleff.kare_android.domain.wrapper.ExerciseTotalSetsWrapper
import com.koleff.kare_android.domain.wrapper.ExerciseTotalWeightWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import kotlinx.coroutines.flow.Flow

class ExerciseStatisticsRepositoryImpl(private val exerciseStatisticsDataSource: ExerciseStatisticsDataSource) :
    ExerciseStatisticsRepository {
    override suspend fun getPR(exerciseId: Int): Flow<ResultWrapper<ExercisePRWrapper>> {
        return exerciseStatisticsDataSource.getPR(exerciseId)
    }

    override suspend fun getTotalRepsPerformed(exerciseId: Int): Flow<ResultWrapper<ExerciseTotalRepsWrapper>> {
        return exerciseStatisticsDataSource.getTotalRepsPerformed(exerciseId)
    }

    override suspend fun getTotalSetsPerformed(exerciseId: Int): Flow<ResultWrapper<ExerciseTotalSetsWrapper>> {
        return exerciseStatisticsDataSource.getTotalSetsPerformed(exerciseId)
    }

    override suspend fun getTotalWeightLifted(exerciseId: Int): Flow<ResultWrapper<ExerciseTotalWeightWrapper>> {
        return exerciseStatisticsDataSource.getTotalWeightLifted(exerciseId)
    }
}