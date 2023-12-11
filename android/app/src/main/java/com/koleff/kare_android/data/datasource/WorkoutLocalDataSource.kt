package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.common.Network
import com.koleff.kare_android.data.model.dto.ExerciseDetailsDto
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.SaveWorkoutDto
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.request.BaseWorkoutRequest
import com.koleff.kare_android.data.model.request.SaveWorkoutRequest
import com.koleff.kare_android.data.model.response.GetAllWorkoutsResponse
import com.koleff.kare_android.data.model.response.GetWorkoutDetailsResponse
import com.koleff.kare_android.data.model.response.GetWorkoutResponse
import com.koleff.kare_android.data.model.response.base_response.BaseResponse
import com.koleff.kare_android.data.model.wrapper.GetAllWorkoutsWrapper
import com.koleff.kare_android.data.model.wrapper.GetWorkoutDetailsWrapper
import com.koleff.kare_android.data.model.wrapper.GetWorkoutWrapper
import com.koleff.kare_android.data.model.wrapper.ResultWrapper
import com.koleff.kare_android.data.model.wrapper.ServerResponseData
import com.koleff.kare_android.data.room.dao.WorkoutDao
import com.koleff.kare_android.data.room.manager.WorkoutDBManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WorkoutLocalDataSource @Inject constructor(
    private val workoutDao: WorkoutDao,
    private val workoutDBManager: WorkoutDBManager
) : WorkoutDataSource {
    override suspend fun selectWorkout(workoutId: String): Flow<ResultWrapper<ServerResponseData>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            workoutDao.selectWorkoutById(workoutId)

            val result = ServerResponseData(
                BaseResponse()
            )

            emit(ResultWrapper.Success(result))
        }

    override suspend fun getSelectedWorkout(): Flow<ResultWrapper<GetWorkoutWrapper>> = flow {
        emit(ResultWrapper.Loading())
        delay(Constants.fakeDelay)

        val data = workoutDao.getWorkoutByIsSelected()

        val result = GetWorkoutWrapper(
            GetWorkoutResponse(data)
        )

        emit(ResultWrapper.Success(result))
    }

    override suspend fun getAllWorkouts(): Flow<ResultWrapper<GetAllWorkoutsWrapper>> = flow {
        emit(ResultWrapper.Loading())
        delay(Constants.fakeDelay)

        //Check if Room DB has data
        if (!workoutDBManager.hasInitializedWorkoutTableRoomDB) {
            workoutDBManager.initializeWorkoutTableRoomDB(workoutDao)
        }

        val data = workoutDao.getWorkoutsOrderedById()

        val result = GetAllWorkoutsWrapper(
            GetAllWorkoutsResponse(data)
        )

        emit(ResultWrapper.Success(result))
    }

    override suspend fun getWorkoutDetails(workoutId: String): Flow<ResultWrapper<GetWorkoutDetailsWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val data = workoutDao.getWorkoutById(workoutId)
            val description =
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc interdum nibh nec pharetra iaculis. Aenean ultricies egestas leo at ultricies. Quisque suscipit, purus ut congue porta, eros eros tincidunt sem, sed commodo magna metus eu nibh. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Vestibulum quis velit eget eros malesuada luctus. Suspendisse iaculis ullamcorper condimentum. Sed metus augue, dapibus eu venenatis vitae, ornare non turpis. Donec suscipit iaculis dolor, id fermentum mauris interdum in. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas."
            val exercises = getWorkoutExercises(workoutId)

            val result = GetWorkoutDetailsWrapper(
                GetWorkoutDetailsResponse(
                    WorkoutDetailsDto(
                        workoutId = workoutId,
                        name = data.name,
                        description = description,
                        muscleGroup = data.muscleGroup,
                        exercises = exercises,
                        isSelected = data.isSelected
                    )
                )
            )

            emit(ResultWrapper.Success(result))
        }

    private fun getWorkoutExercises(workoutId: String): List<ExerciseDto> {
        when(workoutId){
            "Workout1" -> {

            }
            "Workout2" -> {

            }
            "Workout3" -> {

            }
            else -> {
                return emptyList()
            }
        }
    }

    override suspend fun deleteWorkout(workoutId: String): Flow<ResultWrapper<ServerResponseData>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            workoutDao.deleteWorkout(workoutId)

            val result = ServerResponseData(
                BaseResponse()
            )

            emit(ResultWrapper.Success(result))
        }

    override suspend fun saveWorkout(workout: SaveWorkoutDto): Flow<ResultWrapper<ServerResponseData>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            workoutDao.insertWorkout(workout)

            val result = ServerResponseData(
                BaseResponse()
            )

            emit(ResultWrapper.Success(result))
        }
}