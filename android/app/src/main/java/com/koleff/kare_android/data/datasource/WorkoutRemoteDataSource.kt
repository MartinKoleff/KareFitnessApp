package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.common.Network
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.data.model.dto.SaveWorkoutDto
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.data.model.request.BaseWorkoutRequest
import com.koleff.kare_android.data.model.request.DeleteExerciseRequest
import com.koleff.kare_android.data.model.request.SaveWorkoutRequest
import com.koleff.kare_android.data.model.request.UpdateWorkoutRequest
import com.koleff.kare_android.domain.wrapper.GetAllWorkoutsWrapper
import com.koleff.kare_android.domain.wrapper.GetWorkoutDetailsWrapper
import com.koleff.kare_android.domain.wrapper.GetWorkoutWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.ServerResponseData
import com.koleff.kare_android.data.remote.WorkoutApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WorkoutRemoteDataSource @Inject constructor(
    private val workoutApi: WorkoutApi,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : WorkoutDataSource {
    override suspend fun selectWorkout(workoutId: Int): Flow<ResultWrapper<ServerResponseData>> {
        val body = BaseWorkoutRequest(workoutId)

        return Network.executeApiCall(dispatcher, { ServerResponseData(workoutApi.selectWorkout(body)) })
    }

    override suspend fun getSelectedWorkout(): Flow<ResultWrapper<GetWorkoutWrapper>> {
        return Network.executeApiCall(dispatcher, { GetWorkoutWrapper(workoutApi.getSelectedWorkout()) })
    }

    override suspend fun getWorkout(workoutId: Int): Flow<ResultWrapper<GetWorkoutWrapper>> {
        val body = BaseWorkoutRequest(workoutId)

        return Network.executeApiCall(dispatcher, { GetWorkoutWrapper(workoutApi.getWorkout(body)) })
    }

    override suspend fun getAllWorkouts(): Flow<ResultWrapper<GetAllWorkoutsWrapper>> {
        return Network.executeApiCall(dispatcher, { GetAllWorkoutsWrapper(workoutApi.getAllWorkouts()) })
    }

    override suspend fun getWorkoutDetails(workoutId: Int): Flow<ResultWrapper<GetWorkoutDetailsWrapper>> {
        val body = BaseWorkoutRequest(workoutId)

        return Network.executeApiCall(dispatcher, { GetWorkoutDetailsWrapper(workoutApi.getWorkoutDetails(body)) })
    }

    override suspend fun deleteWorkout(workoutId: Int): Flow<ResultWrapper<ServerResponseData>> {
        val body = BaseWorkoutRequest(workoutId)

        return Network.executeApiCall(dispatcher, { ServerResponseData(workoutApi.deleteWorkout(body)) })
    }

    override suspend fun saveWorkout(workout: WorkoutDetailsDto): Flow<ResultWrapper<ServerResponseData>> {
        val body = SaveWorkoutRequest(workout)

        return Network.executeApiCall(dispatcher, { ServerResponseData(workoutApi.saveWorkout(body)) })
    }

    override suspend fun updateWorkout(workout: WorkoutDto): Flow<ResultWrapper<ServerResponseData>> {
        val body = UpdateWorkoutRequest(workout)

        return Network.executeApiCall(dispatcher, { ServerResponseData(workoutApi.updateWorkout(body)) })
    }

    override suspend fun deleteExercise(
        workoutId: Int,
        exerciseId: Int
    ): Flow<ResultWrapper<GetWorkoutDetailsWrapper>> {
        val body = DeleteExerciseRequest(workoutId, exerciseId)

        return Network.executeApiCall(dispatcher, { GetWorkoutDetailsWrapper(workoutApi.deleteExercise(body)) })
    }

    override suspend fun createWorkout(): Flow<ResultWrapper<GetWorkoutWrapper>> {
        return Network.executeApiCall(dispatcher, { GetWorkoutWrapper(workoutApi.createWorkout()) })
    }
}