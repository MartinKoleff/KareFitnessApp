package com.koleff.kare_android.data.repository

import com.koleff.kare_android.data.datasource.WorkoutStatisticsDataSource
import com.koleff.kare_android.domain.repository.WorkoutStatisticsRepository
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.TotalRepsWrapper
import com.koleff.kare_android.domain.wrapper.TotalSetsWrapper
import com.koleff.kare_android.domain.wrapper.TotalWeightLiftedWrapper
import com.koleff.kare_android.domain.wrapper.TotalTimesCompletedWrapper
import kotlinx.coroutines.flow.Flow

class WorkoutStatisticsRepositoryImpl(private val workoutStatisticsDataSource: WorkoutStatisticsDataSource) :
    WorkoutStatisticsRepository {
    override suspend fun getTotalTimesCompleted(workoutId: Int): Flow<ResultWrapper<TotalTimesCompletedWrapper>> {
        return workoutStatisticsDataSource.getTotalTimesCompleted(workoutId)
    }

    override suspend fun getTotalWeightLifted(workoutId: Int): Flow<ResultWrapper<TotalWeightLiftedWrapper>> {
       return workoutStatisticsDataSource.getTotalWeightLifted(workoutId)
    }

    override suspend fun getTotalRepsPerformed(workoutId: Int, exerciseId: Int): Flow<ResultWrapper<TotalRepsWrapper>> {
        return workoutStatisticsDataSource.getTotalRepsPerformed(workoutId, exerciseId)
    }

    override suspend fun getTotalSetsPerformed(workoutId: Int, exerciseId: Int): Flow<ResultWrapper<TotalSetsWrapper>> {
        return workoutStatisticsDataSource.getTotalSetsPerformed(workoutId, exerciseId)
    }
}