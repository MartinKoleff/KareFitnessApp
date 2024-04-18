package com.koleff.kare_android.domain.repository

import com.koleff.kare_android.data.model.dto.DoWorkoutPerformanceMetricsDto
import com.koleff.kare_android.domain.wrapper.DoWorkoutPerformanceMetricsListWrapper
import com.koleff.kare_android.domain.wrapper.DoWorkoutPerformanceMetricsWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.ServerResponseData
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface DoWorkoutPerformanceMetricsRepository {

    suspend fun saveDoWorkoutPerformanceMetrics(performanceMetrics: DoWorkoutPerformanceMetricsDto): Flow<ResultWrapper<ServerResponseData>>

    suspend fun getAllDoWorkoutPerformanceMetricsById(id: Int, start: Date, end: Date): Flow<ResultWrapper<DoWorkoutPerformanceMetricsListWrapper>>

    suspend fun getDoWorkoutPerformanceMetricsById(id: Int): Flow<ResultWrapper<DoWorkoutPerformanceMetricsWrapper>>

    suspend fun getDoWorkoutPerformanceMetricsByWorkoutId(workoutId: Int): Flow<ResultWrapper<DoWorkoutPerformanceMetricsListWrapper>>

    suspend fun getAllDoWorkoutPerformanceMetricsByWorkoutId(workoutId: Int, start: Date, end: Date): Flow<ResultWrapper<DoWorkoutPerformanceMetricsListWrapper>>

    suspend fun getAllDoWorkoutPerformanceMetrics(): Flow<ResultWrapper<DoWorkoutPerformanceMetricsListWrapper>>

    suspend fun getAllDoWorkoutPerformanceMetrics(start: Date, end: Date): Flow<ResultWrapper<DoWorkoutPerformanceMetricsListWrapper>>

    suspend fun deleteDoWorkoutPerformanceMetrics(id: Int): Flow<ResultWrapper<ServerResponseData>>

    suspend fun updateWorkoutPerformanceMetrics(performanceMetrics: DoWorkoutPerformanceMetricsDto): Flow<ResultWrapper<DoWorkoutPerformanceMetricsWrapper>>
}