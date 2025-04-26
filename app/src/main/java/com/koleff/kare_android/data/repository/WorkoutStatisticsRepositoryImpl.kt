package com.koleff.kare_android.data.repository

import com.koleff.kare_android.data.datasource.WorkoutStatisticsDataSource
import com.koleff.kare_android.domain.repository.WorkoutStatisticsRepository
import com.koleff.kare_android.domain.wrapper.DatesOfCompletionWrapper
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

    override suspend fun getDatesOfCompletionForWorkout(workoutId: Int): Flow<ResultWrapper<DatesOfCompletionWrapper>> {
        return workoutStatisticsDataSource.getDatesOfCompletionForWorkout(workoutId)
    }

    override suspend fun getTotalRepsPerformed(workoutId: Int): Flow<ResultWrapper<TotalRepsWrapper>> {
        return workoutStatisticsDataSource.getTotalRepsPerformed(workoutId)
    }

    override suspend fun getTotalSetsPerformed(workoutId: Int): Flow<ResultWrapper<TotalSetsWrapper>> {
        return workoutStatisticsDataSource.getTotalSetsPerformed(workoutId)
    }
}