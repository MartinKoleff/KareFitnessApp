package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.common.network.Network
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.data.model.request.DeleteExerciseSetRequest
import com.koleff.kare_android.domain.wrapper.ExerciseListWrapper
import com.koleff.kare_android.data.model.request.FetchExerciseRequest
import com.koleff.kare_android.data.model.request.FetchExercisesByMuscleGroupRequest
import com.koleff.kare_android.data.model.request.FetchExercisesByWorkoutIdRequest
import com.koleff.kare_android.domain.wrapper.ExerciseDetailsWrapper
import com.koleff.kare_android.domain.wrapper.ExerciseWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.data.remote.ExerciseApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

class ExerciseRemoteDataSource @Inject constructor(
    private val exerciseApi: ExerciseApi,
    @IoDispatcher val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ExerciseDataSource {

    override suspend fun getExercise(
        exerciseId: Int,
        workoutId: Int
    ): Flow<ResultWrapper<ExerciseWrapper>> {
        val body = FetchExerciseRequest(exerciseId, Constants.CATALOG_EXERCISE_ID)

        return Network.executeApiCall(dispatcher, { ExerciseWrapper(exerciseApi.getExercise(body)) })
    }

    override suspend fun getCatalogExercise(exerciseId: Int): Flow<ResultWrapper<ExerciseWrapper>> {
        val body = FetchExerciseRequest(exerciseId, Constants.CATALOG_EXERCISE_ID)

        return Network.executeApiCall(dispatcher, { ExerciseWrapper(exerciseApi.getCatalogExercise(body)) })
    }

    override suspend fun getCatalogExercises(muscleGroupId: Int): Flow<ResultWrapper<ExerciseListWrapper>> {
        val body = FetchExercisesByMuscleGroupRequest(muscleGroupId)

        return Network.executeApiCall(dispatcher, { ExerciseListWrapper(exerciseApi.getCatalogExercises(body)) })
    }

    override suspend fun getExerciseDetails(exerciseId: Int, workoutId: Int): Flow<ResultWrapper<ExerciseDetailsWrapper>> {
        val body = FetchExerciseRequest(exerciseId, workoutId)

        return Network.executeApiCall(dispatcher, { ExerciseDetailsWrapper(exerciseApi.getExerciseDetails(body)) })
    }

    override suspend fun addNewExerciseSet(exerciseId: Int, workoutId: Int): Flow<ResultWrapper<ExerciseWrapper>> {
        val body = FetchExerciseRequest(exerciseId, workoutId)

        return Network.executeApiCall(dispatcher, { ExerciseWrapper(exerciseApi.addNewExerciseSet(body)) })
    }

    override suspend fun deleteExerciseSet(
        exerciseId: Int,
        workoutId: Int,
        setId: UUID
    ): Flow<ResultWrapper<ExerciseWrapper>> {
        val body = DeleteExerciseSetRequest(exerciseId, workoutId, setId)

        return Network.executeApiCall(dispatcher, { ExerciseWrapper(exerciseApi.deleteExerciseSet(body)) })
    }
}