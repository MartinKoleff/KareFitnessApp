package com.koleff.kare_android.data.datasource.statistics.exercise

import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.common.network.ApiAuthorizationCallWrapper
import com.koleff.kare_android.data.model.request.ExerciseStatisticsRequest
import com.koleff.kare_android.data.remote.ExerciseStatisticsApi
import com.koleff.kare_android.domain.wrapper.ExercisePRWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.TotalRepsWrapper
import com.koleff.kare_android.domain.wrapper.TotalSetsWrapper
import com.koleff.kare_android.domain.wrapper.TotalWeightLiftedWrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExerciseStatisticsRemoteDataSource @Inject constructor(
    private val exerciseStatisticsApi: ExerciseStatisticsApi,
    private val apiAuthorizationCallWrapper: ApiAuthorizationCallWrapper,
    @IoDispatcher val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ExerciseStatisticsDataSource {
    override suspend fun getPR(exerciseId: Int): Flow<ResultWrapper<ExercisePRWrapper>> {
        val body = ExerciseStatisticsRequest(exerciseId)

        return apiAuthorizationCallWrapper.executeApiCall(dispatcher, {
            ExercisePRWrapper(
                exerciseStatisticsApi.getPR(body)
            )
        })
    }

    override suspend fun get1RepMax(exerciseId: Int): Flow<ResultWrapper<ExercisePRWrapper>> {
        val body = ExerciseStatisticsRequest(exerciseId)

        return apiAuthorizationCallWrapper.executeApiCall(dispatcher, {
            ExercisePRWrapper(
                exerciseStatisticsApi.get1RepMax(body)
            )
        })
    }

    override suspend fun getTotalRepsPerformed(exerciseId: Int): Flow<ResultWrapper<TotalRepsWrapper>> {
        val body = ExerciseStatisticsRequest(exerciseId)

        return apiAuthorizationCallWrapper.executeApiCall(dispatcher, {
            TotalRepsWrapper(
                exerciseStatisticsApi.getTotalRepsPerformed(body)
            )
        })
    }

    override suspend fun getTotalSetsPerformed(exerciseId: Int): Flow<ResultWrapper<TotalSetsWrapper>> {
        val body = ExerciseStatisticsRequest(exerciseId)

        return apiAuthorizationCallWrapper.executeApiCall(dispatcher, {
            TotalSetsWrapper(
                exerciseStatisticsApi.getTotalSetsPerformed(body)
            )
        })
    }

    override suspend fun getTotalWeightLifted(exerciseId: Int): Flow<ResultWrapper<TotalWeightLiftedWrapper>> {
        val body = ExerciseStatisticsRequest(exerciseId)

        return apiAuthorizationCallWrapper.executeApiCall(dispatcher, {
            TotalWeightLiftedWrapper(
                exerciseStatisticsApi.getTotalWeighLifted(body)
            )
        })
    }
}