package com.koleff.kare_android.data.repository

import com.koleff.kare_android.data.datasource.statistics.exercise.ExerciseStatisticsDataSource
import com.koleff.kare_android.domain.repository.ExerciseStatisticsRepository
import com.koleff.kare_android.domain.wrapper.ExercisePRWrapper
import com.koleff.kare_android.domain.wrapper.TotalRepsWrapper
import com.koleff.kare_android.domain.wrapper.TotalSetsWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.TotalWeightLiftedWrapper
import kotlinx.coroutines.flow.Flow

class ExerciseStatisticsRepositoryImpl(private val exerciseStatisticsDataSource: ExerciseStatisticsDataSource) :
    ExerciseStatisticsRepository {
    override suspend fun getPR(exerciseId: Int): Flow<ResultWrapper<ExercisePRWrapper>> {
        return exerciseStatisticsDataSource.getPR(exerciseId)
    }

    override suspend fun get1RepMax(exerciseId: Int): Flow<ResultWrapper<ExercisePRWrapper>> {
        return exerciseStatisticsDataSource.get1RepMax(exerciseId)
    }

    override suspend fun getTotalRepsPerformed(exerciseId: Int): Flow<ResultWrapper<TotalRepsWrapper>> {
        return exerciseStatisticsDataSource.getTotalRepsPerformed(exerciseId)
    }

    override suspend fun getTotalSetsPerformed(exerciseId: Int): Flow<ResultWrapper<TotalSetsWrapper>> {
        return exerciseStatisticsDataSource.getTotalSetsPerformed(exerciseId)
    }

    override suspend fun getTotalWeightLifted(exerciseId: Int): Flow<ResultWrapper<TotalWeightLiftedWrapper>> {
        return exerciseStatisticsDataSource.getTotalWeightLifted(exerciseId)
    }
}