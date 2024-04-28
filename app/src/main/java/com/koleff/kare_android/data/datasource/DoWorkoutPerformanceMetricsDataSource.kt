package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.data.model.dto.DoWorkoutExerciseSetDto
import com.koleff.kare_android.data.model.dto.DoWorkoutPerformanceMetricsDto
import com.koleff.kare_android.domain.wrapper.DoWorkoutPerformanceMetricsListWrapper
import com.koleff.kare_android.domain.wrapper.DoWorkoutPerformanceMetricsWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.ServerResponseData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Date

interface DoWorkoutPerformanceMetricsDataSource {
    suspend fun saveDoWorkoutPerformanceMetrics(performanceMetrics: DoWorkoutPerformanceMetricsDto): Flow<ResultWrapper<DoWorkoutPerformanceMetricsWrapper>>

    suspend fun getAllDoWorkoutPerformanceMetricsById(id: Int, start: Date, end: Date): Flow<ResultWrapper<DoWorkoutPerformanceMetricsListWrapper>>

    suspend fun getDoWorkoutPerformanceMetricsById(id: Int): Flow<ResultWrapper<DoWorkoutPerformanceMetricsWrapper>>

    suspend fun getDoWorkoutPerformanceMetricsByWorkoutId(workoutId: Int): Flow<ResultWrapper<DoWorkoutPerformanceMetricsListWrapper>>

    suspend fun getAllDoWorkoutPerformanceMetricsByWorkoutId(workoutId: Int, start: Date, end: Date): Flow<ResultWrapper<DoWorkoutPerformanceMetricsListWrapper>>

    suspend fun getAllDoWorkoutPerformanceMetrics(): Flow<ResultWrapper<DoWorkoutPerformanceMetricsListWrapper>>

    suspend fun getAllDoWorkoutPerformanceMetrics(start: Date, end: Date): Flow<ResultWrapper<DoWorkoutPerformanceMetricsListWrapper>>

    suspend fun deleteDoWorkoutPerformanceMetrics(id: Int): Flow<ResultWrapper<ServerResponseData>>

    suspend fun updateDoWorkoutPerformanceMetrics(performanceMetrics: DoWorkoutPerformanceMetricsDto): Flow<ResultWrapper<DoWorkoutPerformanceMetricsWrapper>>

    suspend fun saveDoWorkoutExerciseSet(exerciseSet: DoWorkoutExerciseSetDto): Flow<ResultWrapper<ServerResponseData>>

    suspend fun saveAllDoWorkoutExerciseSets(exerciseSets: List<DoWorkoutExerciseSetDto>): Flow<ResultWrapper<ServerResponseData>>
}