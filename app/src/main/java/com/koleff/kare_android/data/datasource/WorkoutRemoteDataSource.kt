package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.common.network.Network
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.WorkoutConfigurationDto
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.data.model.request.ExerciseAddRequest
import com.koleff.kare_android.data.model.request.FetchWorkoutByIdRequest
import com.koleff.kare_android.data.model.request.ExerciseDeletionRequest
import com.koleff.kare_android.data.model.request.FetchWorkoutConfigurationRequest
import com.koleff.kare_android.data.model.request.MultipleExercisesAddRequest
import com.koleff.kare_android.data.model.request.MultipleExercisesDeletionRequest
import com.koleff.kare_android.data.model.request.UpdateWorkoutDetailsRequest
import com.koleff.kare_android.data.model.request.UpdateWorkoutRequest
import com.koleff.kare_android.domain.wrapper.WorkoutListWrapper
import com.koleff.kare_android.domain.wrapper.WorkoutDetailsWrapper
import com.koleff.kare_android.domain.wrapper.WorkoutWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.ServerResponseData
import com.koleff.kare_android.data.remote.WorkoutApi
import com.koleff.kare_android.domain.wrapper.WorkoutDetailsListWrapper
import com.koleff.kare_android.domain.wrapper.SelectedWorkoutWrapper
import com.koleff.kare_android.domain.wrapper.WorkoutConfigurationWrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WorkoutRemoteDataSource @Inject constructor(
    private val workoutApi: WorkoutApi,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : WorkoutDataSource {
    override suspend fun selectWorkout(workoutId: Int): Flow<ResultWrapper<ServerResponseData>> {
        val body = FetchWorkoutByIdRequest(workoutId)

        return Network.executeApiCall(dispatcher, { ServerResponseData(workoutApi.selectWorkout(body)) })
    }

    override suspend fun deselectWorkout(workoutId: Int): Flow<ResultWrapper<ServerResponseData>> {
        val body = FetchWorkoutByIdRequest(workoutId)

        return Network.executeApiCall(dispatcher, { ServerResponseData(workoutApi.deselectWorkout(body)) })
    }

    override suspend fun getSelectedWorkout(): Flow<ResultWrapper<SelectedWorkoutWrapper>> {
        return Network.executeApiCall(dispatcher, { SelectedWorkoutWrapper(workoutApi.getSelectedWorkout()) })
    }

    override suspend fun getWorkout(workoutId: Int): Flow<ResultWrapper<WorkoutWrapper>> {
        val body = FetchWorkoutByIdRequest(workoutId)

        return Network.executeApiCall(dispatcher, { WorkoutWrapper(workoutApi.getWorkout(body)) })
    }

    override suspend fun getAllWorkouts(): Flow<ResultWrapper<WorkoutListWrapper>> {
        return Network.executeApiCall(dispatcher, { WorkoutListWrapper(workoutApi.getAllWorkouts()) })
    }

    override suspend fun getAllWorkoutDetails(): Flow<ResultWrapper<WorkoutDetailsListWrapper>> {
        return Network.executeApiCall(dispatcher, { WorkoutDetailsListWrapper(workoutApi.getAllWorkoutDetails()) })
    }

    override suspend fun getWorkoutDetails(workoutId: Int): Flow<ResultWrapper<WorkoutDetailsWrapper>> {
        val body = FetchWorkoutByIdRequest(workoutId)

        return Network.executeApiCall(dispatcher, { WorkoutDetailsWrapper(workoutApi.getWorkoutDetails(body)) })
    }

    override suspend fun deleteWorkout(workoutId: Int): Flow<ResultWrapper<ServerResponseData>> {
        val body = FetchWorkoutByIdRequest(workoutId)

        return Network.executeApiCall(dispatcher, { ServerResponseData(workoutApi.deleteWorkout(body)) })
    }

    override suspend fun updateWorkoutDetails(workoutDetails: WorkoutDetailsDto): Flow<ResultWrapper<ServerResponseData>> {
        val body = UpdateWorkoutDetailsRequest(workoutDetails)

        return Network.executeApiCall(dispatcher, { ServerResponseData(workoutApi.updateWorkoutDetails(body)) })
    }

    override suspend fun updateWorkout(workout: WorkoutDto): Flow<ResultWrapper<ServerResponseData>> {
        val body = UpdateWorkoutRequest(workout)

        return Network.executeApiCall(dispatcher, { ServerResponseData(workoutApi.updateWorkout(body)) })
    }

    override suspend fun deleteExercise(
        workoutId: Int,
        exerciseId: Int
    ): Flow<ResultWrapper<WorkoutDetailsWrapper>> {
        val body = ExerciseDeletionRequest(workoutId, exerciseId)

        return Network.executeApiCall(dispatcher, { WorkoutDetailsWrapper(workoutApi.deleteExercise(body)) })
    }

    override suspend fun deleteMultipleExercises(
        workoutId: Int,
        exerciseIds: List<Int>
    ): Flow<ResultWrapper<WorkoutDetailsWrapper>> {
        val body = MultipleExercisesDeletionRequest(workoutId, exerciseIds)

        return Network.executeApiCall(dispatcher, { WorkoutDetailsWrapper(workoutApi.deleteMultipleExercises(body)) })
    }

    override suspend fun addExercise(
        workoutId: Int,
        exercise: ExerciseDto
    ): Flow<ResultWrapper<WorkoutDetailsWrapper>> {
        val body = ExerciseAddRequest(workoutId, exercise)

        return Network.executeApiCall(dispatcher, { WorkoutDetailsWrapper(workoutApi.addExercise(body)) })
    }

    override suspend fun addMultipleExercises(
        workoutId: Int,
        exerciseList: List<ExerciseDto>
    ): Flow<ResultWrapper<WorkoutDetailsWrapper>> {
        val body = MultipleExercisesAddRequest(workoutId, exerciseList)

        return Network.executeApiCall(dispatcher, { WorkoutDetailsWrapper(workoutApi.addMultipleExercises(body)) })
    }

    override suspend fun submitExercise(
        workoutId: Int,
        exercise: ExerciseDto
    ): Flow<ResultWrapper<WorkoutDetailsWrapper>> {
        val body = ExerciseAddRequest(workoutId, exercise)

        return Network.executeApiCall(dispatcher, { WorkoutDetailsWrapper(workoutApi.submitExercise(body)) })
    }

    override suspend fun createNewWorkout(): Flow<ResultWrapper<WorkoutWrapper>> {
        return Network.executeApiCall(dispatcher, { WorkoutWrapper(workoutApi.createNewWorkout()) })
    }

    override suspend fun createCustomWorkout(workoutDto: WorkoutDto): Flow<ResultWrapper<WorkoutWrapper>> {
        val body = UpdateWorkoutRequest(workoutDto)

        return Network.executeApiCall(dispatcher, { WorkoutWrapper(workoutApi.createCustomWorkout(body)) })
    }

    override suspend fun createCustomWorkoutDetails(workoutDetailsDto: WorkoutDetailsDto): Flow<ResultWrapper<WorkoutDetailsWrapper>> {
        val body = UpdateWorkoutDetailsRequest(workoutDetailsDto)

        return Network.executeApiCall(dispatcher, { WorkoutDetailsWrapper(workoutApi.createCustomWorkoutDetails(body)) })
    }

    override suspend fun getWorkoutConfiguration(workoutId: Int): Flow<ResultWrapper<WorkoutConfigurationWrapper>> {
        val body = FetchWorkoutByIdRequest(workoutId)

        return Network.executeApiCall(dispatcher, { WorkoutConfigurationWrapper(workoutApi.getWorkoutConfiguration(body)) })
    }

    override suspend fun updateWorkoutConfiguration(workoutConfiguration: WorkoutConfigurationDto): Flow<ResultWrapper<ServerResponseData>> {
        val body = FetchWorkoutConfigurationRequest(workoutConfiguration)

        return Network.executeApiCall(dispatcher, { ServerResponseData(workoutApi.updateWorkoutConfiguration(body)) })
    }

    override suspend fun saveWorkoutConfiguration(workoutConfiguration: WorkoutConfigurationDto): Flow<ResultWrapper<WorkoutConfigurationWrapper>> {
        val body = FetchWorkoutConfigurationRequest(workoutConfiguration)

        return Network.executeApiCall(dispatcher, { WorkoutConfigurationWrapper(workoutApi.saveWorkoutConfiguration(body)) })
    }

    override suspend fun deleteWorkoutConfiguration(workoutId: Int): Flow<ResultWrapper<ServerResponseData>> {
        val body = FetchWorkoutByIdRequest(workoutId)

        return Network.executeApiCall(dispatcher, { ServerResponseData(workoutApi.deleteWorkoutConfiguration(body)) })
    }
}