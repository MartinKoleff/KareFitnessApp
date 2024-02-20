package com.koleff.kare_android.workout.data

import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.data.datasource.WorkoutDataSource
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.data.model.response.WorkoutsListResponse
import com.koleff.kare_android.data.model.response.SelectedWorkoutResponse
import com.koleff.kare_android.data.model.response.WorkoutDetailsListResponse
import com.koleff.kare_android.data.model.response.WorkoutDetailsResponse
import com.koleff.kare_android.data.model.response.WorkoutResponse
import com.koleff.kare_android.data.model.response.base_response.BaseResponse
import com.koleff.kare_android.domain.wrapper.WorkoutDetailsListWrapper
import com.koleff.kare_android.domain.wrapper.WorkoutListWrapper
import com.koleff.kare_android.domain.wrapper.WorkoutDetailsWrapper
import com.koleff.kare_android.domain.wrapper.WorkoutWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.SelectedWorkoutWrapper
import com.koleff.kare_android.domain.wrapper.ServerResponseData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import io.mockk.coEvery
import io.mockk.mockk

@Deprecated("Unused.")
class WorkoutMockupDataSource(private val isError: Boolean = false) :
    WorkoutDataSource {

    private val mockWorkout = mockk<WorkoutDto>(relaxed = true)
    private val mockWorkoutDetails = mockk<WorkoutDetailsDto>(relaxed = true)

    private val mockWorkoutsList =
        mockk<MutableList<WorkoutDto>>(relaxed = true)

    private val mockWorkoutDetailsList =
        mockk<MutableList<WorkoutDetailsDto>>(relaxed = true)

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

    override suspend fun deselectWorkout(workoutId: Int): Flow<ResultWrapper<ServerResponseData>> =
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

    override suspend fun getSelectedWorkout(): Flow<ResultWrapper<SelectedWorkoutWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            coEvery { mockWorkout.isSelected } returns true

            val mockupResult = SelectedWorkoutWrapper(
                SelectedWorkoutResponse(mockWorkout)
            )

            if (isError) {
                emit(ResultWrapper.ApiError())
            } else {
                emit(ResultWrapper.Success(mockupResult))
            }
        }

    override suspend fun getWorkout(workoutId: Int): Flow<ResultWrapper<WorkoutWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            coEvery { mockWorkout.workoutId } returns workoutId

            val mockupResult = WorkoutWrapper(
                WorkoutResponse(mockWorkout)
            )

            if (isError) {
                emit(ResultWrapper.ApiError())
            } else {
                emit(ResultWrapper.Success(mockupResult))
            }
        }

    override suspend fun getAllWorkouts(): Flow<ResultWrapper<WorkoutListWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val mockupResult = WorkoutListWrapper(
                WorkoutsListResponse(mockWorkoutsList)
            )

            if (isError) {
                emit(ResultWrapper.ApiError())
            } else {
                emit(ResultWrapper.Success(mockupResult))
            }
        }

    override suspend fun getAllWorkoutDetails(): Flow<ResultWrapper<WorkoutDetailsListWrapper>>  =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val mockupResult = WorkoutDetailsListWrapper(
                WorkoutDetailsListResponse(mockWorkoutDetailsList)
            )

            if (isError) {
                emit(ResultWrapper.ApiError())
            } else {
                emit(ResultWrapper.Success(mockupResult))
            }
        }

    override suspend fun getWorkoutDetails(workoutId: Int): Flow<ResultWrapper<WorkoutDetailsWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            coEvery { mockWorkoutDetails.workoutId } returns workoutId


            val mockupResult = WorkoutDetailsWrapper(
                WorkoutDetailsResponse(mockWorkoutDetails)
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

    override suspend fun updateWorkoutDetails(workoutDetails: WorkoutDetailsDto): Flow<ResultWrapper<ServerResponseData>> =
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
    ): Flow<ResultWrapper<WorkoutDetailsWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            coEvery { mockWorkoutDetails.workoutId } returns workoutId
            coEvery { mockWorkoutDetails.exercises } returns mockWorkoutDetails.exercises
                .filter { it.exerciseId == exerciseId }
                .toMutableList()

            val mockupResult = WorkoutDetailsWrapper(
                WorkoutDetailsResponse(mockWorkoutDetails)
            )

            if (isError) {
                emit(ResultWrapper.ApiError())
            } else {
                emit(ResultWrapper.Success(mockupResult))
            }
        }

    override suspend fun addExercise(
        workoutId: Int,
        exercise: ExerciseDto
    ): Flow<ResultWrapper<WorkoutDetailsWrapper>>  =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            coEvery { mockWorkoutDetails.workoutId } returns workoutId
            coEvery { mockWorkoutDetails.exercises } returns mockWorkoutDetails.exercises
                .toMutableList()

            mockWorkoutDetails.exercises.add(exercise)

            val mockupResult = WorkoutDetailsWrapper(
                WorkoutDetailsResponse(mockWorkoutDetails)
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

    override suspend fun createNewWorkout(): Flow<ResultWrapper<WorkoutWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

//            mockWorkoutsList.add(mockWorkout)

            val mockupResult = WorkoutWrapper(
                WorkoutResponse(mockWorkout)
            )

            if (isError) {
                emit(ResultWrapper.ApiError())
            } else {
                emit(ResultWrapper.Success(mockupResult))
            }
        }

    override suspend fun createCustomWorkout(workoutDto: WorkoutDto): Flow<ResultWrapper<WorkoutWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

//            mockWorkoutsList.add(mockWorkout)

            val mockupResult = WorkoutWrapper(
                WorkoutResponse(workoutDto)
            )

            if (isError) {
                emit(ResultWrapper.ApiError())
            } else {
                emit(ResultWrapper.Success(mockupResult))
            }
        }

    override suspend fun createCustomWorkoutDetails(workoutDetailsDto: WorkoutDetailsDto): Flow<ResultWrapper<WorkoutDetailsWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

//            mockWorkoutsDetailsList.add(mockWorkout)

            val mockupResult = WorkoutDetailsWrapper(
                WorkoutDetailsResponse(workoutDetailsDto)
            )

            if (isError) {
                emit(ResultWrapper.ApiError())
            } else {
                emit(ResultWrapper.Success(mockupResult))
            }
        }
}