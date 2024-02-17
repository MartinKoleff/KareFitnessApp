package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.common.Network
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.domain.wrapper.ExerciseListWrapper
import com.koleff.kare_android.data.model.request.GetExerciseDetailsRequest
import com.koleff.kare_android.data.model.request.GetExerciseRequest
import com.koleff.kare_android.data.model.request.GetExercisesRequest
import com.koleff.kare_android.domain.wrapper.ExerciseDetailsWrapper
import com.koleff.kare_android.domain.wrapper.ExerciseWrapper
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

    override suspend fun getExercises(muscleGroupId: Int): Flow<ResultWrapper<ExerciseListWrapper>> {
        val body = GetExercisesRequest(muscleGroupId)

        return Network.executeApiCall(dispatcher, { ExerciseListWrapper(exerciseApi.getExercises(body)) })
    }

    override suspend fun getExercise(exerciseId: Int): Flow<ResultWrapper<ExerciseWrapper>> {
        val body = GetExerciseRequest(exerciseId)

        return Network.executeApiCall(dispatcher, { ExerciseWrapper(exerciseApi.getExercise(body)) })
    }

    override suspend fun getExerciseDetails(exerciseId: Int): Flow<ResultWrapper<ExerciseDetailsWrapper>> {
        val body = GetExerciseDetailsRequest(exerciseId)

        return Network.executeApiCall(dispatcher, { ExerciseDetailsWrapper(exerciseApi.getExerciseDetails(body)) })
    }
}