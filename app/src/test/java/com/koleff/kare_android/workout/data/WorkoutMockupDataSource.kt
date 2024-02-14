package com.koleff.kare_android.workout.data

import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.common.MockupDataGenerator
import com.koleff.kare_android.data.datasource.WorkoutDataSource
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.data.model.response.GetAllWorkoutsResponse
import com.koleff.kare_android.data.model.response.GetSelectedWorkoutResponse
import com.koleff.kare_android.data.model.response.GetWorkoutDetailsResponse
import com.koleff.kare_android.data.model.response.GetWorkoutResponse
import com.koleff.kare_android.data.model.response.base_response.BaseResponse
import com.koleff.kare_android.domain.wrapper.GetAllWorkoutsWrapper
import com.koleff.kare_android.domain.wrapper.GetWorkoutDetailsWrapper
import com.koleff.kare_android.domain.wrapper.GetWorkoutWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.GetSelectedWorkoutWrapper
import com.koleff.kare_android.domain.wrapper.ServerResponseData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import io.mockk.coEvery
import io.mockk.mockk

class WorkoutMockupDataSource(private val isError: Boolean = false) :
    WorkoutDataSource {

    private val mockWorkout = mockk<WorkoutDto>(relaxed = true)
    private val mockWorkoutDetails = mockk<WorkoutDetailsDto>(relaxed = true)

    private val mockWorkoutsList = mockk<MutableList<WorkoutDto>>(relaxed = true) //listOf(mockWorkout, mockWorkout) // Example with two mocked workouts

    init {
        //Setup your mocks here
        //For example, to always return a specific workout ID when accessing mockWorkout.workoutId
    }

    override suspend fun selectWorkout(workoutId: Int): Flow<ResultWrapper<ServerResponseData>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            //Not selecting anything just simulating result...

            val mockupResult = ServerResponseData(
                BaseResponse()
            )

            if (isError) {
                emit(ResultWrapper.ApiError())
            } else {
                emit(ResultWrapper.Success(mockupResult))
            }
        }

    override suspend fun getSelectedWorkout(): Flow<ResultWrapper<GetSelectedWorkoutWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            coEvery { mockWorkout.isSelected } returns true

            val mockupResult = GetSelectedWorkoutWrapper(
                GetSelectedWorkoutResponse(mockWorkout)
            )

            if (isError) {
                emit(ResultWrapper.ApiError())
            } else {
                emit(ResultWrapper.Success(mockupResult))
            }
        }

    override suspend fun getWorkout(workoutId: Int): Flow<ResultWrapper<GetWorkoutWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            coEvery { mockWorkout.workoutId } returns workoutId

            val mockupResult = GetWorkoutWrapper(
                GetWorkoutResponse(mockWorkout)
            )

            if (isError) {
                emit(ResultWrapper.ApiError())
            } else {
                emit(ResultWrapper.Success(mockupResult))
            }
        }

    override suspend fun getAllWorkouts(): Flow<ResultWrapper<GetAllWorkoutsWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val mockupResult = GetAllWorkoutsWrapper(
                GetAllWorkoutsResponse(mockWorkoutsList)
            )

            if (isError) {
                emit(ResultWrapper.ApiError())
            } else {
                emit(ResultWrapper.Success(mockupResult))
            }
        }

    override suspend fun getWorkoutDetails(workoutId: Int): Flow<ResultWrapper<GetWorkoutDetailsWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            coEvery { mockWorkoutDetails.workoutId } returns workoutId


            val mockupResult = GetWorkoutDetailsWrapper(
                GetWorkoutDetailsResponse(mockWorkoutDetails)
            )

            if (isError) {
                emit(ResultWrapper.ApiError())
            } else {
                emit(ResultWrapper.Success(mockupResult))
            }
        }

    override suspend fun deleteWorkout(workoutId: Int): Flow<ResultWrapper<ServerResponseData>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            //Not deleting anything just simulating result...

            val mockupResult = ServerResponseData(
                BaseResponse()
            )

            if (isError) {
                emit(ResultWrapper.ApiError())
            } else {
                emit(ResultWrapper.Success(mockupResult))
            }
        }

    override suspend fun saveWorkout(workout: WorkoutDetailsDto): Flow<ResultWrapper<ServerResponseData>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            //Not deleting anything just simulating result...

            val mockupResult = ServerResponseData(
                BaseResponse()
            )

            if (isError) {
                emit(ResultWrapper.ApiError())
            } else {
                emit(ResultWrapper.Success(mockupResult))
            }
        }

    override suspend fun deleteExercise(
        workoutId: Int,
        exerciseId: Int
    ): Flow<ResultWrapper<GetWorkoutDetailsWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            coEvery { mockWorkoutDetails.workoutId } returns workoutId
            coEvery { mockWorkoutDetails.exercises } returns mockWorkoutDetails.exercises
                .filter { it.exerciseId == exerciseId }
                .toMutableList()

            val mockupResult = GetWorkoutDetailsWrapper(
                GetWorkoutDetailsResponse(mockWorkoutDetails)
            )

            if (isError) {
                emit(ResultWrapper.ApiError())
            } else {
                emit(ResultWrapper.Success(mockupResult))
            }
        }

    override suspend fun updateWorkout(workout: WorkoutDto): Flow<ResultWrapper<ServerResponseData>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            //Not updating anything just simulating result...

            val mockupResult = ServerResponseData(
                BaseResponse()
            )

            if (isError) {
                emit(ResultWrapper.ApiError())
            } else {
                emit(ResultWrapper.Success(mockupResult))
            }
        }

    override suspend fun createWorkout(): Flow<ResultWrapper<GetWorkoutWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

//            mockWorkoutsList.add(mockWorkout)

            val mockupResult = GetWorkoutWrapper(
                GetWorkoutResponse(mockWorkout)
            )

            if (isError) {
                emit(ResultWrapper.ApiError())
            } else {
                emit(ResultWrapper.Success(mockupResult))
            }
        }
}