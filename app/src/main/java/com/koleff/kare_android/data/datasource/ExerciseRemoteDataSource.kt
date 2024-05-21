package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.common.network.NetworkManager
import com.koleff.kare_android.data.model.dto.ExerciseSetDto
import com.koleff.kare_android.data.model.request.AddNewExerciseSetRequest
import com.koleff.kare_android.data.model.request.DeleteExerciseSetRequest
import com.koleff.kare_android.data.model.request.FetchExerciseRequest
import com.koleff.kare_android.data.model.request.FetchExercisesByMuscleGroupRequest
import com.koleff.kare_android.data.remote.ExerciseApi
import com.koleff.kare_android.domain.wrapper.ExerciseDetailsWrapper
import com.koleff.kare_android.domain.wrapper.ExerciseListWrapper
import com.koleff.kare_android.domain.wrapper.ExerciseWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

class ExerciseRemoteDataSource @Inject constructor(
    private val exerciseApi: ExerciseApi,
    private val networkManager: NetworkManager,
    @IoDispatcher val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ExerciseDataSource {

    override suspend fun getExercise(
        exerciseId: Int,
        workoutId: Int
    ): Flow<ResultWrapper<ExerciseWrapper>> {
        val body = FetchExerciseRequest(exerciseId, Constants.CATALOG_EXERCISE_ID)

        return networkManager.executeApiCall(
            dispatcher,
            { ExerciseWrapper(exerciseApi.getExercise(body)) }
        )
    }

    override suspend fun getCatalogExercise(exerciseId: Int): Flow<ResultWrapper<ExerciseWrapper>> {
        val body = FetchExerciseRequest(exerciseId, Constants.CATALOG_EXERCISE_ID)

        return networkManager.executeApiCall(
            dispatcher,
            { ExerciseWrapper(exerciseApi.getCatalogExercise(body)) }
        )
    }

    override suspend fun getCatalogExercises(muscleGroupId: Int): Flow<ResultWrapper<ExerciseListWrapper>> {
        val body = FetchExercisesByMuscleGroupRequest(muscleGroupId)

        return networkManager.executeApiCall(
            dispatcher,
            { ExerciseListWrapper(exerciseApi.getCatalogExercises(body)) }
        )
    }

    override suspend fun getExerciseDetails(
        exerciseId: Int,
        workoutId: Int
    ): Flow<ResultWrapper<ExerciseDetailsWrapper>> {
        val body = FetchExerciseRequest(exerciseId, workoutId)

        return networkManager.executeApiCall(
            dispatcher,
            { ExerciseDetailsWrapper(exerciseApi.getExerciseDetails(body)) }
        )
    }

    override suspend fun addNewExerciseSet(
        exerciseId: Int,
        workoutId: Int,
        currentSets: List<ExerciseSetDto>
    ): Flow<ResultWrapper<ExerciseWrapper>> {
        val body = AddNewExerciseSetRequest(exerciseId, workoutId, currentSets)

        return networkManager.executeApiCall(
            dispatcher,
            { ExerciseWrapper(exerciseApi.addNewExerciseSet(body)) }
        )
    }

    override suspend fun deleteExerciseSet(
        exerciseId: Int,
        workoutId: Int,
        setId: UUID,
        currentSets: List<ExerciseSetDto>
    ): Flow<ResultWrapper<ExerciseWrapper>> {
        val body = DeleteExerciseSetRequest(exerciseId, workoutId, setId, currentSets)

        return networkManager.executeApiCall(
            dispatcher,
            { ExerciseWrapper(exerciseApi.deleteExerciseSet(body)) }
        )
    }
}