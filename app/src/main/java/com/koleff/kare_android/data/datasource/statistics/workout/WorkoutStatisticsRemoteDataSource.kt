package com.koleff.kare_android.data.datasource.statistics.workout

import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.common.network.ApiAuthorizationCallWrapper
import com.koleff.kare_android.data.model.request.FetchByWorkoutIdRequest
import com.koleff.kare_android.data.remote.WorkoutStatisticsApi
import com.koleff.kare_android.domain.wrapper.DatesOfCompletionWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.TotalRepsWrapper
import com.koleff.kare_android.domain.wrapper.TotalSetsWrapper
import com.koleff.kare_android.domain.wrapper.TotalTimesCompletedWrapper
import com.koleff.kare_android.domain.wrapper.TotalWeightLiftedWrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WorkoutStatisticsRemoteDataSource @Inject constructor(
    private val workoutStatisticsApi: WorkoutStatisticsApi,
    private val apiAuthorizationCallWrapper: ApiAuthorizationCallWrapper,
    @IoDispatcher private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : WorkoutStatisticsDataSource {
    override suspend fun getTotalTimesCompleted(workoutId: Int): Flow<ResultWrapper<TotalTimesCompletedWrapper>> {
        val body = FetchByWorkoutIdRequest(workoutId)

        return apiAuthorizationCallWrapper.executeApiCall(dispatcher, {
            TotalTimesCompletedWrapper(
                workoutStatisticsApi.getTotalTimesCompleted(body)
            )
        })
    }

    override suspend fun getTotalWeightLifted(workoutId: Int): Flow<ResultWrapper<TotalWeightLiftedWrapper>> {
        val body = FetchByWorkoutIdRequest(workoutId)

        return apiAuthorizationCallWrapper.executeApiCall(dispatcher, {
            TotalWeightLiftedWrapper(
                workoutStatisticsApi.getTotalWeightLifted(body)
            )
        })
    }

    override suspend fun getDatesOfCompletionForWorkout(workoutId: Int): Flow<ResultWrapper<DatesOfCompletionWrapper>> {
        val body = FetchByWorkoutIdRequest(workoutId)

        return apiAuthorizationCallWrapper.executeApiCall(dispatcher, {
            DatesOfCompletionWrapper(
                workoutStatisticsApi.getDatesOfCompletionForWorkout(body)
            )
        })
    }

    override suspend fun getTotalRepsPerformed(workoutId: Int): Flow<ResultWrapper<TotalRepsWrapper>> {
        val body = FetchByWorkoutIdRequest(workoutId)

        return apiAuthorizationCallWrapper.executeApiCall(dispatcher, {
            TotalRepsWrapper(
                workoutStatisticsApi.getTotalRepsPerformed(body)
            )
        })
    }

    override suspend fun getTotalSetsPerformed(workoutId: Int): Flow<ResultWrapper<TotalSetsWrapper>> {
        val body = FetchByWorkoutIdRequest(workoutId)

        return apiAuthorizationCallWrapper.executeApiCall(dispatcher, {
            TotalSetsWrapper(
                workoutStatisticsApi.getTotalSetsPerformed(body)
            )
        })
    }

}