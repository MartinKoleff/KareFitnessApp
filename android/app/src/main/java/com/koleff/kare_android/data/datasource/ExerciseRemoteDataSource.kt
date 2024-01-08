package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.common.Network
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.domain.wrapper.GetExercisesWrapper
import com.koleff.kare_android.data.model.request.GetExerciseDetailsRequest
import com.koleff.kare_android.data.model.request.GetExerciseRequest
import com.koleff.kare_android.data.model.request.GetExercisesRequest
import com.koleff.kare_android.domain.wrapper.GetExerciseDetailsWrapper
import com.koleff.kare_android.domain.wrapper.GetExerciseWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.data.remote.ExerciseApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExerciseRemoteDataSource @Inject constructor(
    private val exerciseApi: ExerciseApi,
    @IoDispatcher val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ExerciseDataSource {

    override suspend fun getExercises(muscleGroupId: Int): Flow<ResultWrapper<GetExercisesWrapper>> {
        val body = GetExercisesRequest(muscleGroupId)

        return Network.executeApiCall(dispatcher, { GetExercisesWrapper(exerciseApi.getExercises(body)) })
    }

    override suspend fun getExercise(exerciseId: Int): Flow<ResultWrapper<GetExerciseWrapper>> {
        val body = GetExerciseRequest(exerciseId)

        return Network.executeApiCall(dispatcher, { GetExerciseWrapper(exerciseApi.getExercise(body)) })
    }

    override suspend fun getExerciseDetails(exerciseId: Int): Flow<ResultWrapper<GetExerciseDetailsWrapper>> {
        val body = GetExerciseDetailsRequest(exerciseId)

        return Network.executeApiCall(dispatcher, { GetExerciseDetailsWrapper(exerciseApi.getExerciseDetails(body)) })
    }
}