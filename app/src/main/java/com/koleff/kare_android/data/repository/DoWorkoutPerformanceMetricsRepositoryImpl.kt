package com.koleff.kare_android.data.repository

import com.koleff.kare_android.data.datasource.DoWorkoutPerformanceMetricsDataSource
import com.koleff.kare_android.data.model.dto.DoWorkoutExerciseSetDto
import com.koleff.kare_android.data.model.dto.DoWorkoutPerformanceMetricsDto
import com.koleff.kare_android.domain.repository.DoWorkoutPerformanceMetricsRepository
import com.koleff.kare_android.domain.wrapper.DoWorkoutPerformanceMetricsListWrapper
import com.koleff.kare_android.domain.wrapper.DoWorkoutPerformanceMetricsWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.ServerResponseData
import kotlinx.coroutines.flow.Flow
import java.util.Date

class DoWorkoutPerformanceMetricsRepositoryImpl(
    private val doWorkoutPerformanceMetricsDataSource: DoWorkoutPerformanceMetricsDataSource
) : DoWorkoutPerformanceMetricsRepository {
    override suspend fun saveDoWorkoutPerformanceMetrics(performanceMetrics: DoWorkoutPerformanceMetricsDto): Flow<ResultWrapper<ServerResponseData>> {
        return doWorkoutPerformanceMetricsDataSource.saveDoWorkoutPerformanceMetrics(
            performanceMetrics
        )
    }

    override suspend fun getAllDoWorkoutPerformanceMetricsById(
        id: Int,
        start: Date,
        end: Date
    ): Flow<ResultWrapper<DoWorkoutPerformanceMetricsListWrapper>> {
        return doWorkoutPerformanceMetricsDataSource.getAllDoWorkoutPerformanceMetricsById(
            id,
            start,
            end
        )
    }

    override suspend fun getDoWorkoutPerformanceMetricsById(id: Int): Flow<ResultWrapper<DoWorkoutPerformanceMetricsWrapper>> {
        return doWorkoutPerformanceMetricsDataSource.getDoWorkoutPerformanceMetricsById(id)
    }

    override suspend fun getDoWorkoutPerformanceMetricsByWorkoutId(workoutId: Int): Flow<ResultWrapper<DoWorkoutPerformanceMetricsListWrapper>> {
        return doWorkoutPerformanceMetricsDataSource.getDoWorkoutPerformanceMetricsByWorkoutId(
            workoutId
        )
    }

    override suspend fun getAllDoWorkoutPerformanceMetricsByWorkoutId(
        workoutId: Int,
        start: Date,
        end: Date
    ): Flow<ResultWrapper<DoWorkoutPerformanceMetricsListWrapper>> {
        return doWorkoutPerformanceMetricsDataSource.getAllDoWorkoutPerformanceMetricsByWorkoutId(
            workoutId,
            start,
            end
        )
    }

    override suspend fun getAllDoWorkoutPerformanceMetrics(): Flow<ResultWrapper<DoWorkoutPerformanceMetricsListWrapper>> {
        return doWorkoutPerformanceMetricsDataSource.getAllDoWorkoutPerformanceMetrics()
    }

    override suspend fun getAllDoWorkoutPerformanceMetrics(
        start: Date,
        end: Date
    ): Flow<ResultWrapper<DoWorkoutPerformanceMetricsListWrapper>> {
        return doWorkoutPerformanceMetricsDataSource.getAllDoWorkoutPerformanceMetrics(start, end)
    }

    override suspend fun deleteDoWorkoutPerformanceMetrics(id: Int): Flow<ResultWrapper<ServerResponseData>> {
        return doWorkoutPerformanceMetricsDataSource.deleteDoWorkoutPerformanceMetrics(id)
    }

    override suspend fun updateWorkoutPerformanceMetrics(performanceMetrics: DoWorkoutPerformanceMetricsDto): Flow<ResultWrapper<DoWorkoutPerformanceMetricsWrapper>> {
        return doWorkoutPerformanceMetricsDataSource.updateWorkoutPerformanceMetrics(
            performanceMetrics
        )
    }

    override suspend fun saveDoWorkoutExerciseSet(exerciseSet: DoWorkoutExerciseSetDto): Flow<ResultWrapper<ServerResponseData>> {
        return doWorkoutPerformanceMetricsDataSource.saveDoWorkoutExerciseSet(exerciseSet)
    }

    override suspend fun saveAllDoWorkoutExerciseSet(exerciseSets: List<DoWorkoutExerciseSetDto>): Flow<ResultWrapper<ServerResponseData>> {
        return doWorkoutPerformanceMetricsDataSource.saveAllDoWorkoutExerciseSet(exerciseSets)
    }
}