package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.common.network.ApiAuthorizationCallWrapper
import com.koleff.kare_android.data.model.dto.DoWorkoutExerciseSetDto
import com.koleff.kare_android.data.model.dto.DoWorkoutPerformanceMetricsDto
import com.koleff.kare_android.data.model.request.DeletePerformanceMetricsRequest
import com.koleff.kare_android.data.model.request.FetchPerformanceMetricsByDateAndIdRequest
import com.koleff.kare_android.data.model.request.FetchPerformanceMetricsByDateAndWorkoutIdRequest
import com.koleff.kare_android.data.model.request.FetchPerformanceMetricsByDateRequest
import com.koleff.kare_android.data.model.request.FetchPerformanceMetricsByIdRequest
import com.koleff.kare_android.data.model.request.FetchWorkoutByIdRequest
import com.koleff.kare_android.data.model.request.SaveDoWorkoutExerciseSetRequest
import com.koleff.kare_android.data.model.request.SaveDoWorkoutExerciseSetsRequest
import com.koleff.kare_android.data.model.request.UpdatePerformanceMetricsRequest
import com.koleff.kare_android.data.remote.DoWorkoutPerformanceMetricsApi
import com.koleff.kare_android.domain.wrapper.DoWorkoutPerformanceMetricsListWrapper
import com.koleff.kare_android.domain.wrapper.DoWorkoutPerformanceMetricsWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.ServerResponseData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject

typealias FetchPerformanceMetricsByWorkoutIdRequest = FetchWorkoutByIdRequest

class DoWorkoutPerformanceMetricsRemoteDataSource @Inject constructor(
    private val doWorkoutPerformanceMetricsApi: DoWorkoutPerformanceMetricsApi,
    private val apiAuthorizationCallWrapper: ApiAuthorizationCallWrapper,
    @IoDispatcher val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : DoWorkoutPerformanceMetricsDataSource {

    override suspend fun getAllDoWorkoutPerformanceMetricsById(
        id: Int,
        start: Date,
        end: Date
    ): Flow<ResultWrapper<DoWorkoutPerformanceMetricsListWrapper>> {
        val body = FetchPerformanceMetricsByDateAndIdRequest(
            id = id,
            start = start,
            end = end
        )

        return apiAuthorizationCallWrapper.executeApiCall(dispatcher, {
            DoWorkoutPerformanceMetricsListWrapper(
                doWorkoutPerformanceMetricsApi.getAllDoWorkoutPerformanceMetrics(body)
            )
        })
    }

    override suspend fun getDoWorkoutPerformanceMetricsById(id: Int): Flow<ResultWrapper<DoWorkoutPerformanceMetricsWrapper>> {
        val body = FetchPerformanceMetricsByIdRequest(
            id = id,
        )

        return apiAuthorizationCallWrapper.executeApiCall(dispatcher, {
            DoWorkoutPerformanceMetricsWrapper(
                doWorkoutPerformanceMetricsApi.getAllDoWorkoutPerformanceMetrics(body)
            )
        })
    }

    override suspend fun getDoWorkoutPerformanceMetricsByWorkoutId(workoutId: Int): Flow<ResultWrapper<DoWorkoutPerformanceMetricsListWrapper>> {
        val body = FetchPerformanceMetricsByWorkoutIdRequest(
            workoutId = workoutId,
        )

        return apiAuthorizationCallWrapper.executeApiCall(dispatcher, {
            DoWorkoutPerformanceMetricsListWrapper(
                doWorkoutPerformanceMetricsApi.getDoWorkoutPerformanceMetrics(body)
            )
        })
    }

    override suspend fun getAllDoWorkoutPerformanceMetricsByWorkoutId(
        workoutId: Int,
        start: Date,
        end: Date
    ): Flow<ResultWrapper<DoWorkoutPerformanceMetricsListWrapper>> {
        val body = FetchPerformanceMetricsByDateAndWorkoutIdRequest(
            workoutId = workoutId,
            start = start,
            end = end
        )

        return apiAuthorizationCallWrapper.executeApiCall(dispatcher, {
            DoWorkoutPerformanceMetricsListWrapper(
                doWorkoutPerformanceMetricsApi.getAllDoWorkoutPerformanceMetrics(body)
            )
        })
    }

    override suspend fun getAllDoWorkoutPerformanceMetrics(): Flow<ResultWrapper<DoWorkoutPerformanceMetricsListWrapper>> {
        return apiAuthorizationCallWrapper.executeApiCall(dispatcher, {
            DoWorkoutPerformanceMetricsListWrapper(
                doWorkoutPerformanceMetricsApi.getAllDoWorkoutPerformanceMetrics()
            )
        })
    }

    override suspend fun getAllDoWorkoutPerformanceMetrics(
        start: Date,
        end: Date
    ): Flow<ResultWrapper<DoWorkoutPerformanceMetricsListWrapper>> {
        val body = FetchPerformanceMetricsByDateRequest(
            start = start,
            end = end
        )

        return apiAuthorizationCallWrapper.executeApiCall(dispatcher, {
            DoWorkoutPerformanceMetricsListWrapper(
                doWorkoutPerformanceMetricsApi.getAllDoWorkoutPerformanceMetrics(body)
            )
        })
    }

    override suspend fun saveDoWorkoutPerformanceMetrics(
        performanceMetrics: DoWorkoutPerformanceMetricsDto
    ): Flow<ResultWrapper<DoWorkoutPerformanceMetricsWrapper>> {
        val body = UpdatePerformanceMetricsRequest(performanceMetrics)

        return apiAuthorizationCallWrapper.executeApiCall(dispatcher, {
            DoWorkoutPerformanceMetricsWrapper(
                doWorkoutPerformanceMetricsApi.saveDoWorkoutPerformanceMetrics(body)
            )
        })
    }

    override suspend fun updateDoWorkoutPerformanceMetrics(
        performanceMetrics: DoWorkoutPerformanceMetricsDto
    ): Flow<ResultWrapper<DoWorkoutPerformanceMetricsWrapper>> {
        val body = UpdatePerformanceMetricsRequest(performanceMetrics)

        return apiAuthorizationCallWrapper.executeApiCall(dispatcher, {
            DoWorkoutPerformanceMetricsWrapper(
                doWorkoutPerformanceMetricsApi.updateDoWorkoutPerformanceMetrics(body)
            )
        })
    }

    override suspend fun deleteDoWorkoutPerformanceMetrics(id: Int): Flow<ResultWrapper<ServerResponseData>> {
        val body = DeletePerformanceMetricsRequest(
            id = id,
        )

        return apiAuthorizationCallWrapper.executeApiCall(dispatcher, {
            ServerResponseData(
                doWorkoutPerformanceMetricsApi.deleteDoWorkoutPerformanceMetrics(body)
            )
        })
    }

    override suspend fun saveDoWorkoutExerciseSet(
        exerciseSet: DoWorkoutExerciseSetDto
    ): Flow<ResultWrapper<ServerResponseData>> {
        val body = SaveDoWorkoutExerciseSetRequest(exerciseSet)

        return apiAuthorizationCallWrapper.executeApiCall(dispatcher, {
            ServerResponseData(
                doWorkoutPerformanceMetricsApi.saveDoWorkoutExerciseSet(body)
            )
        })
    }

    override suspend fun saveAllDoWorkoutExerciseSets(
        exerciseSets: List<DoWorkoutExerciseSetDto>
    ): Flow<ResultWrapper<ServerResponseData>> {
        val body = SaveDoWorkoutExerciseSetsRequest(exerciseSets)

        return apiAuthorizationCallWrapper.executeApiCall(dispatcher, {
            ServerResponseData(
                doWorkoutPerformanceMetricsApi.saveAllDoWorkoutExerciseSets(body)
            )
        })
    }
}