package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.common.Network
import com.koleff.kare_android.data.model.dto.GetExercisesWrapper
import com.koleff.kare_android.data.model.request.GetExercisesRequest
import com.koleff.kare_android.data.model.response.base_response.ServerResponseData
import com.koleff.kare_android.data.model.wrapper.ResultWrapper
import com.koleff.kare_android.data.remote.ExerciseApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@AndroidEntryPoint
class ExerciseRemoteDataSource @Inject constructor(
    private val exerciseApi: ExerciseApi,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ExerciseDataSource {

    override suspend fun getExercises(): Flow<ResultWrapper<GetExercisesWrapper>> {
        val body = GetExercisesRequest() //TODO: fill with data...

        return Network.executeApiCall(dispatcher, { GetExercisesWrapper(exerciseApi.getExercises()) })
    }
}