package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.common.network.ApiAuthorizationCallWrapper
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.request.UpdateExerciseSetsRequest
import com.koleff.kare_android.data.model.request.UpdateWorkoutDetailsRequest
import com.koleff.kare_android.data.remote.DoWorkoutApi
import com.koleff.kare_android.domain.wrapper.DoWorkoutWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.ui.state.DoWorkoutData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

typealias DoWorkoutSetupRequest = UpdateWorkoutDetailsRequest

class DoWorkoutRemoteDataSource @Inject constructor(
    private val doWorkoutApi: DoWorkoutApi,
    private val apiAuthorizationCallWrapper: ApiAuthorizationCallWrapper,
    @IoDispatcher val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : DoWorkoutDataSource{

    override suspend fun initialSetup(workoutDetailsDto: WorkoutDetailsDto): Flow<ResultWrapper<DoWorkoutWrapper>> {
        val body = DoWorkoutSetupRequest(workoutDetailsDto)

        return apiAuthorizationCallWrapper.executeApiCall(dispatcher, {
            DoWorkoutWrapper(
                doWorkoutApi.initialSetup(body)
            )
        })
    }

    override suspend fun updateExerciseSetsAfterTimer(currentDoWorkoutData: DoWorkoutData): Flow<ResultWrapper<DoWorkoutWrapper>> {
        val body = UpdateExerciseSetsRequest(currentDoWorkoutData)

        return apiAuthorizationCallWrapper.executeApiCall(dispatcher, {
            DoWorkoutWrapper(
                doWorkoutApi.updateExerciseSetsAfterTimer(body)
            )
        })
    }
}