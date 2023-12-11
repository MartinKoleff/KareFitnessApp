package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.data.model.dto.SaveWorkoutDto
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
import com.koleff.kare_android.data.room.entity.Workout
import com.koleff.kare_android.data.room.manager.WorkoutDBManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WorkoutLocalDataSource @Inject constructor(
    private val workoutDao: WorkoutDao,
    private val workoutDBManager: WorkoutDBManager
) : WorkoutDataSource {
    override suspend fun selectWorkout(workoutId: Int): Flow<ResultWrapper<ServerResponseData>> =
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
            GetWorkoutResponse(data.toWorkoutDto())
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
            GetAllWorkoutsResponse(data.map(Workout::toWorkoutDto))
        )

        emit(ResultWrapper.Success(result))
    }

    override suspend fun getWorkoutDetails(workoutId: Int): Flow<ResultWrapper<GetWorkoutDetailsWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val data = workoutDao.getWorkoutById(workoutId)

            val result = GetWorkoutDetailsWrapper(
                GetWorkoutDetailsResponse(data.workoutDetails.toWorkoutDetailsDto())
            )

            emit(ResultWrapper.Success(result))
        }

//    private fun getWorkoutExercises(workoutId: Int): List<ExerciseDto> {
//        val data = workoutDao.getWorkoutExercises(workoutId)
//
//        return data.exercises.map(Exercise::toExerciseDto)
//    }

    override suspend fun deleteWorkout(workoutId: Int): Flow<ResultWrapper<ServerResponseData>> =
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

            //TODO: parse SaveWorkoutDto to Workout... ExerciseDto to Exercise
//            val workoutEntity = Workout(
//                name = workout.name,
//                muscleGroup = workout.muscleGroup,
//                snapshot = workout.snapshot,
//                totalExercises = workout.exercises.size,
//                isSelected = workout.isSelected,
//                exercises = workout.exercises.map(Exercise::toExercise),
//            )
//
//            workoutDao.insertWorkout(workout)

            val result = ServerResponseData(
                BaseResponse()
            )

            emit(ResultWrapper.Success(result))
        }
}